package at.technikum.tour_planner.service;

import at.technikum.tour_planner.entity.Tour;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OpenRouteService {
    private static final String API_KEY = "5b3ce3597851110001cf62489053f9d72da2401d91e21169cbc311f9";
    private static final String BASE_URL = "https://api.openrouteservice.org/v2/directions/";

    public String getRoute(String fromLat, String fromLon, String toLat, String toLon, String transportType) throws Exception {
        String urlString = String.format("%s%s?api_key=%s&start=%s,%s&end=%s,%s", BASE_URL, transportType, API_KEY, fromLon, fromLat, toLon, toLat);
        return makeHttpRequest(urlString);
    }

    public double[] geocodeAddress(String address) throws Exception {
        String urlString = String.format("https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s", API_KEY, URLEncoder.encode(address, StandardCharsets.UTF_8));
        String response = makeHttpRequest(urlString);

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray features = jsonObject.getAsJsonArray("features");
        if (features == null || features.isEmpty()) {
            throw new Exception("No coordinates found for the address: " + address);
        }
        JsonObject geometry = features.get(0).getAsJsonObject().getAsJsonObject("geometry");
        JsonArray coordinates = geometry.getAsJsonArray("coordinates");
        return new double[]{coordinates.get(1).getAsDouble(), coordinates.get(0).getAsDouble()};
    }

    public RouteInfo parseRoute(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        if (jsonObject.has("error")) {
            String errorMessage = jsonObject.get("error").getAsString();
            throw new RuntimeException("API Error: " + errorMessage);
        }
        JsonArray features = jsonObject.getAsJsonArray("features");
        if (features == null || features.isEmpty()) {
            throw new RuntimeException("No features found in the response.");
        }
        JsonObject feature = features.get(0).getAsJsonObject();
        JsonObject properties = feature.getAsJsonObject("properties");
        JsonObject summary = properties.getAsJsonObject("summary");
        if (summary == null) {
            throw new RuntimeException("No summary found in the properties.");
        }
        double distance = summary.get("distance").getAsDouble() / 1000; // Kilometers
        double duration = summary.get("duration").getAsDouble() / 60; // Minutes
        return new RouteInfo(distance, duration);
    }

    public BufferedImage fetchMapForTour(Tour tour, int zoom, int gridSize) throws IOException {
        double[] fromCoords = null;
        try {
            fromCoords = geocodeAddress(tour.getOrigin());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        double[] toCoords = null;
        try {
            toCoords = geocodeAddress(tour.getDestination());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MapAssembler mapAssembler = new MapAssembler();
        double centerLat = (fromCoords[0] + toCoords[0]) / 2;
        double centerLon = (fromCoords[1] + toCoords[1]) / 2;
        return mapAssembler.assembleMap(centerLat, centerLon, zoom, gridSize);
    }

    private String makeHttpRequest(String urlString) throws Exception {
        URI uri = new URI(urlString);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new Exception("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();
        return sb.toString();
    }
}