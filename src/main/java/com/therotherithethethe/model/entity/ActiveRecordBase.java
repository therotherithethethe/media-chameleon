package com.therotherithethethe.model.entity;

import com.therotherithethethe.model.HibernateUtil;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class ActiveRecordBase {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public boolean save() {
        if (Objects.isNull(getId())) {
            setId(UUID.randomUUID());
            return insert();
        }
        return update();
    }

    public List<?> findAll(Class<? extends Model> clazz) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<?> query = session.createQuery(STR."FROM \{clazz.getSimpleName()}", clazz);
            return query.getResultList();
        } catch (Exception ex) {
            //TODO: need to use logger
            return null;
        }
    }
    public abstract UUID getId();
    public abstract void setId(UUID id);
    private boolean insert() {
        try (Session session = sessionFactory.openSession(); ) {
            Transaction transaction = session.beginTransaction();
            session.persist(this);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            //TODO: need to use logger
            return false;
        }
    }

    private boolean update() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(this);
            transaction.commit();
            return true;
        } catch (Throwable ex) {
            //TODO: need to use logger
            return false;
        }
    }

    public void delete() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.remove(this);
        tx.commit();
        session.close();
    }

    public static Optional<?> findByColumn(String columnName, Object value, Class<? extends Model> clazz) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<?> query =
                session.createQuery(
                    STR."FROM \{clazz.getName()} WHERE \{columnName} = :1", clazz);
            query.setParameter(":1", value);
            return query.getResultStream().findFirst();
        } catch (Exception ex) {
            //TODO: need to use logger
            return Optional.empty();
        }
    }
}