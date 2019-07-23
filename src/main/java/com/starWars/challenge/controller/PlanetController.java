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

//TODO(discover a way to document this API automatically)
@Controller
//@Api(value = "Planets")
@RequestMapping(value = "/api/v1/planets")
@PropertySource("swApi.properties")
public class PlanetController {

    private final PlanetRepository planetRepository;

    public PlanetController(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Value("${sw.api.url}")
    private String swApiURL;

//    @ApiOperation(value = "Application health checker")
    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<DefaultAPIResponse> health() {
        DefaultAPIResponse response = new DefaultAPIResponse(HttpStatus.OK.value(), "UP");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    @ResponseBody
    public List<Planet> getPlanetByName(@RequestParam(name="name", required = false) String name) {
        if (name == null) return planetRepository.findAll();

        Planet planet = planetRepository.findByName(name);
        if (planet == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Planet '" + name + "' not found!");

        return singletonList(planetRepository.findByName(name));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Planet getPlanetById(@PathVariable int id) {
        Planet planet = planetRepository.findById(id);
        if (planet == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Planet Id '" + id + "' not found!");
        return planet;
    }

    @GetMapping("/publicAPI/")
    @ResponseBody
    public SWAPIResult getPlanetsFromSWPublicAPI(@RequestParam(name="page", required = false) String page) {
        //TODO(Create a service to group this code below)
        try {
            String uri = swApiURL + "/planets";
            if (page != null) uri += "/?page=" + page;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);

            return deserialize(result.getBody());
        } catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page '" + page + "' not found!");
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public Planet add(@RequestBody PlanetInput planetInput) {
        Planet planet = planetRepository.findByName(planetInput.getName());
        if (planet != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Planet '" + planetInput.getName() + "' already exists!");

        return planetRepository.save(new Planet(planetInput));
    }

    public SWAPIResult deserialize(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SWAPIResult.class, new SWAPIResultDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, SWAPIResult.class);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public DefaultAPIResponse delete(@PathVariable int id) {
        Planet planetToDelete = getPlanetById(id);
        planetRepository.delete(planetToDelete);
        return new DefaultAPIResponse(HttpStatus.OK.value(), "Planet Id '" + id + "' deleted successfully");
    }

}
