package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.repository.TourLogOverviewDatabaseRepository;
import at.technikum.tour_planner.repository.ToursTabDatabaseRepository;
import at.technikum.tour_planner.service.TourLogOverviewService;
import at.technikum.tour_planner.service.ToursTabService;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuBarViewModel {
    private static final Logger logger = Logger.getLogger(MenuBarViewModel.class.getName());
    private final Publisher publisher;
    private final ToursTabService tourService;
    private final TourLogOverviewService tourLogService;

    public MenuBarViewModel(Publisher publisher) {
        this.publisher = publisher;
        this.tourService = new ToursTabService(new ToursTabDatabaseRepository());
        this.tourLogService = new TourLogOverviewService(new TourLogOverviewDatabaseRepository());
    }

    public void importTourFromCsv(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Tour currentTour = null;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 1) {
                    if (values[0].equalsIgnoreCase("Tour") && values.length >= 9) {
                        // Handle Tour
                        String name = values[1];
                        String description = values[2];
                        String origin = values[3];
                        String destination = values[4];
                        String transportType = values[5];
                        double distance = Double.parseDouble(values[6]);
                        double estimatedTime = Double.parseDouble(values[7]);
                        String imagePath = values[8];

                        logger.log(Level.INFO, "Importing tour: {0}", name);

                        currentTour = new Tour(name, description, origin, destination, transportType, imagePath);
                        currentTour.setDistance(distance);
                        currentTour.setEstimatedTime(estimatedTime);
                        tourService.saveTour(currentTour);
                        publisher.publish(Event.TOUR_IMPORTED, currentTour);
                    } else if (values[0].equalsIgnoreCase("Log") && values.length >= 6 && currentTour != null) {
                        // Handle Log
                        LocalDate date = LocalDate.parse(values[1]);
                        String comment = values[2];
                        int difficulty = Integer.parseInt(values[3]);
                        double totalTime = Double.parseDouble(values[4]);
                        int rating = Integer.parseInt(values[5]);

                        logger.log(Level.INFO, "Importing log for tour: {0}", currentTour.getName());

                        TourLogModel log = new TourLogModel(date, comment, difficulty, totalTime, rating);
                        log.setTour(currentTour);
                        tourLogService.add(log);
                    } else {
                        logger.log(Level.WARNING, "Invalid CSV format: {0}", line);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error importing tours", e);
            throw e;
        }
    }

    public void exportTourToCsv(Tour selectedTour, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            if (selectedTour != null) {
                writer.println("Tour," + selectedTour.getName() + "," + selectedTour.getDescription() + ","
                        + selectedTour.getOrigin() + "," + selectedTour.getDestination() + ","
                        + selectedTour.getTransportType() + "," + selectedTour.getDistance() + ","
                        + selectedTour.getEstimatedTime() + "," + selectedTour.getImageUrl());

                List<TourLogModel> logs = tourLogService.findByTourId(selectedTour.getId());
                for (TourLogModel log : logs) {
                    writer.println("Log," + log.getDate() + "," + log.getComment() + ","
                            + log.getDifficulty() + "," + log.getTotalTime() + "," + log.getRating());
                }

                logger.log(Level.INFO, "Exported tour: {0} to {1}", new Object[]{selectedTour.getName(), file.getPath()});
            } else {
                logger.log(Level.WARNING, "No tour selected for export.");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting tour", e);
            throw e;
        }
    }
}
