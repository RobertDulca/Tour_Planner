package at.technikum.tour_planner.repository;

import at.technikum.tour_planner.entity.Tour;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ToursTabRepository <T> {
    List<T> findAll();
    Optional<T> findById(UUID id);
    List<T> findByTourId(UUID tourId);
    T save(T tour);
    boolean deleteById(UUID id);
    T update(T tour);
    List<T> findToursByID(List<UUID> searchedToursID);
}
