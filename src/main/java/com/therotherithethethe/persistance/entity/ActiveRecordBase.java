package com.therotherithethethe.persistance.entity;

import com.therotherithethethe.domain.utils.HibernateUtil;
import jakarta.persistence.TypedQuery;
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

public abstract class ActiveRecordBase<T extends Model> {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    //private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
        } catch (HibernateException ex) {
            // TODO: use logger
            return false;
        }
    }

    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return getTypedQuery(session).getResultList();
        } catch (HibernateException ex) {
            // TODO: need to use logger
            return null;
        }
    }

    private TypedQuery<T> getTypedQuery(Session session) {
        Class<T> clazz = (Class<T>) getClass();
        return session.createQuery("FROM " + clazz.getSimpleName(), clazz);
    }

    public abstract UUID getId();

    public abstract void setId(UUID id);


    public void delete() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.remove(this);
        tx.commit();
        session.close();
    }

    public Optional<T> findById(UUID uuid) {
      return findByColumn(entity ->
          entity.getId()
              .equals(uuid)).stream()
          .findFirst();
    }

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
