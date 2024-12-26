package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetTypeDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.service.PetTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/type")
@RequiredArgsConstructor
@Tag(name = "Pet Types")
public class PetTypeController {

    private final PetTypeService petTypeService;

    @Operation(summary = "Get all pet types", description = "Retrieve a list of all pet types available in the system.")
    @GetMapping()
    public List<GetPetTypeDTO> getPetType() {
        return petTypeService.findAllPetTypes();
    }

    @Operation(summary = "Get pet type by ID", description = "Retrieve details of a specific pet type by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<PetType> getTypeById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(petTypeService.findPetTypeById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new pet type", description = "Add a new pet type to the system.")
    @PostMapping()
    public ResponseEntity<PetType> createType(@Valid @RequestBody PetType petType, HttpServletRequest request) {
        PetType petTypeCreated = petTypeService.savePetType(petType);
        String baseUrl = request.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + petTypeCreated.getId());
        return ResponseEntity.created(newLocation).body(petTypeCreated);
    }

    @Operation(summary = "Update a pet type by ID", description = "Update the details of an existing pet type by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<PetType> updateType(@PathVariable Long id,@Valid  @RequestBody PetType petType) {
        try {
            return ResponseEntity.ok(petTypeService.updatePetTypeById(id, petType));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a pet type by ID", description = "Remove a pet type from the system by its unique ID.")
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
