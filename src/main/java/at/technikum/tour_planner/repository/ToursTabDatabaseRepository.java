package at.technikum.tour_planner.repository;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ToursTabDatabaseRepository implements ToursTabRepository <Tour>{
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

    @Override
    public Optional<Tour> findById(UUID id) {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Tour> criteriaQuery = criteriaBuilder.createQuery(Tour.class);
        Root<Tour> root = criteriaQuery.from(Tour.class);

        Predicate termPredicate = criteriaBuilder.equal(root.get("id"), id);
        criteriaQuery.where(termPredicate);
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Tour tour = entityManager.createQuery(criteriaQuery).getSingleResult();

            return Optional.of(tour);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tour save(Tour tour) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(tour);
            transaction.commit();
        }

        return tour;
    }

    @Override
    public boolean deleteById(UUID id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Tour tour = em.find(Tour.class, id);
            if (tour != null) {
                em.remove(tour);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Tour update(Tour updatedTour) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Tour mergedTour = em.merge(updatedTour);
            transaction.commit();
            return mergedTour;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Or handle it depending on your error handling policy
        } finally {
            em.close();
        }
    }

    @Override
    public List<Tour> findByTourId(UUID tourId) {
        return Collections.emptyList();
    }
    @Override
    public List<Tour> findToursByID(List<UUID> searchedToursID) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tour> cq = cb.createQuery(Tour.class);
            Root<Tour> tour = cq.from(Tour.class);

            Predicate predicate = tour.get("id").in(searchedToursID);
            cq.where(predicate);

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Tour> findTourLogsByID(List<UUID> logIds) {
        return Collections.emptyList();
    }
}
