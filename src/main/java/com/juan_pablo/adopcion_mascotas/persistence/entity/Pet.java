package com.juan_pablo.adopcion_mascotas.persistence.entity;

import jakarta.persistence.*;
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
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "petType_id")
    private PetType petType;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Boolean available;

    @OneToMany(mappedBy = "pet")
    private List<Adoption> adoptions;

}
