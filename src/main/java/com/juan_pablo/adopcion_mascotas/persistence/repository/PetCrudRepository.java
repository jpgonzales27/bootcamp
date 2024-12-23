package com.juan_pablo.adopcion_mascotas.persistence.repository;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PetCrudRepository extends JpaRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {
}
