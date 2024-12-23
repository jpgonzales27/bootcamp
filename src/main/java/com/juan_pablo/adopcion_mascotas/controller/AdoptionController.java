package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.service.AdoptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/adopciones")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @GetMapping
    public ResponseEntity<Page<Adoption>> getAdoptions(
            @RequestParam(required = false) LocalDate exactDate,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable){
        Page<Adoption> adoptions = adoptionService.findAllAdoptions(exactDate, startDate, endDate,pageable);

        return ResponseEntity.ok(adoptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adoption> getAdoption(@PathVariable Long id){
        try {
            return ResponseEntity.ok(adoptionService.findAdoptionById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<Adoption> createAdoption(@Valid @RequestBody Adoption adoption, HttpServletRequest request) {

        Adoption adoptionCreated = adoptionService.saveAdoption(adoption);
        String baseUrl = request.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + adoptionCreated.getId());
        return ResponseEntity.created(newLocation).body(adoptionCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adoption> updateAdoption(@PathVariable Long id,@Valid @RequestBody Adoption adoption) {
        try {
            return ResponseEntity.ok(adoptionService.updateAdoptionById(id, adoption));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

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
