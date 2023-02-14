package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.List;
import java.util.Scanner;

public class HibernateResultListWhere {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("select c from Cliente c where c.formaPago=?1", Cliente.class);
        System.out.println("Ingrese una forma de pago: ");
        String pago = s.next();
        query.setParameter(1, pago);
        //query.setMaxResults(1); // esto va obtener el primero que encuentra en la base de datos y no lanza error
        // cuando usamos getResultList()? --> cuando existe varios registro en este caso va a devolverme un arreglo con todos los clientes que su pago sea con debito
        List<Cliente> clientes = query.getResultList(); // si tenemos mas registro usamos getResultList y que el tipo de dato sea una Lista de clientes

        System.out.println(clientes);
        em.close();
    }
}
