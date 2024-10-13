package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;

import java.util.List;

public interface PetService {
    List<Pet> findAllPets();
    List<Pet> findAllByName(String name);
    List<Pet> findAllByPetTypeTypeName(String typeName);
    List<Pet> findByAvailability(Boolean available);
    Pet findPetById( Long id );
    Pet savePet(Pet pet );
    Pet updatePetById( Long id, Pet pet );
    void deletePetById( Long id );

}
