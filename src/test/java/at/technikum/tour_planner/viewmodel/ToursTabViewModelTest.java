package at.technikum.tour_planner.viewmodel;
import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.ToursTabService;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ToursTabViewModelTest {

    private Publisher publisher;
    private ToursTabService tourService;
    private ToursTabViewModel viewModel;

    @BeforeEach
    public void setup() {
        publisher = mock(Publisher.class);
        tourService = mock(ToursTabService.class);
        viewModel = new ToursTabViewModel(publisher, tourService);
    }

    @Test
    public void testSelectTour() {
        Tour tour = new Tour("Test Tour", "Description", "Vienna", "Graz", "Car", "");
        viewModel.selectTour(tour);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(publisher).publish(eq(Event.TOUR_SELECTED), captor.capture());
        assertEquals(tour, captor.getValue());
    }

    @Test
    public void testOnTourCreated() {
        Tour newTour = new Tour("New Tour", "Description", "Vienna", "Salzburg", "Bicycle", "");
        List<Tour> tourList = List.of(newTour);
        when(tourService.getAllTours()).thenReturn(tourList);

        viewModel.onTourCreated(newTour);

        ObservableList<Tour> tours = viewModel.getTours();
        assertEquals(1, tours.size());
        assertEquals(newTour, tours.getFirst());
    }
}