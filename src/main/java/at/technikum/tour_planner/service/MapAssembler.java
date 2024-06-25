package at.technikum.tour_planner.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapAssembler {
    private TileFetcher tileFetcher = new TileFetcher();

    public BufferedImage assembleMap(double centerLat, double centerLon, int zoom, int gridSize) throws IOException {
        var topLeftTile = TileCalculator.latlon2Tile(centerLat, centerLon, zoom);
        var bottomRightTile = TileCalculator.latlon2Tile(centerLat, centerLon, zoom);

        int tilesX = bottomRightTile.x() - topLeftTile.x() + 1;
        int tilesY = bottomRightTile.y() - topLeftTile.y() + 1;

        BufferedImage mapImage = new BufferedImage(tilesX * 256, tilesY * 256, BufferedImage.TYPE_INT_ARGB);
        Graphics g = mapImage.getGraphics();

        for (int x = topLeftTile.x(); x <= bottomRightTile.x(); x++) {
            for (int y = topLeftTile.y(); y <= bottomRightTile.y(); y++) {
                BufferedImage tileImage = tileFetcher.fetchTile(x, y, zoom);
                int xPos = (x - topLeftTile.x()) * 256;
                int yPos = (y - topLeftTile.y()) * 256;
                g.drawImage(tileImage, xPos, yPos, null);
            }
        }

        g.dispose();
        return mapImage;
    }
}