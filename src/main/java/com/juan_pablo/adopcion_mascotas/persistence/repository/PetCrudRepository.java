package com.juan_pablo.adopcion_mascotas.persistence.repository;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetCrudRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByName(String name);
    List<Pet> findAllByPetTypeTypeName(String petType);
    List<Pet> findByAvailable(Boolean available);

}
