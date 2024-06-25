package at.technikum.tour_planner.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TileFetcher {
    private static final String USER_AGENT = "TourPlannerSwen";

    public BufferedImage fetchTile(int x, int y, int zoom) throws IOException {
        String tileUrl = String.format("https://tile.openstreetmap.org/%d/%d/%d.png", zoom, x, y);
        URL url = new URL(tileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to fetch tile: HTTP error code " + responseCode);
        }

        try (InputStream inputStream = connection.getInputStream()) {
            return ImageIO.read(inputStream);
        } finally {
            connection.disconnect();
        }
    }
}
