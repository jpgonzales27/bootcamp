package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();
    Role findRoleById(Long id );
    Role saveRole(Role role);
    Role updateRoleById( Long id, Role role );
    void deleteRoleById( Long id );
}
