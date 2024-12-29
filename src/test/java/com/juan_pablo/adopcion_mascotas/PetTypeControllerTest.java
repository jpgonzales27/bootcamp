package com.juan_pablo.adopcion_mascotas;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetTypeControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllPetTypesWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/type", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int typesCount = documentContext.read("$.length()");
        assertThat(typesCount).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldReturnAPetTypeById() {
        ResponseEntity<String> response = restTemplate.getForEntity("/type/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        String typeName = documentContext.read("$.typeName");
        assertThat(typeName).isNotBlank();
    }

    @Test
    @DirtiesContext
    void shouldCreateANewPetType() {
        PetType petType = new PetType(null, "Hamster");
        ResponseEntity<Void> response = restTemplate.postForEntity("/type", petType, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location = response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isNotNull();
    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingPetType() {
        PetType updatedPetType = new PetType(null, "Updated Hamster");
        HttpEntity<PetType> request = new HttpEntity<>(updatedPetType);

        ResponseEntity<Void> response = restTemplate.exchange("/type/1", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/type/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        String typeName = documentContext.read("$.typeName");
        assertThat(typeName).isEqualTo("Updated Hamster");
    }

    @Test
    @DirtiesContext
    void shouldDeleteAPetTypeById() {
        ResponseEntity<Void> response = restTemplate.exchange("/type/5", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/type/5", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
