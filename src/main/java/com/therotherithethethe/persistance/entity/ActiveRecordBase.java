package com.therotherithethethe.persistance.entity;

import com.therotherithethethe.domain.utils.HibernateUtil;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Abstract base class for active record pattern implementation.
 *
 * @param <T> the type of the model
 */
public abstract class ActiveRecordBase<T extends Model> {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    //private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    /**
     * Finds an entity by a column value.
     *
     * @param columnName the column name
     * @param value the value to search for
     * @param clazz the entity class
     * @return an optional containing the found entity or empty if not found
     */
    public static Optional<?> findByColumn(
        String columnName, Object value, Class<? extends Model> clazz) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<?> query =
                session.createQuery(STR."FROM \{clazz.getSimpleName()} WHERE \{columnName} = :1",
                    clazz);
            query.setParameter(":1", value);
            return query.getResultStream().findFirst();
        } catch (Exception ex) {
            // TODO: need to use logger
            return Optional.empty();
        }
    }
    /**
     * Saves the entity to the database.
     *
     * @return true if the entity was saved successfully, false otherwise
     */
    public boolean save() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            if (Objects.isNull(getId())) {
                session.persist(this);
            } else {
                session.merge(this);
            }
            transaction.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Finds all entities of the specified type.
     *
     * @return a list of all entities
     */
    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return getTypedQuery(session).getResultList();
        } catch (HibernateException ex) {
            // TODO: need to use logger
            return null;
        }
    }
    /**
     * Deletes the entity from the database.
     */
    private TypedQuery<T> getTypedQuery(Session session) {
        Class<T> clazz = (Class<T>) getClass();
        return session.createQuery("FROM " + clazz.getSimpleName(), clazz);
    }
    /**
     * Gets the ID of the entity.
     *
     * @return the ID of the entity
     */
    public abstract UUID getId();
    /**
     * Sets the ID of the entity.
     *
     * @param id the ID to set
     */
    public abstract void setId(UUID id);


    public void delete() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(this);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Finds an entity by its ID.
     *
     * @param uuid the UUID of the entity
     * @return an optional containing the found entity or empty if not found
     */
    public Optional<T> findById(UUID uuid) {
        List<T> byColumn = findByColumn(en -> true);
        return Optional.empty();
    }
    /**
     * Finds entities by a column value using a predicate.
     *
     * @param predicate the predicate to apply to the column value
     * @return a list of entities matching the predicate
     */
    public List<T> findByColumn(Predicate<T> predicate) {
        try (Session session = sessionFactory.openSession()) {
            return getTypedQuery(session)
                .getResultStream()
                .filter(predicate)
                .toList();

        } catch (Exception ex) {
            // TODO: need to use logger
            return Collections.emptyList();
        }
    }
}
