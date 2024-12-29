package com.juan_pablo.adopcion_mascotas;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllPetsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/mascotas", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int petsCount = documentContext.read("$.length()");
        assertThat(petsCount).isGreaterThanOrEqualTo(1);

        List<String> names = documentContext.read("$..name");
        assertThat(names).isNotEmpty();
    }

    @Test
    void shouldReturnAPetById() {
        ResponseEntity<String> response = restTemplate.getForEntity("/mascotas/pet/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        String name = documentContext.read("$.name");
        assertThat(name).isNotBlank();
    }

    @Test
    void shouldNotReturnAPetWithUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/mascotas/pet/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldCreateANewPet() {
        PetType petType = new PetType(1L, null); // Solo se necesita el ID de un tipo existente
        Pet pet = new Pet(null, "Firulais", petType, 3, Genre.MALE, true, null);
        ResponseEntity<Void> response = restTemplate.postForEntity("/mascotas", pet, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location = response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String name = documentContext.read("$.name");

        assertThat(id).isNotNull();
        assertThat(name).isEqualTo("Firulais");
    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingPet() {
        PetType petType = new PetType(2L,null);
        Pet pet = new Pet(null, "Updated Firulais", petType,3, Genre.MALE, true, null);
        HttpEntity<Pet> request = new HttpEntity<>(pet);

        ResponseEntity<Void> response = restTemplate.exchange("/mascotas/1", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/mascotas/pet/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("Updated Firulais");
    }

    @Test
    @DirtiesContext
    void shouldDeleteAPetById() {
        ResponseEntity<Void> response = restTemplate.exchange("/mascotas/1", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/mascotas/pet/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
