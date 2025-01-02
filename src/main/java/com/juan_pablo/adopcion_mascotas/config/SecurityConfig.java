package com.juan_pablo.adopcion_mascotas.config;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Role;
import com.juan_pablo.adopcion_mascotas.persistence.repository.UserCrudRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserCrudRepository userRepository) {
        return username -> {

            System.out.println("username: " + username);

            var userEntity = userRepository.findByEmailWithRoles(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

            // Convertir roles a SimpleGrantedAuthority
            var authorities = userEntity.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) // Convertir el nombre de rol
                    .collect(Collectors.toList());

            // Crear y devolver un UserDetails con los roles convertidos
            return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Permitir acceso a Swagger
                        .requestMatchers("/usuarios/**").hasRole("ADMIN") // Solo accesible para el rol ADMIN
                        .requestMatchers("/mascotas/**", "/adopciones/**").hasAnyRole("ADMIN", "USER") // Acceso para ADMIN y USER
                        .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
                )
                .httpBasic(Customizer.withDefaults()); // Usa autenticación básica (usuario y contraseña)

        return httpSecurity.build();
    }
}
