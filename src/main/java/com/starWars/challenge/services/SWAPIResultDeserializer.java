package com.starWars.challenge.services;

import com.google.gson.*;
import com.starWars.challenge.model.SWAPIResult;
import com.starWars.challenge.model.SWPlanet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SWAPIResultDeserializer implements JsonDeserializer<SWAPIResult> {

    @Override
    public SWAPIResult deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();

        final int count = jsonObject.get("count").getAsInt();
        final int lastPage = (count%10 != 0) ? count/10 + 1 : count/10;

        final JsonArray results = jsonObject.get("results").getAsJsonArray();
        List<SWPlanet> swPlanets = new ArrayList<>();
        results.forEach(planet -> {
                JsonObject planetJson = planet.getAsJsonObject();
                String planetName = planetJson.get("name").getAsString();
                String planetClimate = planetJson.get("climate").getAsString();
                String planetTerrain = planetJson.get("terrain").getAsString();
                int planetFilmsCount = planetJson.get("films").getAsJsonArray().size();
                swPlanets.add(new SWPlanet(planetName, planetClimate, planetTerrain, planetFilmsCount));
            }
        );

        return new SWAPIResult(count, lastPage, swPlanets);
    }
}
