package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Role;
import com.juan_pablo.adopcion_mascotas.persistence.repository.RoleCrudRepository;
import com.juan_pablo.adopcion_mascotas.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleCrudRepository roleCrudRepository;
    @Override
    public List<Role> findAllRoles() {
        return roleCrudRepository.findAll();
    }

    @Override
    public Role findRoleById(Long id) {
        return roleCrudRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("[Role: " + Long.toString(id) + "]")
        );
    }

    @Override
    public Role saveRole(Role role) {
        return roleCrudRepository.save(role);
    }

    @Override
    public Role updateRoleById(Long id, Role role) {
        Role oldRole = this.findRoleById(id);
        oldRole.setName(role.getName());
        return roleCrudRepository.save(oldRole);
    }

    @Override
    public void deleteRoleById(Long id) {
        Role exits = this.findRoleById(id);
        roleCrudRepository.delete(exits);
    }
}
