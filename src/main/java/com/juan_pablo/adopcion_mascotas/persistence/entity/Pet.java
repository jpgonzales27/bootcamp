package com.juan_pablo.adopcion_mascotas.persistence.entity;

import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message ="{generic.notblank}" )
    @Size(min = 1, max = 255,message = "{generic.size}")
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "petType_id")
    private PetType petType;

    @Column(nullable = false)
    @PositiveOrZero
    private Integer age;

    @NotBlank(message ="{generic.notblank}" )
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(nullable = false)
    private Boolean available;

    @OneToMany(mappedBy = "pet")
    private List<Adoption> adoptions;

}
