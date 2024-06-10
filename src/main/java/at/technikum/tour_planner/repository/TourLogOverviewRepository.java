package at.technikum.tour_planner.repository;

import at.technikum.tour_planner.entity.TourLogModel;

import java.util.ArrayList;
import java.util.List;

public class TourLogOverviewRepository {
    private final List<TourLogModel> tourLogModels;

    public TourLogOverviewRepository() {
        tourLogModels = new ArrayList<>();
    }

    public List<TourLogModel> findAll() {
        return tourLogModels;
    }

    public TourLogModel save(TourLogModel entity) {
        tourLogModels.add(entity);

        return entity;
    }
}
