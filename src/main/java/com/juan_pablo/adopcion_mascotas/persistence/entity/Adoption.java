package com.juan_pablo.adopcion_mascotas.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message ="{generic.notblank}" )
    @Column(name = "pet_id",nullable = false)
    private Long petId;

    @NotBlank(message ="{generic.notblank}" )
    @Column(name = "user_id",nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "pet_id", insertable = false, updatable = false)
    @JsonIgnore
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    @NotBlank(message ="{generic.notblank}" )
    @Temporal(TemporalType.DATE)
    private Date adoptionDate;

}
