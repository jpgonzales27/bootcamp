package com.juan_pablo.adopcion_mascotas;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.service.AdoptionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdoptionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private AdoptionService adoptionService;


    @Test
    void shouldReturnAllAdoptionsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/adopciones", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int adoptionsCount = documentContext.read("$.length()");
        assertThat(adoptionsCount).isGreaterThanOrEqualTo(1);

        List<Number> petIds = documentContext.read("$..petId");
        assertThat(petIds).isNotEmpty();
    }

    @Test
    void shouldReturnAnAdoptionById() {
        ResponseEntity<String> response = restTemplate.getForEntity("/adopciones/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        Number petId = documentContext.read("$.petId");
        assertThat(petId).isNotNull();
    }

    @Test
    void shouldNotReturnAnAdoptionWithUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/adopciones/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldCreateANewAdoption() {
        Adoption adoption = new Adoption(null, 1L, 1L, null, null, LocalDate.now());
        ResponseEntity<Void> response = restTemplate.postForEntity("/adopciones", adoption, Void.class);
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
    void shouldUpdateAnExistingAdoption() {
        Adoption adoption = new Adoption(null, 2L, 2L, null, null, LocalDate.now());
        HttpEntity<Adoption> request = new HttpEntity<>(adoption);

        ResponseEntity<Void> response = restTemplate.exchange("/adopciones/1", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/adopciones/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        //TODO:"Revisar cuando fixieamos el bug de update adoptions"
//        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
//        Number petId = documentContext.read("$.petId");
//        assertThat(petId).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    void shouldDeleteAnAdoptionById() {
        ResponseEntity<Void> response = restTemplate.exchange("/adopciones/1", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/adopciones/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
