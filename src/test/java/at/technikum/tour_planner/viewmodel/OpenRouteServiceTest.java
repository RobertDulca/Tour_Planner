package at.technikum.tour_planner.viewmodel;
import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.service.OpenRouteService;
import at.technikum.tour_planner.service.RouteInfo;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class OpenRouteServiceTest {

    @Test
    public void testParseRoute() {
        OpenRouteService service = new OpenRouteService();
        String jsonResponse = "{\"features\":[{\"properties\":{\"summary\":{\"distance\":1000,\"duration\":600}}}]}";
        RouteInfo routeInfo = service.parseRoute(jsonResponse);
        assertEquals(1.0, routeInfo.getDistance(), 0.001);
        assertEquals(10, routeInfo.getDuration(), 0.001);
    }

    @Test
    public void testParseRouteWithError() {
        OpenRouteService service = new OpenRouteService();
        String jsonResponse = "{\"error\":\"Some error occurred\"}";
        Exception exception = assertThrows(RuntimeException.class, () -> service.parseRoute(jsonResponse));
        assertTrue(exception.getMessage().contains("API Error: Some error occurred"));
    }

    private static class TestableOpenRouteService extends OpenRouteService {
        @Override
        protected String makeHttpRequest(String urlString) throws Exception {
            return "{\"features\":[{\"geometry\":{\"coordinates\":[16.3738, 48.2082]}}]}";
        }
    }

    @Test
    public void testGeocodeAddressWithValidResponse() throws Exception {
        TestableOpenRouteService service = new TestableOpenRouteService();
        double[] coords = service.geocodeAddress("Vienna");
        assertEquals(48.2082, coords[0], 0.0001);
        assertEquals(16.3738, coords[1], 0.0001);
    }

    @Test
    public void testSaveImage() throws Exception {
        OpenRouteService service = new OpenRouteService();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        String imagePath = service.saveImage(image);
        assertNotNull(imagePath);
        File file = new File(imagePath);
        assertTrue(file.exists());
        file.delete(); // Clean up the test file
    }

    private static class TestableOpenRouteServiceForMap extends OpenRouteService {
        @Override
        protected String makeHttpRequest(String urlString) throws Exception {
            return "{\"features\":[{\"geometry\":{\"coordinates\":[16.3738, 48.2082]}}]}";
        }
    }

    @Test
    public void testFetchMapForTour() throws Exception {
        OpenRouteService service = new OpenRouteService() {
            @Override
            protected String makeHttpRequest(String urlString) throws Exception {
                return "{\"features\":[{\"geometry\":{\"coordinates\":[16.3738, 48.2082]}}]}";
            }

            @Override
            public BufferedImage fetchMapForTour(Tour tour, int zoom, int gridSize) throws IOException {
                return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            }
        };

        Tour tour = new Tour("Test Tour", "Description", "Wien", "Graz", "Car", "");
        BufferedImage mapImage = service.fetchMapForTour(tour, 16, 3);
        assertNotNull(mapImage);
        assertEquals(100, mapImage.getWidth());
        assertEquals(100, mapImage.getHeight());
    }
}
