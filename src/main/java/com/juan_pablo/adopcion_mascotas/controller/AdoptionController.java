package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/adopciones")
@Tag(name = "Adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @Operation(summary = "Get all adoptions", description = "Retrieve a paginated list of all adoptions recorded in the system.")
    @GetMapping
    public ResponseEntity<Page<Adoption>> getAdoptions(
            @RequestParam(required = false) LocalDate exactDate,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable){
        Page<Adoption> adoptions = adoptionService.findAllAdoptions(exactDate, startDate, endDate,pageable);

        return ResponseEntity.ok(adoptions);
    }

    @Operation(summary = "Get adoption by ID", description = "Retrieve details of an adoption by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Adoption> getAdoption(@PathVariable Long id){
        try {
            return ResponseEntity.ok(adoptionService.findAdoptionById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new adoption", description = "Record a new adoption in the system.")
    @PostMapping()
    public ResponseEntity<Adoption> createAdoption(@Valid @RequestBody Adoption adoption, HttpServletRequest request) {

        Adoption adoptionCreated = adoptionService.saveAdoption(adoption);
        String baseUrl = request.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + adoptionCreated.getId());
        return ResponseEntity.created(newLocation).body(adoptionCreated);
    }

    @Operation(summary = "Update an adoption by ID", description = "Update the details of an existing adoption by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Adoption> updateAdoption(@PathVariable Long id,@Valid @RequestBody Adoption adoption) {
        try {
            return ResponseEntity.ok(adoptionService.updateAdoptionById(id, adoption));
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
