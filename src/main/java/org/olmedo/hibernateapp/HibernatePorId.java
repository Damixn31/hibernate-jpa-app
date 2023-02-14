package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.Scanner;

public class HibernatePorId {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("select c from Cliente c where c.id=?1", Cliente.class);
        System.out.println("Ingrese el id: ");
        Long pago = s.nextLong();
        query.setParameter(1, pago);
        Cliente c = (Cliente) query.getSingleResult(); // si tenemos mas registro usamos getResultList y que el tipo de dato sea una Lista de clientes
        System.out.println(c);
        em.close();
    }
}
