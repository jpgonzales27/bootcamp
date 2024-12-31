package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria.PetSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PetService {
    Page<Pet> findAllPets(PetSearchCriteria petSearchCriteria, Pageable pageable);
    Pet findPetById( Long id );
    GetPetDTO savePet(Pet pet );
    GetPetDTO updatePetById( Long id, Pet pet );
    void deletePetById( Long id );

}
