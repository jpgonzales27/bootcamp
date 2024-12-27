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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mascotas")
@Tag(name = "Pets")
public class PetController {

    private final PetService petService;
    private final PagedResourcesAssembler<Pet> pagedResourcesAssembler;

    @Operation(summary = "Get all pets", description = "Retrieve a paginated list of all pets available in the system.")
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Pet>>> getPets(
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
        PagedModel<EntityModel<Pet>> pagedModel = pagedResourcesAssembler.toModel(pets);

        return ResponseEntity.ok(pagedModel);
    }
    @Operation(summary = "Get pet by ID", description = "Retrieve details of a pet by its unique ID.")
    @GetMapping("/pet/{id}")
    public ResponseEntity<EntityModel<Pet>> getPetById(@PathVariable Long id) {
        try {
            Pet pet = petService.findPetById(id);
            EntityModel<Pet> petModel = EntityModel.of(pet);
            Link selfLink = linkTo(methodOn(PetController.class).getPetById(id)).withSelfRel();
            petModel.add(selfLink);

            return ResponseEntity.ok(petModel);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new pet", description = "Add a new pet to the system.")
    @PostMapping()
    public ResponseEntity<EntityModel<GetPetDTO>> createPet(@Valid @RequestBody Pet pet, HttpServletRequest request) {
        GetPetDTO petCreated = petService.savePet(pet);
        EntityModel<GetPetDTO> petModel = EntityModel.of(petCreated);
        Link selfLink = linkTo(methodOn(UserController.class).getUserById(petCreated.getId())).withSelfRel();
        petModel.add(selfLink);

        return ResponseEntity.created(linkTo(methodOn(UserController.class).getUserById(petCreated.getId())).toUri()).body(petModel);
    }

    @Operation(summary = "Update a pet by ID", description = "Update the details of an existing pet by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pet>> updatePet(@PathVariable Long id,@Valid @RequestBody Pet pet) {
        try {
            Pet petUpdated = petService.updatePetById(id, pet);
            EntityModel<Pet> petModel = EntityModel.of(petUpdated);
            Link selfLink = linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel();
            petModel.add(selfLink);

            return ResponseEntity.ok(petModel);
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
