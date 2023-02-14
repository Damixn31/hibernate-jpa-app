package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.Scanner;

public class HibernatePorIdForma2 {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Ingrese el id: ");
        Long id = s.nextLong();
        EntityManager em = JpaUtil.getEntityManager();
        Cliente cliente = em.find(Cliente.class, id);

        System.out.println(cliente);

        // una sola consulta para dos ejecuciones
        //cuando usa el id utiliza el cache
        Cliente cliente2 = em.find(Cliente.class, id);
        // cuando le pasamos el id por parametro en este caso 2L hace dos consultas a la base de datos
       // Cliente cliente2 = em.find(Cliente.class, 2L);
        System.out.println(cliente2);

        em.close();
    }
}
