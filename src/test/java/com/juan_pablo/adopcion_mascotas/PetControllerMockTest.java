package com.juan_pablo.adopcion_mascotas;

import com.juan_pablo.adopcion_mascotas.controller.PetController;
import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import com.juan_pablo.adopcion_mascotas.service.PetService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PetControllerMockTest {
    @Mock
    private PetService petService;

    @Mock
    private PagedResourcesAssembler<Pet> pagedResourcesAssembler;

    @InjectMocks
    private PetController petController;

    public PetControllerMockTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPets() {
        // Arrange
        Pet pet1 = Pet.builder().id(1L).name("Cachito").age(2).available(true).genre(Genre.MALE).build();
        Pet pet2 = Pet.builder().id(2L).name("Mila").age(3).available(false).genre(Genre.FEMALE).build();
        Page<Pet> petsPage = new PageImpl<>(List.of(pet1, pet2), PageRequest.of(0, 2), 2);
        when(petService.findAllPets(null, null, null, null, null, null, null, PageRequest.of(0, 2))).thenReturn(petsPage);
        when(pagedResourcesAssembler.toModel(petsPage)).thenReturn(PagedModel.empty());

        // Act
        ResponseEntity<PagedModel<EntityModel<Pet>>> response = petController.getPets(null, null, null, null, null, null, null, PageRequest.of(0, 2));

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(petService, times(1)).findAllPets(null, null, null, null, null, null, null, PageRequest.of(0, 2));
    }

    @Test
    void testGetPetById_Success() {
        // Arrange
        Long id = 1L;
        Pet pet = Pet.builder().id(id).name("Cachito").age(2).available(true).genre(Genre.MALE).build();
        when(petService.findPetById(id)).thenReturn(pet);

        // Act
        ResponseEntity<EntityModel<Pet>> response = petController.getPetById(id);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cachito", response.getBody().getContent().getName());
        verify(petService, times(1)).findPetById(id);
    }

    @Test
    void testGetPetById_NotFound() {
        // Arrange
        Long id = 99L;
        when(petService.findPetById(id)).thenThrow(new ObjectNotFoundException("Pet not found"));

        // Act
        ResponseEntity<EntityModel<Pet>> response = petController.getPetById(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(petService, times(1)).findPetById(id);
    }

    @Test
    void testCreatePet() {
        // Arrange
        Pet pet = Pet.builder().name("Cachito").age(2).available(true).genre(Genre.MALE).build();
        GetPetDTO createdPet = GetPetDTO.builder().id(1L).name("Cachito").age(2).available(true).genre(Genre.MALE).build();
        when(petService.savePet(pet)).thenReturn(createdPet);

        // Act
        ResponseEntity<EntityModel<GetPetDTO>> response = petController.createPet(pet, null);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getContent().getId());
        verify(petService, times(1)).savePet(pet);
    }

    @Test
    void testUpdatePet_Success() {
        // Arrange
        Long id = 1L;
        Pet pet = Pet.builder().id(id).name("Updated Cachito").age(3).available(false).genre(Genre.MALE).build();
        when(petService.updatePetById(id, pet)).thenReturn(pet);

        // Act
        ResponseEntity<EntityModel<Pet>> response = petController.updatePet(id, pet);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Cachito", response.getBody().getContent().getName());
        verify(petService, times(1)).updatePetById(id, pet);
    }

    @Test
    void testUpdatePet_NotFound() {
        // Arrange
        Long id = 99L;
        Pet pet = Pet.builder().id(id).name("Updated Cachito").age(3).available(false).genre(Genre.MALE).build();
        when(petService.updatePetById(id, pet)).thenThrow(new ObjectNotFoundException("Pet not found"));

        // Act
        ResponseEntity<EntityModel<Pet>> response = petController.updatePet(id, pet);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(petService, times(1)).updatePetById(id, pet);
    }

    @Test
    void testDeletePet_Success() {
        // Arrange
        Long id = 1L;
        doNothing().when(petService).deletePetById(id);

        // Act
        ResponseEntity<Void> response = petController.deletePet(id);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(petService, times(1)).deletePetById(id);
    }

    @Test
    void testDeletePet_NotFound() {
        // Arrange
        Long id = 99L;
        doThrow(new ObjectNotFoundException("Pet not found")).when(petService).deletePetById(id);

        // Act
        ResponseEntity<Void> response = petController.deletePet(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(petService, times(1)).deletePetById(id);
    }
}
