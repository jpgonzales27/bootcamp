package com.juan_pablo.adopcion_mascotas.persistence.repository;

import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCrudRepository extends JpaRepository<User,Long> {
}
