package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import javax.swing.*;

public class HibernateCrear {
    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre: ");
            String apellido = JOptionPane.showInputDialog("Ingrese el apellido: ");
            String pago = JOptionPane.showInputDialog("Ingrese la forma de pago: ");
            em.getTransaction().begin();

            Cliente c = new Cliente(); // crea el nuevo objeto
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setFormaPago(pago);

            //aca lo guardado siempre tiene que estar dentro de begin() y el commit()
            em.persist(c); // el persist() --> aca guarda el objeto en la base de datos

            // el commit() transpasa este objeto de jpa o hibernate hacia la base de datos, es decir hace efectivo la transaccion hace el insert en la base de datos y eso sincroniza los datos del objeto con los datos de la tabla
            em.getTransaction().commit();

            System.out.println("El id del cliente registrado es " + c.getId());
            // el find() lo va a buscar en el contexto y no en la base de datos
            c = em.find(Cliente.class, c.getId());
            System.out.println(c);

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();

        }
    }
}
