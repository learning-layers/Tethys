package de.dbis.acis.cloud.i5cloud.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//singleton

/**
 * Singleton Pattern to just have one EntityManagerFactory
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
public class JPAHelper { 
    private static EntityManagerFactory entityManagerFactory = null;

    /**
     * Empty - no instantiation
     */
    private JPAHelper() { }
    
    /**
     * static initializer - create EntityManagerFactory
     */
    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("i5Cloud");
        } catch (Throwable ex) {
            //logger.error("Initial SessionFactory creation failed", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Returns the EntityManagerFactory
     * 
     * @return the EntityManagerFactory
     */
    public static EntityManagerFactory getInstance() {
    	return entityManagerFactory;
    }
    
}