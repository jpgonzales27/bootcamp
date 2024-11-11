package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
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
    public ResponseEntity<PetType> getUserById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(petTypeService.findPetTypeById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<PetType> createUser(@RequestBody PetType petType) {
        return ResponseEntity.ok(petTypeService.savePetType(petType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetType> updateUser(@PathVariable Long id, @RequestBody PetType petType) {
        try {
            return ResponseEntity.ok(petTypeService.updatePetTypeById(id, petType));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            petTypeService.deletePetTypeById(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
