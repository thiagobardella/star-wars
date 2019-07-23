package com.starWars.challenge.controller;

import com.google.gson.*;
import com.starWars.challenge.model.DefaultAPIResponse;
import com.starWars.challenge.model.Planet;
import com.starWars.challenge.model.PlanetInput;
import com.starWars.challenge.model.SWAPIResult;
import com.starWars.challenge.repository.PlanetRepository;
import com.starWars.challenge.service.SWAPIResultDeserializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.Collections.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@Controller
@Api(value = "Planets")
@RequestMapping(value = "/api/v1/planets")
@PropertySource("swApi.properties")
public class PlanetController {

    private final PlanetRepository planetRepository;

    public PlanetController(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Value("${sw.api.url}")
    private String swApiURL;

    @ApiOperation(value = "Application health checker")
    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<DefaultAPIResponse> health() {
        DefaultAPIResponse response = new DefaultAPIResponse(OK.value(), "UP");

        return new ResponseEntity<>(response, OK);
    }

    @ApiOperation(value = "Retrieves all planets already registered or a planet filtered by name")
    @GetMapping("/")
    @ResponseBody
    public List<Planet> getPlanetByName(@RequestParam(name="name", required = false) String name) {
        if (name == null) return planetRepository.findAll();

        Planet planet = planetRepository.findByName(name);
        if (planet == null) throw new ResponseStatusException(NOT_FOUND, "Planet '" + name + "' not found!");

        return singletonList(planetRepository.findByName(name));
    }

    @ApiOperation(value = "Retrieves a planet filtered by {id}")
    @GetMapping("/{id}")
    @ResponseBody
    public Planet getPlanetById(@PathVariable int id) {
        Planet planet = planetRepository.findById(id);
        if (planet == null) throw new ResponseStatusException(NOT_FOUND, "Planet Id '" + id + "' not found!");

        return planet;
    }

    @ApiOperation(value = "Retrieves all planets from Star Wars public API. Once there may be too many planets, the results will be paginated")
    @GetMapping("/publicAPI/")
    @ResponseBody
    public SWAPIResult getPlanetsFromSWPublicAPI(@RequestParam(name = "page", required = false) String page) {
        try {
            String uri = swApiURL + "/planets";
            if (page != null) uri += "/?page=" + page;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> result = restTemplate.exchange(uri, GET, request, String.class);

            return deserialize(result.getBody());
        } catch(Exception ex) {
            throw new ResponseStatusException(NOT_FOUND, "Page '" + page + "' not found!");
        }
    }

    @ApiOperation(value = "Register a new planet")
    @PostMapping("/add")
    @ResponseBody
    public Planet add(@RequestBody PlanetInput planetInput) {
        Planet planet = planetRepository.findByName(planetInput.getName());
        if (planet != null) throw new ResponseStatusException(CONFLICT, "Planet '" + planetInput.getName() + "' already exists!");

        return planetRepository.save(new Planet(planetInput));
    }

    private SWAPIResult deserialize(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SWAPIResult.class, new SWAPIResultDeserializer());
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, SWAPIResult.class);
    }

    @ApiOperation(value = "Delete a planet defined by {id}")
    @DeleteMapping("/{id}")
    @ResponseBody
    public DefaultAPIResponse delete(@PathVariable int id) {
        Planet planetToDelete = getPlanetById(id);
        planetRepository.delete(planetToDelete);

        return new DefaultAPIResponse(OK.value(), "Planet Id '" + id + "' deleted successfully");
    }

}
