package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adopciones")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @GetMapping
    public List<Adoption> getAdoptions(){
        return adoptionService.findAllAdoptions();
    }

    @GetMapping("/{id}")
    public Adoption getAdoption(@PathVariable Long id){
        return adoptionService.findAdoptionById(id);
    }

    @PostMapping()
    public ResponseEntity<Adoption> createAdoption(@RequestBody Adoption adoption) {
        return ResponseEntity.ok(adoptionService.saveAdoption(adoption));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adoption> updateAdoption(@PathVariable Long id, @RequestBody Adoption adoption) {
        return ResponseEntity.ok(adoptionService.updateAdoptionById(id, adoption));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoptionById(id);
        return ResponseEntity.noContent().build();
    }
}
