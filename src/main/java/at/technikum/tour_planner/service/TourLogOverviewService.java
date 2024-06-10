package at.technikum.tour_planner.service;

import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.repository.TourLogOverviewRepository;

import java.util.List;

public class TourLogOverviewService {
    private final TourLogOverviewRepository tourLogOverviewRepository;

    public TourLogOverviewService(TourLogOverviewRepository tourLogOverviewRepository) {
        this.tourLogOverviewRepository = tourLogOverviewRepository;
    }

    public void add(TourLogModel term) {
        tourLogOverviewRepository.save(term);
    }

    public List<TourLogModel> findAll() {
        return tourLogOverviewRepository.findAll();
    }
}
