package com.juan_pablo.adopcion_mascotas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AdopcionMascotasApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AdopcionMascotasApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
//		var password = "secret"; //mail & password
//		var passwordEncoded = this.passwordEncoder.encode(password);
//		System.out.println(passwordEncoded);
	}
}
