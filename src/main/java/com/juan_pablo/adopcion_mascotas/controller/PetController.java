package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import com.juan_pablo.adopcion_mascotas.service.PetService;
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
public class PetController {

    @Autowired
    private PetService petService;

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

    @GetMapping("/pet/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(petService.findPetById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<GetPetDTO> createPet(@Valid @RequestBody Pet pet, HttpServletRequest request) {

        GetPetDTO petCreated = petService.savePet(pet);
        String baseUrl = request.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + petCreated.getId());

        return ResponseEntity.created(newLocation).body(petCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id,@Valid @RequestBody Pet pet) {
        try {
            return ResponseEntity.ok(petService.updatePetById(id, pet));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

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
