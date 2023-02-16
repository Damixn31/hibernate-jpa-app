package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HibernateCriteriaBusquedaDinamica {
    public static void main(String[] args) {

        /* Busqueda dinamica algunos campos algunos filtros que podrian ser opcionales y solamente va a buscar
         * por lo que ingresemos o lo que completemos en la busqueda, ya sea un formulario en una aplicacion web
         * o incluzo usando la clase Scanner para ingresar datos mediante el teclado por consola
         */

        Scanner s = new Scanner(System.in);

        System.out.println("Filtro para nombre: ");
        String nombre = s.nextLine(); // con esto campuramos el nombre que nos ingrese

        System.out.println("Filtro por el apellido: ");
        String apellido = s.nextLine(); // lo mismo camturamos el apellido

        System.out.println("Filtro para la forma de pago: ");
        String formaPago = s.nextLine();

        EntityManager em = JpaUtil.getEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Cliente> query = cb.createQuery(Cliente.class);
        Root<Cliente> from = query.from(Cliente.class);

        List<Predicate> condiciones = new ArrayList<>();

        // hacemos las condiciones

        if (nombre != null && !nombre.isEmpty()) {
            condiciones.add(cb.equal(from.get("nombre"), nombre));
        }

        if (apellido != null && !apellido.isEmpty()) {
            condiciones.add(cb.equal(from.get("apellido"), apellido));
        }

        if (formaPago != null && !formaPago.equals("")) { // tambien puede ser equal que sea distinto a una cadena vacia
            condiciones.add(cb.equal(from.get("formaPago"), formaPago));
        }

        // ahora construimos el query
        // el where -> acepta un arreglo de predicates por eso tenemos que convertir el arrayList en un arreglo predicates con toArray(new Predicate[]) no de objetos
        query.select(from).where(cb.and(condiciones.toArray(new Predicate[condiciones.size()])));
        List<Cliente> clientes = em.createQuery(query).getResultList();
        clientes.forEach(System.out::println);

        em.close();

    }
}
