package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.List;

public class HibernateCriteria {
    public static void main(String[] args) {

        System.out.println("------- ESTO ES OTRA FORMA DE CONTRUIR UN CONSULTA A LA BASE DE DATOS CON CRITERIA LO HACE MAS DINAMICO ---------");
        EntityManager em = JpaUtil.getEntityManager();

        //CriteriaBuilder nos permite generar o crear paso a paso nuetra consulta de hibernate
        // por debajo usa el patron de disenio builder
        CriteriaBuilder criteria = em.getCriteriaBuilder();
        //siempre importar de jakarta.persistence
        CriteriaQuery<Cliente> query = criteria.createQuery(Cliente.class); // como el segundo argumento que le pasamos en JPQL

        // el from representa a nuestra consultas por lo tanto podemos acceder a todos los metodos de la clase entity
        Root<Cliente> from = query.from(Cliente.class); // esto equivale al from Cliente como JPQL

        // si quiero alistar todo los clientes
        query.select(from); // esto equivale a un select
        List<Cliente> clientes = em.createQuery(query).getResultList(); // al createQuery le pasamos el query que contiene todo la sentencia completa, aca no necesitamos segundo parametro ya que lo manejamos en el criteria
        clientes.forEach(System.out::println);

        System.out.println("========== Listar con where equals ========");
        // como la variable query ya la tenemos arriba la vamos a sobreescribir
        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        ParameterExpression<String> nombreParam = criteria.parameter(String.class, "nombre");
        query.select(from).where(criteria.equal(from.get("nombre"), nombreParam));//va a listar a todos los clientes con nombre Damian
        clientes = em.createQuery(query).setParameter("nombre", "Damian").getResultList();
        clientes.forEach(System.out::println);

        em.close();
    }
}
