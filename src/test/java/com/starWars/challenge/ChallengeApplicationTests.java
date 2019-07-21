package com.starWars.challenge;

import com.starWars.challenge.model.Planet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssertions.then;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"server.port=0"})
@AutoConfigureMockMvc
public class ChallengeApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturn200WhenSendingRequestToActuator() throws Exception {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
                "http://localhost:" + this.port + "/actuator/health", Map.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturn200WhenSendingHealthRequestToController() throws Exception {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
                "http://localhost:" + this.port + "/planets/health", Map.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnNewAddedPlanet() throws Exception {
        Planet newPlanet = new Planet(0, "Marte", "Warm", "Plain", new ArrayList<>());

        String baseUrl = "http://localhost:" + this.port + "/planets/add";

        baseUrl += "?name=" + newPlanet.getName();
        baseUrl += "&climate=" + newPlanet.getClimate();
        baseUrl += "&terrain=" + newPlanet.getTerrain();

        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Planet> request = new HttpEntity<>(newPlanet, headers);

        ResponseEntity<Planet> entity = this.testRestTemplate.postForEntity(uri, request, Planet.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(entity.getBody()).isEqualTo(newPlanet);
    }

    //TODO(test get operation)

//    @Test
//    public void givenEmployees_whenGetEmployees_thenReturnJsonArray()
//            throws Exception {
//
//        Planet newPlanet = new Planet(0, "Marte", "Warm", "Plain", new ArrayList<>());
//
//        List<Employee> allEmployees = Arrays.asList(alex);
//
//        given(service.getAllEmployees()).willReturn(allEmployees);
//
//        mvc.perform(get("/api/employees")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].name", is(alex.getName())));
//    }

}
