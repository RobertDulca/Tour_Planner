package at.technikum.tour_planner.service;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.repository.ToursTabDatabaseRepository;
import at.technikum.tour_planner.repository.ToursTabRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ToursTabService {
    private final ToursTabRepository<Tour> tourRepository;

    public ToursTabService(ToursTabRepository<Tour> tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(UUID id) {
        return tourRepository.findById(id);
    }

    public Tour saveTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public void deleteTour(UUID id) {
        tourRepository.deleteById(id);
    }

    public void updateTour(Tour selectedTour) {
        tourRepository.update(selectedTour);
    }
}
