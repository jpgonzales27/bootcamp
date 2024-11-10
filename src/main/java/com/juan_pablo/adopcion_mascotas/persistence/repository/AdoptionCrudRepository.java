package com.juan_pablo.adopcion_mascotas.persistence.repository;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionCrudRepository extends JpaRepository<Adoption,Long> {
}
