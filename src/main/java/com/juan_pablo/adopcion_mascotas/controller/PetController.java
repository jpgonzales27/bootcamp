package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import com.juan_pablo.adopcion_mascotas.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mascotas")
@Tag(name = "Pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Operation(summary = "Get all pets", description = "Retrieve a paginated list of all pets available in the system.")
    @GetMapping()
    public ResponseEntity<Page<Pet>> getPets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Boolean available,
            Pageable petPageable
    ) {

        Page<Pet> pets = petService.findAllPets(name,typeId,typeName,minAge,maxAge,genre,available,petPageable);

        return ResponseEntity.ok(pets);
    }
    @Operation(summary = "Get pet by ID", description = "Retrieve details of a pet by its unique ID.")
    @GetMapping("/pet/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(petService.findPetById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new pet", description = "Add a new pet to the system.")
    @PostMapping()
    public ResponseEntity<GetPetDTO> createPet(@Valid @RequestBody Pet pet, HttpServletRequest request) {

        GetPetDTO petCreated = petService.savePet(pet);
        String baseUrl = request.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + petCreated.getId());

        return ResponseEntity.created(newLocation).body(petCreated);
    }

    @Operation(summary = "Update a pet by ID", description = "Update the details of an existing pet by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id,@Valid @RequestBody Pet pet) {
        try {
            return ResponseEntity.ok(petService.updatePetById(id, pet));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a pet by ID", description = "Remove a pet from the system by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        try {
            petService.deletePetById(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
