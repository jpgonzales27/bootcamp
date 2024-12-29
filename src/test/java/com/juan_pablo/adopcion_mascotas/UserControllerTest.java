package com.juan_pablo.adopcion_mascotas;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    List<Adoption> adoptionsList = new ArrayList<>();

    @Test
    void shouldReturnAllUsersWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/usuarios", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int usersCount = documentContext.read("$.length()");
        assertThat(usersCount).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldReturnAUserById() {
        ResponseEntity<String> response = restTemplate.getForEntity("/usuarios/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        String name = documentContext.read("$.name");
        assertThat(name).isNotBlank();
    }

    @Test
    void shouldNotReturnAUserWithUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/usuarios/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldCreateANewUser() {
        User user = new User(null, "John Doe", "john.doe@example.com", "1234556",adoptionsList);
        ResponseEntity<Void> response = restTemplate.postForEntity("/usuarios", user, Void.class);
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
    void shouldDeleteAUserById() {
        ResponseEntity<Void> response = restTemplate.exchange("/usuarios/3", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/usuarios/3", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
