package com.juan_pablo.adopcion_mascotas.persistence.repository;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleCrudRepository extends JpaRepository<Role,Long> {
}
