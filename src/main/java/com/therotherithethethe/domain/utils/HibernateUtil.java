package com.therotherithethethe.domain.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    //private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    public static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println(STR."Initial SessionFactory creation failed.\{ex}");
            LOGGER.error(ex.getMessage());
            System.exit(1);
            throw new RuntimeException(ex);
        }
    }

    /*public static SessionFactory getSessionFactory() {
        return HibernateUtil.sessionFactory;
    }*/
}
