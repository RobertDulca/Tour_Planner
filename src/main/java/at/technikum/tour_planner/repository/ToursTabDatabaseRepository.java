package at.technikum.tour_planner.repository;

import at.technikum.tour_planner.entity.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ToursTabDatabaseRepository implements ToursTabRepository{
    private final EntityManagerFactory entityManagerFactory;

    public ToursTabDatabaseRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate");
    }

    @Override
    public List<Tour> findAll() {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Tour> criteriaQuery = criteriaBuilder.createQuery(Tour.class);
        Root<Tour> root = criteriaQuery.from(Tour.class);
        CriteriaQuery<Tour> all = criteriaQuery.select(root);

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(all).getResultList();
        }
    }
    //TODO: Rework the following methods to use the EntityManagerFactory
    @Override
    public Optional<Tour> findById(UUID id) {
        Tour tour = entityManager.find(Tour.class, id);
        return tour != null ? Optional.of(tour) : Optional.empty();
    }

    @Override
    public Tour save(Tour tour) {
        if (tour.getId() == null) {
            entityManager.persist(tour);
            return tour;
        } else {
            return entityManager.merge(tour);
        }
    }

    @Override
    public void deleteById(UUID id) {
        Tour tour = entityManager.find(Tour.class, id);
        if (tour != null) {
            entityManager.remove(tour);
        }
    }
}
