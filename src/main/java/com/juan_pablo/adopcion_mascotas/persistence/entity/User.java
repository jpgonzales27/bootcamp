package com.juan_pablo.adopcion_mascotas.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @Column(nullable = false)
    private String email;

    @Size(min = 0, max = 8,message = "{generic.size}")
    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "user")
    private List<Adoption> adoptions;

}
