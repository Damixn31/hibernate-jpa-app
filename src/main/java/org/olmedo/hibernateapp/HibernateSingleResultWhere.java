package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.Scanner;

public class HibernateSingleResultWhere {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("select c from Cliente c where c.formaPago=?1", Cliente.class);
        System.out.println("Ingrese una forma de pago: ");
        String pago = s.next();
        query.setParameter(1, pago);
        query.setMaxResults(1); // esto va obtener el primero que encuentra en la base de datos y no lanza error
        // cuando usamos getSingleResult()? --> cuando existe un solo registro en esa busqueda por ej: por id, email, username
        Cliente c = (Cliente) query.getSingleResult(); // si tenemos mas registro usamos getResultList y que el tipo de dato sea una Lista de clientes

        System.out.println(c);
        em.close();
    }
}
