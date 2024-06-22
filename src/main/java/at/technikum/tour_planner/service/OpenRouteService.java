package at.technikum.tour_planner.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenRouteService {
    private static final String API_KEY = "5b3ce3597851110001cf62489053f9d72da2401d91e21169cbc311f9";
    private static final String BASE_URL = "https://api.openrouteservice.org/v2/directions/driving-car";

    public String getRoute(String fromLat, String fromLon, String toLat, String toLon) throws Exception {
        String urlString = String.format("%s?api_key=%s&start=%s,%s&end=%s,%s", BASE_URL, API_KEY, fromLon, fromLat, toLon, toLat);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new Exception("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();
        return sb.toString();
    }

    public RouteInfo parseRoute(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Check if there are any errors in the response
        if (jsonObject.has("error")) {
            String errorMessage = jsonObject.get("error").getAsString();
            throw new RuntimeException("API Error: " + errorMessage);
        }

        JsonArray features = jsonObject.getAsJsonArray("features");
        if (features == null || features.isEmpty()) {
            System.err.println("No features found in the response: " + jsonObject);
            throw new RuntimeException("No features found in the response.");
        }

        JsonObject feature = features.get(0).getAsJsonObject();
        JsonObject properties = feature.getAsJsonObject("properties");
        JsonArray segments = properties.getAsJsonArray("segments");

        if (segments == null || segments.isEmpty()) {
            System.err.println("No segments found in the feature properties: " + properties);
            throw new RuntimeException("No segments found in the feature properties.");
        }

        JsonObject segment = segments.get(0).getAsJsonObject();

        // Check for the presence of the summary object
        if (!segment.has("summary")) {
            System.err.println("No summary found in the segment: " + segment);
            throw new RuntimeException("No summary found in the segment.");
        }

        JsonObject summary = segment.getAsJsonObject("summary");

        // Ensure that distance and duration are present in the summary
        if (!summary.has("distance") || summary.get("distance").isJsonNull() || !summary.has("duration") || summary.get("duration").isJsonNull()) {
            System.err.println("Incomplete summary found in the segment: " + summary);
            throw new RuntimeException("Incomplete summary found in the segment.");
        }

        double distance = summary.get("distance").getAsDouble();
        double duration = summary.get("duration").getAsDouble();

        return new RouteInfo(distance, duration);
    }

}