package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PetService {
    Page<Pet> findAllPets(String name, Long typeId,String typeName, Integer minAge, Integer maxAge, Boolean available, Pageable pageable);
    Pet findPetById( Long id );
    GetPetDTO savePet(Pet pet );
    Pet updatePetById( Long id, Pet pet );
    void deletePetById( Long id );

}
