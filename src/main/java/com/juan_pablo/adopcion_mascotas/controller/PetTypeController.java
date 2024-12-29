package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetTypeDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.service.PetTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/type")
@RequiredArgsConstructor
@Tag(name = "Pet Types")
@Slf4j
public class PetTypeController {

    private final PetTypeService petTypeService;

    @Operation(summary = "Get all pet types", description = "Retrieve a list of all pet types available in the system.")
    @GetMapping()
    public List<EntityModel<GetPetTypeDTO>> getPetType() {
        log.info("Fetching pet types");
        List<GetPetTypeDTO> petTypes = petTypeService.findAllPetTypes();
        log.info("Found {} pet types", petTypes.size());
        return petTypes.stream()
                .map(petType -> {
                    EntityModel<GetPetTypeDTO> petTypeModel = EntityModel.of(petType);
                    Link selfLink = linkTo(methodOn(PetTypeController.class).getTypeById(petType.getId())).withSelfRel();
                    petTypeModel.add(selfLink);
                    return petTypeModel;
                })
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get pet type by ID", description = "Retrieve details of a specific pet type by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PetType>> getTypeById(@PathVariable Long id){
        log.info("Fetching pet type with ID: {}", id);
        try {
            PetType petType = petTypeService.findPetTypeById(id);
            log.info("Successfully fetched pet type: {}", petType);
            EntityModel<PetType> petTypeModel = EntityModel.of(petType);
            Link selfLink = linkTo(methodOn(PetTypeController.class).getTypeById(id)).withSelfRel();
            Link allpetTypeLink = linkTo(methodOn(PetTypeController.class).getPetType()).withRel("petTypes");
            petTypeModel.add(selfLink, allpetTypeLink);

            return ResponseEntity.ok(petTypeModel);
        } catch (ObjectNotFoundException e) {
            log.error("Pet type with ID {} not found", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new pet type", description = "Add a new pet type to the system.")
    @PostMapping()
    public ResponseEntity<EntityModel<PetType>> createType(@Valid @RequestBody PetType petType, HttpServletRequest request) {
        log.info("Creating a new pet type: {}", petType);
        PetType petTypeCreated = petTypeService.savePetType(petType);
        log.info("Successfully created pet type with ID: {}", petTypeCreated.getId());
        EntityModel<PetType> PetTypeModel = EntityModel.of(petTypeCreated);
        Link selfLink = linkTo(methodOn(PetTypeController.class).getTypeById(petTypeCreated.getId())).withSelfRel();
        Link petTypeLink = linkTo(methodOn(PetTypeController.class).getPetType()).withRel("petTypes");
        PetTypeModel.add(selfLink, petTypeLink);
        return ResponseEntity.created(linkTo(methodOn(PetTypeController.class).getTypeById(petTypeCreated.getId())).toUri()).body(PetTypeModel);
    }

    @Operation(summary = "Update a pet type by ID", description = "Update the details of an existing pet type by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PetType>> updateType(@PathVariable Long id,@Valid  @RequestBody PetType petType) {
        log.info("Updating pet type with ID: {}", id);
        try {
            PetType petTypeUpdate = petTypeService.updatePetTypeById(id, petType);
            log.info("Successfully updated pet type with ID: {}", id);
            EntityModel<PetType> petTypeModel = EntityModel.of(petTypeUpdate);
            Link selfLink = linkTo(methodOn(PetTypeController.class).getTypeById(id)).withSelfRel();
            petTypeModel.add(selfLink);

            return ResponseEntity.ok(petTypeModel);
        } catch (ObjectNotFoundException e) {
            log.error("Pet type with ID {} not found for update", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a pet type by ID", description = "Remove a pet type from the system by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        log.info("Deleting pet type with ID: {}", id);
        try {
            petTypeService.deletePetTypeById(id);
            log.info("Successfully deleted pet type with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            log.error("Pet type with ID {} not found for deletion", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}
