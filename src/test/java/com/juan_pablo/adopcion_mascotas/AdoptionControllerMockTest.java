package com.juan_pablo.adopcion_mascotas;

import com.juan_pablo.adopcion_mascotas.controller.AdoptionController;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.service.AdoptionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AdoptionControllerMockTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private AdoptionService adoptionService;

    @Mock
    private PagedResourcesAssembler<Adoption> pagedResourcesAssembler;

    @InjectMocks
    private AdoptionController adoptionController;

    public AdoptionControllerMockTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAdoptions() {
        // Arrange
        Adoption adoption1 = Adoption.builder().id(1L).petId(100L).userId(200L).adoptionDate(LocalDate.now()).build();
        Adoption adoption2 = Adoption.builder().id(2L).petId(101L).userId(201L).adoptionDate(LocalDate.now()).build();
        Page<Adoption> adoptionPage = new PageImpl<>(List.of(adoption1, adoption2), PageRequest.of(0, 2), 2);
        when(adoptionService.findAllAdoptions(null, null, null, PageRequest.of(0, 2))).thenReturn(adoptionPage);
        when(pagedResourcesAssembler.toModel(adoptionPage)).thenReturn(PagedModel.empty());

        // Act
        ResponseEntity<PagedModel<EntityModel<Adoption>>> response = adoptionController.getAdoptions(null, null, null, PageRequest.of(0, 2));

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(adoptionService, times(1)).findAllAdoptions(null, null, null, PageRequest.of(0, 2));
    }

    @Test
    void testGetAdoptionById_Success() {
        // Arrange
        Long id = 1L;
        Adoption adoption = Adoption.builder().id(id).petId(100L).userId(200L).adoptionDate(LocalDate.now()).build();
        when(adoptionService.findAdoptionById(id)).thenReturn(adoption);

        // Act
        ResponseEntity<EntityModel<Adoption>> response = adoptionController.getAdoption(id);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100L, response.getBody().getContent().getPetId());
        verify(adoptionService, times(1)).findAdoptionById(id);
    }

    @Test
    void testGetAdoptionById_NotFound() {
        // Arrange
        Long id = 99L;
        when(adoptionService.findAdoptionById(id)).thenThrow(new ObjectNotFoundException("Adoption not found"));

        // Act
        ResponseEntity<EntityModel<Adoption>> response = adoptionController.getAdoption(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(adoptionService, times(1)).findAdoptionById(id);
    }

    @Test
    void testCreateAdoption() {
        // Arrange
        Adoption adoption = Adoption.builder().petId(100L).userId(200L).adoptionDate(LocalDate.now()).build();
        Adoption createdAdoption = Adoption.builder().id(1L).petId(100L).userId(200L).adoptionDate(LocalDate.now()).build();
        when(adoptionService.saveAdoption(adoption)).thenReturn(createdAdoption);

        // Act
        ResponseEntity<EntityModel<Adoption>> response = adoptionController.createAdoption(adoption, null);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getContent().getId());
        verify(adoptionService, times(1)).saveAdoption(adoption);
    }

    @Test
    void testUpdateAdoption_Success() {
        // Arrange
        Long id = 1L;
        Adoption adoption = Adoption.builder().id(id).petId(100L).userId(200L).adoptionDate(LocalDate.now()).build();
        when(adoptionService.updateAdoptionById(id, adoption)).thenReturn(adoption);

        // Act
        ResponseEntity<EntityModel<Adoption>> response = adoptionController.updateAdoption(id, adoption);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100L, response.getBody().getContent().getPetId());
        verify(adoptionService, times(1)).updateAdoptionById(id, adoption);
    }

    @Test
    void testUpdateAdoption_NotFound() {
        // Arrange
        Long id = 99L;
        Adoption adoption = Adoption.builder().id(id).petId(100L).userId(200L).adoptionDate(LocalDate.now()).build();
        when(adoptionService.updateAdoptionById(id, adoption)).thenThrow(new ObjectNotFoundException("Adoption not found"));

        // Act
        ResponseEntity<EntityModel<Adoption>> response = adoptionController.updateAdoption(id, adoption);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(adoptionService, times(1)).updateAdoptionById(id, adoption);
    }

    @Test
    void testDeleteAdoption_Success() {
        // Arrange
        Long id = 1L;
        doNothing().when(adoptionService).deleteAdoptionById(id);

        // Act
        ResponseEntity<Void> response = adoptionController.deleteAdoption(id);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(adoptionService, times(1)).deleteAdoptionById(id);
    }

    @Test
    void testDeleteAdoption_NotFound() {
        // Arrange
        Long id = 99L;
        doThrow(new ObjectNotFoundException("Adoption not found")).when(adoptionService).deleteAdoptionById(id);

        // Act
        ResponseEntity<Void> response = adoptionController.deleteAdoption(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(adoptionService, times(1)).deleteAdoptionById(id);
    }

}
