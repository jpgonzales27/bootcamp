package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface AdoptionService {
    Page<Adoption> findAllAdoptions(LocalDate exactDate, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Adoption findAdoptionById(Long id );
    Adoption saveAdoption(Adoption adoption );
    Adoption updateAdoptionById( Long id, Adoption adoption );
    void deleteAdoptionById( Long id );
}
