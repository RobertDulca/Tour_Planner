package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.TourLogOverviewService;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TourLogOverviewViewModelTest {

    @Mock
    private Publisher publisher;

    @Mock
    private TourLogOverviewService tourLogOverviewService;

    private TourLogOverviewViewModel viewModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new TourLogOverviewViewModel(publisher, tourLogOverviewService);
    }

    @Test
    public void testOnTourSelected() {
        // Arrange
        Tour tour = new Tour();
        UUID tourId = UUID.randomUUID();
        tour.setId(tourId);

        List<TourLogModel> tourLogs = new ArrayList<>();
        tourLogs.add(new TourLogModel(LocalDate.now(), "Comment 1", 1, 1.0, 1.0, 1));
        tourLogs.add(new TourLogModel(LocalDate.now(), "Comment 2", 2, 2.0, 2.0, 2));

        when(tourLogOverviewService.findByTourId(tourId)).thenReturn(tourLogs);

        // Act
        viewModel.onTourSelected(tour);

        // Assert
        ObservableList<TourLogModel> observableTourLogs = viewModel.getTourLogs();
        assertEquals(2, observableTourLogs.size());
        assertEquals("Comment 1", observableTourLogs.get(0).getComment());
        assertEquals("Comment 2", observableTourLogs.get(1).getComment());
    }

    @Test
    public void testUpdateTourLogs() {
        // Arrange
        UUID tourId = UUID.randomUUID();
        Tour tour = new Tour();
        tour.setId(tourId);
        viewModel.onTourSelected(tour);

        List<TourLogModel> tourLogs = new ArrayList<>();
        tourLogs.add(new TourLogModel(LocalDate.now(), "Comment 1", 1, 1.0, 1.0, 1));
        tourLogs.add(new TourLogModel(LocalDate.now(), "Comment 2", 2, 2.0, 2.0, 2));

        when(tourLogOverviewService.findByTourId(tourId)).thenReturn(tourLogs);

        // Act
        viewModel.updateTourLogs(null);

        // Assert
        ObservableList<TourLogModel> observableTourLogs = viewModel.getTourLogs();
        assertEquals(2, observableTourLogs.size());
        assertEquals("Comment 1", observableTourLogs.get(0).getComment());
        assertEquals("Comment 2", observableTourLogs.get(1).getComment());
    }

    @Test
    public void testSearchTourLogs() {
        // Arrange
        List<UUID> logIds = new ArrayList<>();
        logIds.add(UUID.randomUUID());
        logIds.add(UUID.randomUUID());

        List<TourLogModel> tourLogs = new ArrayList<>();
        tourLogs.add(new TourLogModel(LocalDate.now(), "Searched Comment 1", 1, 1.0, 1.0, 1));
        tourLogs.add(new TourLogModel(LocalDate.now(), "Searched Comment 2", 2, 2.0, 2.0, 2));
        tourLogs.get(0).setId(logIds.get(0));
        tourLogs.get(1).setId(logIds.get(1));

        when(tourLogOverviewService.getTourLogsByIds(logIds)).thenReturn(tourLogs);

        // Act
        viewModel.searchTourLogs(logIds);

        // Assert
        ObservableList<TourLogModel> observableTourLogs = viewModel.getTourLogs();
        assertEquals(2, observableTourLogs.size());
        assertEquals("Searched Comment 1", observableTourLogs.get(0).getComment());
        assertEquals("Searched Comment 2", observableTourLogs.get(1).getComment());
        verify(tourLogOverviewService, times(1)).getTourLogsByIds(logIds);
    }
}
