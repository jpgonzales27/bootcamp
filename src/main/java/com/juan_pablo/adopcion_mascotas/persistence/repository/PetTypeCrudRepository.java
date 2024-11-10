package com.juan_pablo.adopcion_mascotas.persistence.repository;

import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTypeCrudRepository extends JpaRepository<PetType,Long> {
}
