package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.Arrays;
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

        System.out.println("========== Usando where like para busacr clientes por nombre ============");
        //cuando creamos una nueva consulta tenemos que tener un nuevo query y un nuevo from, pero aca la variable ya la tenemos query asi que en este caso la reasignamos
        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        //tambien otra forma es unsado ParameterExpression pasando el tipo en este caso String, si fuera por id seria de tipo Long
        ParameterExpression<String> nombreParamLike = criteria.parameter(String.class, "nombreParam");
        //el like resive un segundo argumento recordar que cuando usamos el like usamos los % tanto adenate y tras para que busque las coincidencias a la derecha como a la izquierda de string
        // siempre es convieniente envolver todo en upper o lower, le pasamos el criteria.upper
        query.select(from).where(criteria.like(criteria.upper(from.get("nombre")), criteria.upper(nombreParamLike)));
        clientes = em.createQuery(query).setParameter("nombreParam", "%an%").getResultList();
        clientes.forEach(System.out::println);

        System.out.println("=========== Ejemplo usando where between para rangos =========");
        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        query.select(from).where(criteria.between(from.get("id"), 2L, 6L));
        clientes = em.createQuery(query).getResultList();
        clientes.forEach(System.out::println);

        System.out.println("========= Consulta where in =========");
        // filtra por los registro que le pasemos en esta le pasamos solo 3 pero le podemos pasar lo que nosotros queremos
        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        ParameterExpression<List> listParam = criteria.parameter(List.class, "nombres");
        query.select(from).where(from.get("nombre").in(listParam));
        clientes = em.createQuery(query)
                .setParameter("nombres", Arrays.asList("Damian", "John", "Lou"))
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("======== Filtrar usnado predicados mayor que o mayor igual que =========");
        // tambiente estan estos metodos
        // equals -> igual
        // lt -> menor que
        // le -> menor o igual
        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        // ge -> es una abreviacion de greaterThanOrEqualTo()  que singifica mayor que o igual a
        query.select(from).where(criteria.ge(from.get("id"), 2L));
        clientes = em.createQuery(query).getResultList();
        clientes.forEach(System.out::println);

        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        // ahora que me muestre los nombres que tengan caracteres meyor que 5
        // gt -> si tengo algun nombre que tenga 6 caracteres no lo va a encontrar que es mayor no incluye el ultimo caracter
        query.select(from).where(criteria.gt(criteria.length(from.get("nombre")), 5L));
        clientes = em.createQuery(query).getResultList();
        clientes.forEach(System.out::println);

        System.out.println("======= Consulta con los predicados conjuncion and y disyucion or =======");
        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        // vamos a tener un and y un or y esos son de tipo Predicate
        Predicate porNombre = criteria.equal(from.get("nombre"), "Damian");
        Predicate porFormaPago = criteria.equal(from.get("formaPago"), "debito");
        Predicate p3 = criteria.ge(from.get("id"), 4L);
        // si le paso el and -> me devuelve el nombre y la forma de pago
        // si le paso el or -> me devuelve los nombres y todos los con ese nombre tengan esa forma de pago en este caso debito
        query.select(from).where(criteria.and(p3, criteria.or(porNombre, porFormaPago)));
        clientes = em.createQuery(query).getResultList();
        clientes.forEach(System.out::println);



        em.close();
    }
}
