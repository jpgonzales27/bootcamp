package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;

    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users registered in the system.")
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<User>>> getUsers(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String email,
                               Pageable userPageable) {

        Page<User> usersPage = userService.findAllUsers(name,email,userPageable);
        PagedModel<EntityModel<User>> pagedModel = pagedResourcesAssembler.toModel(usersPage);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve details of a user by their unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUserById(@PathVariable Long id){
        try {
            User user = userService.findUserById(id);
            EntityModel<User> userModel = EntityModel.of(user);
            Link selfLink = linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel();
            userModel.add(selfLink);

            return ResponseEntity.ok(userModel);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new user", description = "Add a new user to the system.")
    @PostMapping()
    public ResponseEntity<EntityModel<User>> createUser(@Valid  @RequestBody User user, HttpServletRequest request) {
        User userCreated = userService.saveUser(user);
        EntityModel<User> userModel = EntityModel.of(userCreated);
        Link selfLink = linkTo(methodOn(UserController.class).getUserById(userCreated.getId())).withSelfRel();
        userModel.add(selfLink);

        return ResponseEntity.created(linkTo(methodOn(UserController.class).getUserById(userCreated.getId())).toUri()).body(userModel);
    }

    @Operation(summary = "Update a user by ID", description = "Update the details of an existing user by their unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>>  updateUser(@PathVariable Long id,@Valid @RequestBody User user) {
        try {
            User userUpdated = userService.updateUserById(id, user);
            EntityModel<User> userModel = EntityModel.of(userUpdated);
            Link selfLink = linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel();
            userModel.add(selfLink);

            return ResponseEntity.ok(userModel);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a user by ID", description = "Remove a user from the system by their unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
