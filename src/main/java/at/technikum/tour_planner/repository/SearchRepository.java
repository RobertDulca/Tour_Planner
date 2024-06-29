package at.technikum.tour_planner.repository;

import at.technikum.tour_planner.entity.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchRepository {
    private final EntityManagerFactory entityManagerFactory;

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
}
