package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.SearchService;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SearchBarViewModelTest {

    @Mock
    private Publisher publisher;

    @Mock
    private SearchService searchService;

    private SearchBarViewModel viewModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new SearchBarViewModel(publisher, searchService);
    }

    @Test
    public void testPerformSearchTours() {
        // Arrange
        String searchText = "Mountain";
        viewModel.searchTextProperty().set(searchText);
        viewModel.selectedCategoryProperty().set("Tour");

        List<UUID> expectedIds = List.of(UUID.randomUUID());
        when(searchService.searchTours(searchText)).thenReturn(expectedIds);

        // Act
        viewModel.performSearch();

        // Assert
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(publisher, times(1)).publish(eq(Event.TOUR_SEARCHED), argumentCaptor.capture());
        assertEquals(expectedIds, argumentCaptor.getValue());

        verify(searchService, times(1)).searchTours(searchText);
    }

    @Test
    public void testPerformSearchTourLog() {
        // Arrange
        UUID tourId = UUID.randomUUID();
        Tour tour = new Tour();
        tour.setId(tourId);
        viewModel.onTourSelected(tour);

        String searchText = "Great view";
        viewModel.searchTextProperty().set(searchText);
        viewModel.selectedCategoryProperty().set("Tour Log");

        List<UUID> expectedIds = List.of(UUID.randomUUID());
        when(searchService.searchTourLog(searchText, tourId)).thenReturn(expectedIds);

        // Act
        viewModel.performSearch();

        // Assert
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(publisher, times(1)).publish(eq(Event.TOUR_LOG_SEARCHED), argumentCaptor.capture());
        assertEquals(expectedIds, argumentCaptor.getValue());

        verify(searchService, times(1)).searchTourLog(searchText, tourId);
    }

    @Test
    public void testPerformSearchSpecialAttribute() {
        // Arrange
        String searchText = "sunset";
        viewModel.searchTextProperty().set(searchText);
        viewModel.selectedCategoryProperty().set("Special Attribute");

        // Act
        viewModel.performSearch();

        // Assert
        verify(searchService, times(1)).searchAttribute(searchText);
        // You can add further assertions here to verify additional behaviors, such as logging or state changes
    }
}
