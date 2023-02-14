package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.Scanner;

public class HibernateEliminar {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Ingrese el id del cliente a eliminar: ");
        Long id = s.nextLong();
        EntityManager em = JpaUtil.getEntityManager();

        try {
            Cliente cliente = em.find(Cliente.class, id);

            em.getTransaction().begin();

            // remove() elimina el objeto selecionado por el id por eso lo busca con el find()
            // no puede eliminar cualquier objeto no podemos crear una instancia de cliente
            // con el new Cliente() y pasarle el id, no se puede porque no esta manejado
            //en el contexto Jpa
            em.remove(cliente);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
