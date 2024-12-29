package com.juan_pablo.adopcion_mascotas;

import com.juan_pablo.adopcion_mascotas.controller.UserController;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerMockTest {
    @Mock
    private UserService userService;

    @Mock
    private PagedResourcesAssembler<User> pagedResourcesAssembler;

    @InjectMocks
    private UserController userController;

    public UserControllerMockTest() {
        MockitoAnnotations.openMocks(this);
    }

    List<Adoption> adoptionsList = new ArrayList<>();
    @Test
    void testGetUsers() {
        // Arrange

        User user1 = new User(1L, "John Doe", "john.doe@example.com", "password123",adoptionsList);
        User user2 = new User(2L, "Jane Smith", "jane.smith@example.com", "password456",adoptionsList);
        Page<User> usersPage = new PageImpl<>(List.of(user1, user2), PageRequest.of(0, 2), 2);
        when(userService.findAllUsers(null, null, PageRequest.of(0, 2))).thenReturn(usersPage);
        when(pagedResourcesAssembler.toModel(usersPage)).thenReturn(PagedModel.empty());

        // Act
        ResponseEntity<PagedModel<EntityModel<User>>> response = userController.getUsers(null, null, PageRequest.of(0, 2));

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).findAllUsers(null, null, PageRequest.of(0, 2));
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        Long id = 1L;
        User user = new User(id, "John Doe", "john.doe@example.com", "password123",adoptionsList);
        when(userService.findUserById(id)).thenReturn(user);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.getUserById(id);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getContent().getName());
        verify(userService, times(1)).findUserById(id);
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        Long id = 99L;
        when(userService.findUserById(id)).thenThrow(new ObjectNotFoundException("User not found"));

        // Act
        ResponseEntity<EntityModel<User>> response = userController.getUserById(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).findUserById(id);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User user = new User(null, "John Doe", "john.doe@example.com", "password123",adoptionsList);
        User createdUser = new User(1L, "John Doe", "john.doe@example.com", "password123",adoptionsList);
        when(userService.saveUser(user)).thenReturn(createdUser);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.createUser(user, null);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getContent().getId());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        Long id = 1L;
        User user = new User(id, "Updated Name", "updated.email@example.com", "newpassword123",adoptionsList);
        when(userService.updateUserById(id, user)).thenReturn(user);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.updateUser(id, user);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Name", response.getBody().getContent().getName());
        verify(userService, times(1)).updateUserById(id, user);
    }

    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        Long id = 99L;
        User user = new User(id, "Updated Name", "updated.email@example.com", "newpassword123",adoptionsList);
        when(userService.updateUserById(id, user)).thenThrow(new ObjectNotFoundException("User not found"));

        // Act
        ResponseEntity<EntityModel<User>> response = userController.updateUser(id, user);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).updateUserById(id, user);
    }

    @Test
    void testDeleteUser_Success() {
        // Arrange
        Long id = 1L;
        doNothing().when(userService).deleteUserById(id);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(id);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUserById(id);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        Long id = 99L;
        doThrow(new ObjectNotFoundException("User not found")).when(userService).deleteUserById(id);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUserById(id);
    }
}
