package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.mapper.PetMapper;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.persistence.repository.PetCrudRepository;
import com.juan_pablo.adopcion_mascotas.persistence.repository.PetTypeCrudRepository;
import com.juan_pablo.adopcion_mascotas.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetCrudRepository petCrudRepository;
    private final PetTypeCrudRepository petTypeRepository;
//    private final PetMapper petMapper;

    @Override
    public List<Pet> findAllPets() {
        return petCrudRepository.findAll();
    }

    @Override
    public List<Pet> findAllByName(String name) {
        return petCrudRepository.findAllByName(name);
    }

    @Override
    public List<Pet> findAllByPetTypeTypeName(String typeName) {
        return petCrudRepository.findAllByPetTypeTypeName(typeName);
    }

    @Override
    public List<Pet> findByAvailability(Boolean available) {
        return petCrudRepository.findByAvailable(available);
    }

    @Override
    public Pet findPetById(Long id) {
        return petCrudRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("[Pet: " + Long.toString(id) + "]")
        );
    }

    @Override
    public GetPetDTO savePet(Pet pet) {
        PetType petType = petTypeRepository.findById(pet.getPetType().getId())
                .orElseThrow(() -> new RuntimeException("Pet Type not found"));

        pet.setPetType(petType);
        Pet result = petCrudRepository.save(pet);
        return PetMapper.INSTANCE.fromEntityToDto(result);
    }

    @Override
    public Pet updatePetById(Long id, Pet pet) {
        Pet oldPet = this.findPetById(id);
        oldPet.setName(pet.getName());
        oldPet.setAge(pet.getAge());
        oldPet.setPetType(pet.getPetType());
        oldPet.setAvailable(pet.getAvailable());
        return petCrudRepository.save(oldPet);
    }

    @Override
    public void deletePetById(Long id) {
        Pet exist = this.findPetById(id);
        petCrudRepository.delete(exist);
    }
}
