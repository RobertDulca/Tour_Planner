package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToursTabViewModelTest {
    ToursTabViewModel viewModel;

    @BeforeEach
    public void setUp() {
        //viewModel = ToursTabViewModel.getInstance();
        viewModel.getTours().clear(); // Clear the list before each test
    }
    @Test
    public void testAddTour() {

        Tour tour = new Tour("Tour1", "Description", "From", "To", "Car", "src/main/resources/img.png");
        viewModel.addTour(tour);
        assertTrue(viewModel.getTours().contains(tour));
    }

    @Test
    public void testRemoveTour() {

        Tour tour = new Tour("Tour1", "Description", "From", "To", "Car", "src/main/resources/img.png");
        viewModel.addTour(tour);
        viewModel.removeTour(tour);
        assertFalse(viewModel.getTours().contains(tour));
    }

    @Test
    public void testListFindRemovedItem() {

        viewModel.getTours().clear(); // Clear list for testing

        Tour tour1 = new Tour("Tour4", "Description1", "From1", "To1", "Car", "src/main/resources/img.png");
        Tour tour2 = new Tour("Tour5", "Description2", "From2", "To2", "Walk", "src/main/resources/img.png");
        viewModel.addTour(tour1);
        viewModel.addTour(tour2);

        viewModel.removeTour(tour1);

        assertFalse(viewModel.getTours().contains(tour1));
        assertTrue(viewModel.getTours().contains(tour2));
    }
    @Test
    public void testRemoveTours() {

        Tour tour1 = new Tour("Tour1", "Description1", "From1", "To1", "Bus", "src/resources/img1.png");
        Tour tour2 = new Tour("Tour2", "Description2", "From2", "To2", "Train", "src/resources/img2.png");
        viewModel.addTour(tour1);
        viewModel.addTour(tour2);

        // Remove tour1 and check if it is removed
        viewModel.removeTour(tour1);
        assertFalse(viewModel.getTours().contains(tour1));

        // Remove the last tour and check if no tour is selected
        viewModel.removeTour(tour2);
        assertTrue(viewModel.getTours().isEmpty());
    }
}