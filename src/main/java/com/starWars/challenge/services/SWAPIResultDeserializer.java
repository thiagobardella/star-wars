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
        //TODO(get just the final part to mount my own)
        final String previous = jsonObject.get("previous").isJsonNull() ? null : jsonObject.get("previous").getAsString();
        final String next = jsonObject.get("next").isJsonNull() ? null : jsonObject.get("next").getAsString();
        final JsonArray results = jsonObject.get("results").getAsJsonArray();

        List<SWPlanet> swPlanets = new ArrayList<>();
        results.forEach(planet -> {
                JsonObject planetJson = planet.getAsJsonObject();
                String planetName = planetJson.get("name").getAsString();
                String planetClimate = planetJson.get("climate").getAsString();
                String planetTerrain = planetJson.get("terrain").getAsString();
                //TODO(correct it for films Count)
                List<String> planetFilms = new ArrayList<>();
                swPlanets.add(new SWPlanet(planetName, planetClimate, planetTerrain, planetFilms));
            }
        );

        return new SWAPIResult(count, next, previous, new ArrayList<>());
    }
}
