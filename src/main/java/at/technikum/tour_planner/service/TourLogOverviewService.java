package at.technikum.tour_planner.service;

import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.repository.ToursTabRepository;

import java.util.List;
import java.util.UUID;

public class TourLogOverviewService {
    private final ToursTabRepository<TourLogModel> tourLogOverviewRepository;

    public TourLogOverviewService(ToursTabRepository<TourLogModel> tourLogOverviewRepository) {
        this.tourLogOverviewRepository = tourLogOverviewRepository;
    }

    public void add(TourLogModel term) {
        tourLogOverviewRepository.save(term);
    }

    public void delete(UUID id) {
        tourLogOverviewRepository.deleteById(id);
    }

    public void update(TourLogModel term) {
        tourLogOverviewRepository.update(term);
    }

    public List<TourLogModel> findAll() {
        return tourLogOverviewRepository.findAll();
    }

    public List<TourLogModel> findByTourId(UUID tourId) {
        return tourLogOverviewRepository.findByTourId(tourId);
    }
}
