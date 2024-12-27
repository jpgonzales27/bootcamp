package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.service.AdoptionService;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adopciones")
@Tag(name = "Adoptions")
public class AdoptionController {

    private final AdoptionService adoptionService;
    private final PagedResourcesAssembler<Adoption> pagedResourcesAssembler;

    @Operation(summary = "Get all adoptions", description = "Retrieve a paginated list of all adoptions recorded in the system.")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Adoption>>>  getAdoptions(
            @RequestParam(required = false) LocalDate exactDate,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable){
        Page<Adoption> adoptionsPage = adoptionService.findAllAdoptions(exactDate, startDate, endDate,pageable);
        PagedModel<EntityModel<Adoption>> pagedModel = pagedResourcesAssembler.toModel(adoptionsPage);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Get adoption by ID", description = "Retrieve details of an adoption by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Adoption>> getAdoption(@PathVariable Long id){
        try {
            Adoption adoption = adoptionService.findAdoptionById(id);
            EntityModel<Adoption> adoptionModel = EntityModel.of(adoption);
            Link selfLink = linkTo(methodOn(AdoptionController.class).getAdoption(id)).withSelfRel();
            adoptionModel.add(selfLink);

            return ResponseEntity.ok(adoptionModel);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new adoption", description = "Record a new adoption in the system.")
    @PostMapping()
    public ResponseEntity<EntityModel<Adoption>> createAdoption(@Valid @RequestBody Adoption adoption, HttpServletRequest request) {

        Adoption adoptionCreated = adoptionService.saveAdoption(adoption);
        EntityModel<Adoption> adoptionModel = EntityModel.of(adoptionCreated);
        Link selfLink = linkTo(methodOn(AdoptionController.class).getAdoption(adoptionCreated.getId())).withSelfRel();
        adoptionModel.add(selfLink);

        return ResponseEntity.created(linkTo(methodOn(AdoptionController.class).getAdoption(adoptionCreated.getId())).toUri()).body(adoptionModel);
    }

    @Operation(summary = "Update an adoption by ID", description = "Update the details of an existing adoption by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Adoption>>  updateAdoption(@PathVariable Long id,@Valid @RequestBody Adoption adoption) {
        try {
            Adoption adoptionUpdated = adoptionService.updateAdoptionById(id, adoption);
            EntityModel<Adoption> adoptionModel = EntityModel.of(adoptionUpdated);
            Link selfLink = linkTo(methodOn(AdoptionController.class).getAdoption(id)).withSelfRel();
            adoptionModel.add(selfLink);

            return ResponseEntity.ok(adoptionModel);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Delete an adoption by ID", description = "Remove an adoption from the system by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        try {
            adoptionService.deleteAdoptionById(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
