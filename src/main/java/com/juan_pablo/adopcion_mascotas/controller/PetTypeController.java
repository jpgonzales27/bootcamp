package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class PetTypeController {

    @Autowired
    private PetTypeService petTypeService;

    @GetMapping()
    public List<PetType> getUsers() {
        return petTypeService.findAllPetTypes();
    }

    @GetMapping("/{id}")
    public PetType getUserById(@PathVariable Long id){
        return petTypeService.findPetTypeById(id);
    }

    @PostMapping()
    public ResponseEntity<PetType> createUser(@RequestBody PetType petType) {
        return ResponseEntity.ok(petTypeService.savePetType(petType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetType> updateUser(@PathVariable Long id, @RequestBody PetType petType) {
        return ResponseEntity.ok(petTypeService.updatePetTypeById(id, petType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        petTypeService.deletePetTypeById(id);
        return ResponseEntity.noContent().build();
    }
}
