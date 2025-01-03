package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.mapper.PetMapper;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import com.juan_pablo.adopcion_mascotas.persistence.repository.PetCrudRepository;
import com.juan_pablo.adopcion_mascotas.persistence.repository.PetTypeCrudRepository;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.PetSpecifications;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria.PetSearchCriteria;
import com.juan_pablo.adopcion_mascotas.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetCrudRepository petCrudRepository;
    private final PetTypeCrudRepository petTypeRepository;

    @Override
    public Page<Pet> findAllPets(PetSearchCriteria petSearchCriteria, Pageable pageable) {
        PetSpecifications petSpecifications = new PetSpecifications(petSearchCriteria);
        return petCrudRepository.findAll(petSpecifications, pageable);
    }

    @Override
    public Pet findPetById(Long id) {
        return petCrudRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("[Pet: " + Long.toString(id) + "]")
        );
    }

    private PetType findPetTypeById(Pet pet) {
        Long id = pet.getPetType().getId();
        return petTypeRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("[PetType: " + Long.toString(id) + "]")
        );
    }

    @Override
    public GetPetDTO savePet(Pet pet) {
        PetType petType = findPetTypeById(pet);
        pet.setPetType(petType);
        Pet result = petCrudRepository.save(pet);
        return PetMapper.INSTANCE.fromEntityToDto(result);
    }

    @Override
    public GetPetDTO updatePetById(Long id, Pet pet) {
        PetType petType = findPetTypeById(pet);
        Pet oldPet = this.findPetById(id);
        pet.setPetType(petType);
        oldPet.setName(pet.getName());
        oldPet.setAge(pet.getAge());
        oldPet.setGenre(pet.getGenre());
        oldPet.setPetType(pet.getPetType());
        oldPet.setAvailable(pet.getAvailable());
        Pet result = petCrudRepository.save(oldPet);
        return PetMapper.INSTANCE.fromEntityToDto(result);
    }

    @Override
    public void deletePetById(Long id) {
        Pet exist = this.findPetById(id);
        petCrudRepository.delete(exist);
    }
}
