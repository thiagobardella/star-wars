package com.starWars.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.starWars.challenge.model.DefaultAPIResponse;
import com.starWars.challenge.model.Planet;
import com.starWars.challenge.model.PlanetInput;
import com.starWars.challenge.repository.PlanetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"server.port=0"})
@AutoConfigureMockMvc
public class ChallengeApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @MockBean
    PlanetRepository planetRepository;

    @Test
    public void shouldReturn200WhenSendingHealthRequestToController() throws Exception {
        DefaultAPIResponse expected = new DefaultAPIResponse(200, "UP");

        mvc.perform(get("http://localhost:" + this.port + "/api/v1/planets/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo(expected.getMessage())))
                .andExpect(jsonPath("$.status", equalTo(expected.getStatus())));
    }

    @Test
    public void shouldAddPlanet() throws Exception {
        PlanetInput planetInput = new PlanetInput("batata", "Warm", "Plain", 0);

        given(planetRepository.findByName(planetInput.getName())).willReturn(null);
        given(planetRepository.save(new Planet(planetInput))).willReturn(new Planet(planetInput));

        mvc.perform(post("http://localhost:" + this.port + "/api/v1/planets/add")
                .content(asJsonString(planetInput))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(planetInput.getName())))
                .andExpect(jsonPath("$.climate", equalTo(planetInput.getClimate())))
                .andExpect(jsonPath("$.terrain", equalTo(planetInput.getTerrain())));
    }

    @Test
    public void shouldFailAddingDuplicatePlanet() throws Exception {
        PlanetInput planetInput = new PlanetInput("Marte", "Warm", "Plain", 0);
        Planet planet = new Planet(planetInput);
        given(planetRepository.findByName(planet.getName())).willReturn(planet);

        mvc.perform(post("http://localhost:" + this.port + "/api/v1/planets/add")
                .content(asJsonString(planetInput))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldGetAllPlanetsFromDB() throws Exception {
        Planet planet = new Planet(0, "Marte", "Warm", "Plain", 0);

        given(planetRepository.findAll()).willReturn(singletonList(planet));

        mvc.perform(get("http://localhost:" + this.port + "/api/v1/planets/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", equalTo(planet.getName())))
                .andExpect(jsonPath("$[0].terrain", equalTo(planet.getTerrain())));
    }

    @Test
    public void shouldGetPlanetByName() throws Exception {
        Planet planet = new Planet(0, "Marte", "Warm", "Plain", 0);

        given(planetRepository.findByName(planet.getName())).willReturn(planet);

        mvc.perform(get("http://localhost:" + this.port + "/api/v1/planets/?name=" + planet.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", equalTo(planet.getName())))
                .andExpect(jsonPath("$[0].terrain", equalTo(planet.getTerrain())));
    }

    @Test
    public void shouldGetPlanetById() throws Exception {
        Planet planet = new Planet(0, "Marte", "Warm", "Plain", 0);

        given(planetRepository.findById(planet.getId())).willReturn(planet);

        mvc.perform(get("http://localhost:" + this.port + "/api/v1/planets/" + planet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(planet.getName())))
                .andExpect(jsonPath("$.id", equalTo(planet.getId())));
    }

    @Test
    public void shouldFailWhenPlanetDesiredIsNotFound() throws Exception {
        String name = "thisPlanetDoesNotExist";

        given(planetRepository.findByName(name)).willReturn(null);

        mvc.perform(get("http://localhost:" + this.port + "/api/v1/planets/?name=" + name))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAllPlanetsFromSWPublicAPI() throws Exception {
        mvc.perform(get("http://localhost:" + this.port + "/api/v1/planets/publicAPI/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(61)))
                .andExpect(jsonPath("$.results", hasSize(10)));
    }

    @Test
    public void shouldDeletePlanetById() throws Exception {
        Planet planet = new Planet(0, "Marte", "Warm", "Plain", 0);
        given(planetRepository.findById(planet.getId())).willReturn(planet);
        doNothing().when(planetRepository).delete(planet);

        mvc.perform(delete("http://localhost:" + this.port + "/api/v1/planets/" + planet.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFailDeletingWhenPlanetIsNotFound() throws Exception {
        given(planetRepository.findById(0)).willReturn(null);

        mvc.perform(delete("http://localhost:" + this.port + "/api/v1/planets/0"))
                .andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
