package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.TourLogOverviewService;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourLogDetailsViewModelTest {
    @Mock
    private Publisher publisher;

    @Mock
    private TourLogOverviewService tourLogOverviewService;

    private TourLogDetailsViewModel viewModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new TourLogDetailsViewModel(publisher, tourLogOverviewService);
    }

    @Test
    public void testCreateTourLogPublishesEvent() {
        // Arrange
        Tour tour = new Tour();
        viewModel.onTourSelected(tour);

        viewModel.dateProperty().set(LocalDate.now());
        viewModel.commentProperty().set("Great tour!");
        viewModel.difficultyProperty().set(3);
        viewModel.totalTimeProperty().set(5.0);
        viewModel.totalDistanceProperty().set(10.0);
        viewModel.ratingProperty().set(4);

        // Act
        viewModel.createTourLog();

        // Assert
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(publisher, times(1)).publish(eq(Event.TOUR_LOG_CREATED), argumentCaptor.capture());

        TourLogModel publishedLog = (TourLogModel) argumentCaptor.getValue();
        assertEquals("Great tour!", publishedLog.getComment());
        assertEquals(3, publishedLog.getDifficulty());
        assertEquals(5.0, publishedLog.getTotalTime());
        assertEquals(10.0, publishedLog.getTotalDistance());
        assertEquals(4, publishedLog.getRating());
    }

    @Test
    public void testUpdateTourLogPublishesEvent() {
        // Arrange
        TourLogModel tourLog = new TourLogModel(LocalDate.now(), "Initial comment", 2, 4.0, 8.0, 3);
        viewModel.onTourLogSelected(tourLog);

        viewModel.commentProperty().set("Updated comment");
        viewModel.difficultyProperty().set(4);
        viewModel.totalTimeProperty().set(6.0);
        viewModel.totalDistanceProperty().set(12.0);
        viewModel.ratingProperty().set(5);

        // Act
        viewModel.updateTourLog();

        // Assert
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(publisher, times(1)).publish(eq(Event.TOUR_LOG_UPDATED), argumentCaptor.capture());

        TourLogModel publishedLog = (TourLogModel) argumentCaptor.getValue();
        assertEquals("Updated comment", publishedLog.getComment());
        assertEquals(4, publishedLog.getDifficulty());
        assertEquals(6.0, publishedLog.getTotalTime());
        assertEquals(12.0, publishedLog.getTotalDistance());
        assertEquals(5, publishedLog.getRating());
    }

    @Test
    public void testDeleteTourLogPublishesEvent() {
        // Arrange
        UUID tourLogId = UUID.randomUUID();
        TourLogModel tourLog = new TourLogModel(LocalDate.now(), "Log to delete", 1, 2.0, 4.0, 2);
        tourLog.setId(tourLogId);
        viewModel.onTourLogSelected(tourLog);

        // Act
        viewModel.deleteSelectedTourLog();

        // Assert
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(publisher, times(1)).publish(eq(Event.TOUR_LOG_DELETED), argumentCaptor.capture());

        TourLogModel publishedLog = (TourLogModel) argumentCaptor.getValue();
        assertEquals(tourLog, publishedLog);
    }
}