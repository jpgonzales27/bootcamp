package com.juan_pablo.adopcion_mascotas.controller;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users registered in the system.")
    @GetMapping()
    public ResponseEntity<Page<User>> getUsers(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String email,
                               Pageable userPageable) {
        Page<User> users = userService.findAllUsers(name,email,userPageable);

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve details of a user by their unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.findUserById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new user", description = "Add a new user to the system.")
    @PostMapping()
    public ResponseEntity<User> createUser(@Valid  @RequestBody User user, HttpServletRequest request) {
        User userCreated = userService.saveUser(user);
        String baseUrl = request.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + userCreated.getId());

        return ResponseEntity.created(newLocation).body(userCreated);
    }

    @Operation(summary = "Update a user by ID", description = "Update the details of an existing user by their unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,@Valid @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.updateUserById(id, user));
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
