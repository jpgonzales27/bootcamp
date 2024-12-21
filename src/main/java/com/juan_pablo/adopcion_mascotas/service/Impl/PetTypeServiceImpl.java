package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetTypeDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.mapper.PetTypeMapper;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.persistence.repository.PetTypeCrudRepository;
import com.juan_pablo.adopcion_mascotas.service.PetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeCrudRepository petTypeCrudRepository;

    private final PetTypeMapper petTypeMapper;

    @Override
    public List<GetPetTypeDTO> findAllPetTypes() {
        List<PetType> result = petTypeCrudRepository.findAll();
        return petTypeMapper.fromEntityListToDtoList(result);
    }

    @Override
    public PetType findPetTypeById(Long id) {
        return petTypeCrudRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("[Adoption: " + Long.toString(id) + "]"));
    }

    @Override
    public PetType savePetType(PetType petType) {
        return petTypeCrudRepository.save(petType);
    }

    @Override
    public PetType updatePetTypeById(Long id, PetType petType) {
        PetType oldPetType = this.findPetTypeById(id);
        oldPetType.setTypeName(petType.getTypeName());
        return petTypeCrudRepository.save(oldPetType);
    }

    @Override
    public void deletePetTypeById(Long id) {
        PetType exits = this.findPetTypeById(id);
        petTypeCrudRepository.delete(exits);
    }
}
