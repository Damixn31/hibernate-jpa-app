package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import javax.swing.*;

public class HibernateEditar {
    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();

        try{
            Long id = Long.valueOf(JOptionPane.showInputDialog("Ingrese el id del cliente a modificar: "));
            Cliente c = em.find(Cliente.class, id);

            String nombre = JOptionPane.showInputDialog("Ingrese el nombre: ", c.getNombre());
            String apellido = JOptionPane.showInputDialog("Ingrese el apellido: ", c.getApellido());
            String pago = JOptionPane.showInputDialog("Ingrese la forma de pago: ", c.getFormaPago());

            em.getTransaction().begin();

            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setFormaPago(pago);

            // merge actualiza los datos del objeto pero lo actualizada en el contecto de persistencia
            //en hibernate y jpa sin hacer el update, sin realizar los cambios en la base de datos
            em.merge(c);  // aca pasamos el objeto que vamos a modificar

            // aca cuando hacemos el commit() aca sincronizar el objeto c que esta actulizado
            // en el contexto de persistencia con los datos de la tabla de la base de datos
            // aca se sincroniza y se actualiza aca es donde se hace el update siempre despues de commit
            em.getTransaction().commit();
            System.out.println(c);

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();

        }
    }
}
