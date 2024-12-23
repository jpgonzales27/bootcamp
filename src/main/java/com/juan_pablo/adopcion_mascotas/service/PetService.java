package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;

import java.util.List;

public interface PetService {
    List<Pet> findAllPets(String name, Long typeId,String typeName, Integer minAge, Integer maxAge, Boolean available);
    Pet findPetById( Long id );
    GetPetDTO savePet(Pet pet );
    Pet updatePetById( Long id, Pet pet );
    void deletePetById( Long id );

}
