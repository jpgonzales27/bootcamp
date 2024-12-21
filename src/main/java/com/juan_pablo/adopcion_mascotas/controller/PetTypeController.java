package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetTypeDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.service.PetTypeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/type")
@RequiredArgsConstructor
public class PetTypeController {

    private final PetTypeService petTypeService;

    @GetMapping()
    public List<GetPetTypeDTO> getPetType() {
        return petTypeService.findAllPetTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetType> getTypeById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(petTypeService.findPetTypeById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<PetType> createType(@RequestBody PetType petType, HttpServletRequest request) {
        PetType petTypeCreated = petTypeService.savePetType(petType);
        String baseUrl = request.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + petTypeCreated.getId());
        return ResponseEntity.created(newLocation).body(petTypeCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetType> updateType(@PathVariable Long id, @RequestBody PetType petType) {
        try {
            return ResponseEntity.ok(petTypeService.updatePetTypeById(id, petType));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        try {
            petTypeService.deletePetTypeById(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
