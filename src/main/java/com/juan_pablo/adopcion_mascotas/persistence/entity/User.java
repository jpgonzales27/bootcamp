package com.juan_pablo.adopcion_mascotas.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message ="{generic.notblank}" )
    @Size(max = 255, message = "{generic.size}")
    @Column(nullable = false)
    private String name;

    @Email(message ="{generic.email}")
    @Column(nullable = false,unique = true)
    private String email;

    @NotBlank(message = "{generic.notblank}")
    private String password;

    @Size(min = 5, max = 8,message = "{generic.size}")
    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "user")
    private List<Adoption> adoptions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

}
