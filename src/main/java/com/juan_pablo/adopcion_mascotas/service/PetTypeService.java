package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;

import java.util.List;

public interface PetTypeService {
    List<PetType> findAllPetTypes();
    PetType findPetTypeById(Long id );
    PetType savePetType(PetType petType);
    PetType updatePetTypeById( Long id, PetType petType );
    void deletePetTypeById( Long id );
}
