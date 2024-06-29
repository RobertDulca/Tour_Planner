package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.repository.TourLogOverviewDatabaseRepository;
import at.technikum.tour_planner.repository.ToursTabDatabaseRepository;
import at.technikum.tour_planner.service.TourLogOverviewService;
import at.technikum.tour_planner.service.ToursTabService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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

    public void generateSummaryReport(File file) throws DocumentException, IOException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        List<Tour> tours = tourService.getAllTours();
        if (!tours.isEmpty()) {
            doc.add(new Paragraph("Summary Report"));

            for (Tour tour : tours) {
                List<TourLogModel> logs = tourLogService.findByTourId(tour.getId());
                double totalDistance = 0;
                double totalTime = 0;
                double totalRating = 0;

                for (TourLogModel log : logs) {
                    totalDistance += log.getTotalDistance();
                    totalTime += log.getTotalTime();
                    totalRating += log.getRating();

                }

                double avgDistance = logs.isEmpty() ? 0 : totalDistance / logs.size();
                double avgTime = logs.isEmpty() ? 0 : totalTime / logs.size();
                double avgRating = logs.isEmpty() ? 0 : totalRating / logs.size();

                doc.add(new Paragraph("Tour: " + tour.getName()));
                doc.add(new Paragraph("Average Distance: " + avgDistance));
                doc.add(new Paragraph("Average Time: " + avgTime));
                doc.add(new Paragraph("Average Rating: " + avgRating));
                doc.add(new Paragraph("\n"));
            }

            logger.log(Level.INFO, "Summary Report Generated");
        } else {
            logger.log(Level.WARNING, "No Tours found for Report");
        }

        doc.close();
    }

    public void generateTourReport(Tour selectedTour, File file) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        if (selectedTour != null) {
            document.add(new Paragraph("Tour Report"));
            document.add(new Paragraph("Tour Name: " + selectedTour.getName()));
            document.add(new Paragraph("Description: " + selectedTour.getDescription()));
            document.add(new Paragraph("Origin: " + selectedTour.getOrigin()));
            document.add(new Paragraph("Destination: " + selectedTour.getDestination()));
            document.add(new Paragraph("Transport Type: " + selectedTour.getTransportType()));
            document.add(new Paragraph("Distance: " + selectedTour.getDistance()));
            document.add(new Paragraph("Estimated Time: " + selectedTour.getEstimatedTime()));
            if (selectedTour.getImageUrl() != null && !selectedTour.getImageUrl().isEmpty()) {
                Image image = Image.getInstance(selectedTour.getImageUrl());
                document.add(image);
            }

            List<TourLogModel> logs = tourLogService.findByTourId(selectedTour.getId());
            for (TourLogModel log : logs) {
                document.add(new Paragraph("Log:"));
                document.add(new Paragraph("Date: " + log.getDate()));
                document.add(new Paragraph("Comment: " + log.getComment()));
                document.add(new Paragraph("Difficulty: " + log.getDifficulty()));
                document.add(new Paragraph("Total Time: " + log.getTotalTime()));
                document.add(new Paragraph("Total Distance: " + log.getTotalDistance()));
                document.add(new Paragraph("Rating: " + log.getRating()));
            }

            logger.log(Level.INFO, "Generated Tour Report: {0}", selectedTour.getName());
        } else {
            logger.log(Level.WARNING, "No Tour selected for Report");
        }

        document.close();
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
                    } else if (values[0].equalsIgnoreCase("Log") && values.length >= 7 && currentTour != null) {
                        // Handle Log
                        LocalDate date = LocalDate.parse(values[1]);
                        String comment = values[2];
                        int difficulty = Integer.parseInt(values[3]);
                        double totalTime = Double.parseDouble(values[4]);
                        double totalDistance = Double.parseDouble(values[5]);
                        int rating = Integer.parseInt(values[6]);

                        logger.log(Level.INFO, "Importing log for tour: {0}", currentTour.getName());

                        TourLogModel log = new TourLogModel(date, comment, difficulty, totalTime, totalDistance, rating);
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
                            + log.getDifficulty() + "," + log.getTotalTime() + "," + log.getTotalDistance() + "," + log.getRating());
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
