package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.persistence.repository.PetTypeCrudRepository;
import com.juan_pablo.adopcion_mascotas.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PetTypeServiceImpl implements PetTypeService {


    @Autowired
    private PetTypeCrudRepository petTypeCrudRepository;

    @Override
    public List<PetType> findAllPetTypes() {
        return petTypeCrudRepository.findAll();
    }

    @Override
    public PetType findPetTypeById(Long id) {
        return petTypeCrudRepository.findById(id).orElseThrow();
    }

    @Override
    public PetType savePetType(PetType petType) {
        return petTypeCrudRepository.save(petType);
    }

    @Override
    public PetType updatePetTypeById(Long id, PetType petType) {
        PetType oldPetType = petTypeCrudRepository.findById(id).orElseThrow();
        oldPetType.setTypeName(petType.getTypeName());
        return petTypeCrudRepository.save(oldPetType);
    }

    @Override
    public void deletePetTypeById(Long id) {
        PetType exits = petTypeCrudRepository.findById(id).orElseThrow();
        petTypeCrudRepository.delete(exits);
    }
}
