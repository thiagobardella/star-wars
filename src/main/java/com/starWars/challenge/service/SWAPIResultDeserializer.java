package com.starWars.challenge.service;

import com.google.gson.*;
import com.starWars.challenge.model.PlanetInput;
import com.starWars.challenge.model.SWAPIResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SWAPIResultDeserializer implements JsonDeserializer<SWAPIResult> {

    @Override
    public SWAPIResult deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject swApiResultJson = json.getAsJsonObject();

        final int count = swApiResultJson.get("count").getAsInt();

        final JsonArray results = swApiResultJson.get("results").getAsJsonArray();
        List<PlanetInput> swPlanets = new ArrayList<>();
        results.forEach(planet -> {
                    JsonObject planetJson = planet.getAsJsonObject();
                    String planetName = planetJson.get("name").getAsString();
                    String planetClimate = planetJson.get("climate").getAsString();
                    String planetTerrain = planetJson.get("terrain").getAsString();
                    int planetFilmsCount = planetJson.get("films").getAsJsonArray().size();
                    swPlanets.add(new PlanetInput(planetName, planetClimate, planetTerrain, planetFilmsCount));
                }
        );

        final int resultsPerPage = results.size();
        final int lastPage = (count%resultsPerPage != 0) ? count/resultsPerPage + 1 : count/resultsPerPage;

        return new SWAPIResult(count, lastPage, swPlanets);
    }
}
