package at.technikum.tour_planner.repository;

import at.technikum.tour_planner.entity.Tour;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ToursTabRepository {
    List<Tour> findAll();
    Optional<Tour> findById(UUID id);
    Tour save(Tour tour);
    boolean deleteById(UUID id);
    Tour update(Tour tour);
}
