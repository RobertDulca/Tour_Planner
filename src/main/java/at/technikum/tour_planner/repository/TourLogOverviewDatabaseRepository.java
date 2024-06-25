package at.technikum.tour_planner.repository;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TourLogOverviewDatabaseRepository implements ToursTabRepository <TourLogModel>{
    private final EntityManagerFactory entityManagerFactory;

    public TourLogOverviewDatabaseRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate");
    }

    @Override
    public List<TourLogModel> findAll() {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLogModel> criteriaQuery = criteriaBuilder.createQuery(TourLogModel.class);
        Root<TourLogModel> root = criteriaQuery.from(TourLogModel.class);
        CriteriaQuery<TourLogModel> all = criteriaQuery.select(root);

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(all).getResultList();
        }
    }

    //TODO: Rework the following methods to use the EntityManagerFactory
    @Override
    public Optional<TourLogModel> findById(UUID id) {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLogModel> criteriaQuery = criteriaBuilder.createQuery(TourLogModel.class);
        Root<TourLogModel> root = criteriaQuery.from(TourLogModel.class);

        Predicate termPredicate = criteriaBuilder.equal(root.get("id"), id);
        criteriaQuery.where(termPredicate);
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TourLogModel tour = entityManager.createQuery(criteriaQuery).getSingleResult();

            return Optional.of(tour);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public TourLogModel save(TourLogModel tour) {
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
            TourLogModel tour = em.find(TourLogModel.class, id);
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
    public TourLogModel update(TourLogModel updatedTour) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            TourLogModel mergedTour = em.merge(updatedTour);
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
    public List<TourLogModel> findByTourId(UUID tourId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<TourLogModel> query = em.createQuery("SELECT l FROM TourLogModel l WHERE l.tour.id = :tourId", TourLogModel.class);
        query.setParameter("tourId", tourId);
        return query.getResultList();
    }
}
