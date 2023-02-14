package org.olmedo.hibernateapp.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory(){
        return Persistence.createEntityManagerFactory("ejemploJPA"); // ejemploJPA es el nombre que le dimos en el archivo persistence.xml
    }

    public static EntityManager getEntityManager(){ // getEntityManager() siempre va utilizar el mismo objeto la misma instancia de entityManagerFactory es un singleton para toda la aplicacion solamente del factory
        return entityManagerFactory.createEntityManager(); // crea un entityManager por cada hilo por cada request
    }
}
