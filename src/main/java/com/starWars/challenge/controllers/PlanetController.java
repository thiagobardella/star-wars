package com.starWars.challenge.controllers;

import com.google.gson.*;
import com.starWars.challenge.model.Planet;
import com.starWars.challenge.model.SWAPIResult;
import com.starWars.challenge.services.SWAPIResultDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

//TODO(discover a way to document this API automatically)
@Controller
@RequestMapping(value = "/planets/")
@PropertySource("swApi.properties")
public class PlanetController {

    @Value("${sw.api.url}")
    private String swApiURL;

    private final AtomicInteger counter = new AtomicInteger();
    private List<Planet> planets = new ArrayList<>();

    //TODO(How to make this be a Json and not a String type??)
    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "{\"status\":\"OK\"}";
    }

    //TODO(remove the "get" part and just let it with /)
    @GetMapping("/get")
    @ResponseBody
    public List<Planet> getPlanetsFromDB(@RequestParam(name="name", required = false) String name) {
        //TODO(Insert error handling for no parameter or any other error)
        return name == null ? planets : planets.stream().filter(planet -> planet.getName().equals(name)).collect(toList());
//        return planets;
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public Planet getPlanetFromDB(@PathVariable int id) {
        //TODO(Insert error handling for no parameter or any other error)
        //TODO(handle IllegalStateException from toSingleton() method)
        //TODO(how to make it return a JsonNull when there's no planet with input id?)
        return planets.stream().filter(planet -> planet.getId() == id).collect(toSingleton());
    }

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() > 1) {
                        throw new IllegalStateException();
                    }
                    return list.isEmpty() ? null : list.get(0);
                }
        );
    }

    @PostMapping("/add")
    @ResponseBody
    public Planet add(@RequestParam(name="name") String name,
                      @RequestParam(name="climate") String climate,
                      @RequestParam(name="terrain") String terrain) {
        Planet planet = new Planet(counter.incrementAndGet(), name, climate, terrain, new ArrayList<>());
        planets.add(planet);
        return planet;
    }

    @GetMapping("/publicAPI/get")
    @ResponseBody
    public SWAPIResult getPlanetsFromSWPublicAPI() throws IOException {
        //TODO(Create a service to group this code below)
        //TODO(paginate this search)

        String uri = swApiURL + "/planets";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //TODO(check the best header fot this)
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> request = new HttpEntity<>(headers);
//        SWAPIResult result = restTemplate.getForObject(uri, SWAPIResult.class);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);

        SWAPIResult jsonObject = deserialize(result.getBody());
        return jsonObject;
    }


    public SWAPIResult deserialize(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SWAPIResult.class, new SWAPIResultDeserializer());
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, SWAPIResult.class);
    }

    //TODO(present a better answer when no planet is found)
    @DeleteMapping("/{id}")
    @ResponseBody
    public Planet delete(@PathVariable int id) {
        Planet planetToDelete = getPlanetFromDB(id);
        planets.remove(planetToDelete);
        return planetToDelete;
    }

}
