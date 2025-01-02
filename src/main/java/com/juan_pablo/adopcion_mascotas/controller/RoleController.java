package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Role;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Role;
import com.juan_pablo.adopcion_mascotas.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Roles")
@Slf4j
public class RoleController {

    private final RoleService roleService;
    
    @Operation(summary = "Get all Roles", description = "Retrieve a list of all roles available in the system.")
    @GetMapping
    public List<EntityModel<Role>> getAllRoles() {
        log.info("Fetching roles");
        List<Role> roles = roleService.findAllRoles();
        log.info("Found {} roles", roles.size());
        return roles.stream().map(role -> {
            EntityModel<Role> roleModel = EntityModel.of(role);
            Link selfLink = linkTo(methodOn(RoleController.class).getAllRoles()).withSelfRel();
            roleModel.add(selfLink);
            return roleModel;
        }).collect(Collectors.toList());
    }

    @Operation(summary = "Get role by ID", description = "Retrieve details of a specific role by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Role>> getRoleById(@PathVariable Long id){
        log.info("Fetching role with ID: {}", id);
        Role role = roleService.findRoleById(id);
        log.info("Successfully fetched role: {}", role);
        EntityModel<Role> roleModel = EntityModel.of(role);
        Link selfLink = linkTo(methodOn(RoleController.class).getRoleById(id)).withSelfRel();
        Link allroleLink = linkTo(methodOn(RoleController.class).getAllRoles()).withRel("roles");
        roleModel.add(selfLink, allroleLink);

        return ResponseEntity.ok(roleModel);
    }

    @Operation(summary = "Create a new role", description = "Add a new role to the system.")
    @PostMapping
    public ResponseEntity<EntityModel<Role>> createRole(@Valid @RequestBody Role role) {
        log.info("Creating a new role: {}", role);
        Role roleCreated = roleService.saveRole(role);
        log.info("Successfully created role with ID: {}", roleCreated.getId());
        EntityModel<Role> roleModel = EntityModel.of(roleCreated);
        Link selfLink = linkTo(methodOn(RoleController.class).getRoleById(roleCreated.getId())).withSelfRel();
        Link roleLink = linkTo(methodOn(RoleController.class).getAllRoles()).withRel("roles");
        roleModel.add(selfLink, roleLink);
        return ResponseEntity.created(linkTo(methodOn(RoleController.class).getRoleById(roleCreated.getId())).toUri()).body(roleModel);
    }

    @Operation(summary = "Update a role by ID", description = "Update the details of an existing role by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Role>> updateType(@PathVariable Long id,@Valid  @RequestBody Role role) {
        log.info("Updating role with ID: {}", id);
        Role roleUpdate = roleService.updateRoleById(id, role);
        log.info("Successfully updated role with ID: {}", id);
        EntityModel<Role> roleModel = EntityModel.of(roleUpdate);
        Link selfLink = linkTo(methodOn(RoleController.class).getRoleById(id)).withSelfRel();
        roleModel.add(selfLink);

        return ResponseEntity.ok(roleModel);
    }

    @Operation(summary = "Delete a role by ID", description = "Remove a role from the system by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        log.info("Deleting role with ID: {}", id);
        roleService.deleteRoleById(id);
        log.info("Successfully deleted role with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
