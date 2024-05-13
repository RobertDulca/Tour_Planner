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
        tourLogOverviewRepository.save(new TourLogModel(term));
    }

    public List<Long> findAll() {
        return tourLogOverviewRepository.findAll().stream()
                .map(TourLogModel::getTourLogId)
                .toList();
    }
}
