package com.juan_pablo.adopcion_mascotas;

import com.juan_pablo.adopcion_mascotas.controller.PetTypeController;
import com.juan_pablo.adopcion_mascotas.dto.response.GetPetTypeDTO;
import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.service.PetTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PetTypeControllerMockTest {


    @Mock
    private PetTypeService petTypeService;

    @InjectMocks
    private PetTypeController petTypeController;

    public PetTypeControllerMockTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPetType() {
        // Arrange
        GetPetTypeDTO petType1 = new GetPetTypeDTO(1L, "Dog");
        GetPetTypeDTO petType2 = new GetPetTypeDTO(2L, "Cat");
        when(petTypeService.findAllPetTypes()).thenReturn(Arrays.asList(petType1, petType2));

        // Act
        List<EntityModel<GetPetTypeDTO>> response = petTypeController.getPetType();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        verify(petTypeService, times(1)).findAllPetTypes();
    }
    @Test
    void testGetTypeById_Success() {
        // Arrange
        Long id = 1L;
        PetType petType = new PetType(id, "Dog");
        when(petTypeService.findPetTypeById(id)).thenReturn(petType);

        // Act
        ResponseEntity<EntityModel<PetType>> response = petTypeController.getTypeById(id);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dog", response.getBody().getContent().getTypeName());
        verify(petTypeService, times(1)).findPetTypeById(id);
    }

    @Test
    void testGetTypeById_NotFound() {
        // Arrange
        Long id = 99L;
        when(petTypeService.findPetTypeById(id)).thenThrow(new ObjectNotFoundException("Pet type not found"));

        // Act
        ResponseEntity<EntityModel<PetType>> response = petTypeController.getTypeById(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(petTypeService, times(1)).findPetTypeById(id);
    }

    @Test
    void testCreateType() {
        // Arrange
        PetType petType = new PetType(null, "Dog");
        PetType savedPetType = new PetType(1L, "Dog");
        when(petTypeService.savePetType(petType)).thenReturn(savedPetType);

        // Act
        ResponseEntity<EntityModel<PetType>> response = petTypeController.createType(petType, null);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getContent().getId());
        verify(petTypeService, times(1)).savePetType(petType);
    }

    @Test
    void testUpdateType_Success() {
        // Arrange
        Long id = 1L;
        PetType petType = new PetType(id, "Updated Dog");
        when(petTypeService.updatePetTypeById(id, petType)).thenReturn(petType);

        // Act
        ResponseEntity<EntityModel<PetType>> response = petTypeController.updateType(id, petType);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Dog", response.getBody().getContent().getTypeName());
        verify(petTypeService, times(1)).updatePetTypeById(id, petType);
    }

    @Test
    void testUpdateType_NotFound() {
        // Arrange
        Long id = 99L;
        PetType petType = new PetType(id, "Dog");
        when(petTypeService.updatePetTypeById(id, petType)).thenThrow(new ObjectNotFoundException("Pet type not found"));

        // Act
        ResponseEntity<EntityModel<PetType>> response = petTypeController.updateType(id, petType);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(petTypeService, times(1)).updatePetTypeById(id, petType);
    }

    @Test
    void testDeleteType_Success() {
        // Arrange
        Long id = 1L;
        doNothing().when(petTypeService).deletePetTypeById(id);

        // Act
        ResponseEntity<Void> response = petTypeController.deleteType(id);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(petTypeService, times(1)).deletePetTypeById(id);
    }

    @Test
    void testDeleteType_NotFound() {
        // Arrange
        Long id = 99L;
        doThrow(new ObjectNotFoundException("Pet type not found")).when(petTypeService).deletePetTypeById(id);

        // Act
        ResponseEntity<Void> response = petTypeController.deleteType(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(petTypeService, times(1)).deletePetTypeById(id);
    }
}
