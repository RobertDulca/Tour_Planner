package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.repository.ToursTabDatabaseRepository;
import at.technikum.tour_planner.service.ToursTabService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuBarViewModel {
    private static final Logger logger = Logger.getLogger(MenuBarViewModel.class.getName());
    private final Publisher publisher;
    private final ToursTabService tourService;

    public MenuBarViewModel(Publisher publisher) {
        this.publisher = publisher;
        this.tourService = new ToursTabService(new ToursTabDatabaseRepository());
    }

    public void importTourFromCsv(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 7) { // Updated to check for 7 values
                    String name = values[0];
                    String description = values[1];
                    String origin = values[2];
                    String destination = values[3];
                    String transportType = values[4];
                    double distance = Double.parseDouble(values[5]);
                    double estimatedTime = Double.parseDouble(values[6]);

                    logger.log(Level.INFO, "Importing tour: {0}", name);

                    Tour newTour = new Tour(name, description, origin, destination, transportType, "");
                    newTour.setDistance(distance); // Set distance
                    newTour.setEstimatedTime(estimatedTime); // Set estimated time
                    tourService.saveTour(newTour);
                    publisher.publish(Event.TOUR_IMPORTED, newTour);
                } else {
                    logger.log(Level.WARNING, "Invalid CSV format: {0}", line);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error importing tours", e);
            throw e;
        }
    }


}
