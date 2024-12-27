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
public class PetTypeController {

    private final PetTypeService petTypeService;

    @Operation(summary = "Get all pet types", description = "Retrieve a list of all pet types available in the system.")
    @GetMapping()
    public List<EntityModel<GetPetTypeDTO>> getPetType() {
        List<GetPetTypeDTO> petTypes = petTypeService.findAllPetTypes();
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
        try {
            PetType petType = petTypeService.findPetTypeById(id);
            EntityModel<PetType> petTypeModel = EntityModel.of(petType);
            Link selfLink = linkTo(methodOn(PetTypeController.class).getTypeById(id)).withSelfRel();
            Link allpetTypeLink = linkTo(methodOn(PetTypeController.class).getPetType()).withRel("petTypes");
            petTypeModel.add(selfLink, allpetTypeLink);

            return ResponseEntity.ok(petTypeModel);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new pet type", description = "Add a new pet type to the system.")
    @PostMapping()
    public ResponseEntity<EntityModel<PetType>> createType(@Valid @RequestBody PetType petType, HttpServletRequest request) {
        PetType petTypeCreated = petTypeService.savePetType(petType);

        EntityModel<PetType> PetTypeModel = EntityModel.of(petTypeCreated);
        Link selfLink = linkTo(methodOn(PetTypeController.class).getTypeById(petTypeCreated.getId())).withSelfRel();
        Link petTypeLink = linkTo(methodOn(PetTypeController.class).getPetType()).withRel("petTypes");
        PetTypeModel.add(selfLink, petTypeLink);

        return ResponseEntity.ok(PetTypeModel);
    }

    @Operation(summary = "Update a pet type by ID", description = "Update the details of an existing pet type by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PetType>> updateType(@PathVariable Long id,@Valid  @RequestBody PetType petType) {
        try {
            PetType petTypeUpdate = petTypeService.updatePetTypeById(id, petType);
            EntityModel<PetType> petTypeModel = EntityModel.of(petTypeUpdate);
            Link selfLink = linkTo(methodOn(PetTypeController.class).getTypeById(id)).withSelfRel();
            petTypeModel.add(selfLink);

            return ResponseEntity.ok(petTypeModel);
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
