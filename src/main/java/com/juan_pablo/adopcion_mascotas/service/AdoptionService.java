package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;

import java.util.List;

public interface AdoptionService {
    List<Adoption> findAllAdoptions();
    Adoption findAdoptionById(Long id );
    Adoption saveAdoption(Adoption adoption );
    Adoption updateAdoptionById( Long id, Adoption adoption );
    void deleteAdoptionById( Long id );
}
