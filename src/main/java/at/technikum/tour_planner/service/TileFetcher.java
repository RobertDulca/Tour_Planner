package at.technikum.tour_planner.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class TileFetcher {
    private static final String CONFIG_FILE = "/config.properties";
    private static final String USER_AGENT;
    private static final String TILE_BASE_URL;

    static {
        try (InputStream input = TileFetcher.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find " + CONFIG_FILE);
            }
            Properties properties = new Properties();
            properties.load(input);
            TILE_BASE_URL = properties.getProperty("tile.base.url");
            USER_AGENT = properties.getProperty("user.agent");
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration", ex);
        }
    }

    public BufferedImage fetchTile(int x, int y, int zoom) throws IOException {
        String tileUrl = String.format("%s/%d/%d/%d.png", TILE_BASE_URL, zoom, x, y);
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
