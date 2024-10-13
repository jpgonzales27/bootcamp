package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping()
    public List<Pet> getPets(){
        return petService.findAllPets();
    }

    @GetMapping("/pet/{id}")
    public Pet getPetByName(@PathVariable Long id){
        return petService.findPetById(id);
    }

    @GetMapping("/{name}")
    public List<Pet> getPetByName(@PathVariable String name){
        return petService.findAllByName(name);
    }

    @GetMapping("/type/{petType}")
    public List<Pet> getPetByPetType(@PathVariable String petType){
        return petService.findAllByPetTypeTypeName(petType);
    }

    @GetMapping("/available/{available}")
    public List<Pet> getPetByPetType(@PathVariable Boolean available){
        return petService.findByAvailability(available);
    }

    @PostMapping()
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet){
        return ResponseEntity.ok(petService.savePet(pet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet pet){
        return ResponseEntity.ok(petService.updatePetById(id,pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id){
        petService.deletePetById(id);
        return ResponseEntity.noContent().build();
    }
}
