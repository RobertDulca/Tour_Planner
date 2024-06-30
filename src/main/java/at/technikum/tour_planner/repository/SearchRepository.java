package at.technikum.tour_planner.repository;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchRepository {
    private final EntityManagerFactory entityManagerFactory;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final Pattern ratingPattern = Pattern.compile("rt:(\\d+)");
    private static final Pattern difficultyPattern = Pattern.compile("df:(\\d+)");
    private static final Pattern totalTimePattern = Pattern.compile("tt:(\\d+)");
    private static final Pattern totalDistancePattern = Pattern.compile("td:(\\d+)");

    public SearchRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate");
    }

    public List<UUID> searchTours(String searchText) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UUID> cq = cb.createQuery(UUID.class);
            Root<Tour> tour = cq.from(Tour.class);

            List<Predicate> predicates = new ArrayList<>();

            if (searchText != null && !searchText.trim().isEmpty()) {
                String searchPattern = "%" + searchText.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(tour.get("name")), searchPattern),
                        cb.like(cb.lower(tour.get("description")), searchPattern),
                        cb.like(cb.lower(tour.get("start")), searchPattern),
                        cb.like(cb.lower(tour.get("destination")), searchPattern)
                ));
            }

            cq.select(tour.get("id")).where(predicates.toArray(new Predicate[0]));

            return entityManager.createQuery(cq).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<UUID> searchTourLogs(String searchText, UUID tourId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<UUID> cq = cb.createQuery(UUID.class);
            Root<TourLogModel> tourLog = cq.from(TourLogModel.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(tourLog.get("tour").get("id"), tourId));

            if (searchText != null && !searchText.trim().isEmpty()) {
                // Check for rating pattern
                Matcher ratingMatcher = ratingPattern.matcher(searchText);
                if (ratingMatcher.find()) {
                    int rating = Integer.parseInt(ratingMatcher.group(1));
                    Predicate ratingPredicate = cb.equal(tourLog.get("rating"), rating);
                    predicates.add(ratingPredicate);
                }

                // Check for difficulty pattern
                Matcher difficultyMatcher = difficultyPattern.matcher(searchText);
                if (difficultyMatcher.find()) {
                    int difficulty = Integer.parseInt(difficultyMatcher.group(1));
                    Predicate difficultyPredicate = cb.equal(tourLog.get("difficulty"), difficulty);
                    predicates.add(difficultyPredicate);
                }

                Matcher totalTimeMatcher = totalTimePattern.matcher(searchText);
                if (totalTimeMatcher.find()) {
                    int totalTime = Integer.parseInt(totalTimeMatcher.group(1));
                    Predicate totalTimePredicate = cb.equal(tourLog.get("totalTime"), totalTime);
                    predicates.add(totalTimePredicate);
                }

                Matcher totalDistanceMatcher = totalDistancePattern.matcher(searchText);
                if (totalDistanceMatcher.find()) {
                    int totalDistance = Integer.parseInt(totalDistanceMatcher.group(1));
                    Predicate totalDistancePredicate = cb.equal(tourLog.get("totalDistance"), totalDistance);
                    predicates.add(totalDistancePredicate);
                }

                // Try to parse the searchText as a date
                try {
                    LocalDate date = LocalDate.parse(searchText, dateFormatter);
                    Predicate datePredicate = cb.equal(tourLog.get("date"), date);
                    predicates.add(datePredicate);
                } catch (DateTimeParseException e) {
                    // If parsing fails, just ignore the date filter
                }
            }

            cq.select(tourLog.get("id")).where(predicates.toArray(new Predicate[0]));

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
