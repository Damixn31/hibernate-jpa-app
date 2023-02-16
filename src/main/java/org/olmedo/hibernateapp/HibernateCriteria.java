package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

        System.out.println("====== Consulta order by asc desc ==========");

        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        // tambien se puede convinar con el where con filtro con todo lo que queramos
        query.select(from).orderBy(criteria.desc(from.get("nombre")), criteria.asc(from.get("apellido")));
        clientes = em.createQuery(query).getResultList();
        clientes.forEach(System.out::println);

        System.out.println("================= Consulta por id ==================");
        query = criteria.createQuery(Cliente.class);
        from = query.from(Cliente.class);
        ParameterExpression<Long> idParam = criteria.parameter(Long.class, "id");

        query.select(from).where(criteria.equal(from.get("id"), idParam));

        Cliente cliente = em.createQuery(query)
                .setParameter("id", 1L)
                .getSingleResult();
        System.out.println(cliente);


        System.out.println("=================== Consulta solo el nombre de los clientes ==============");

        CriteriaQuery<String> queryString = criteria.createQuery(String.class);
        // importante que sea queryString y no query porque vamos a usar a un nuevo
        from = queryString.from(Cliente.class);
        //dentro del from solo vamos a obtener el nombre del cliente porque con from obtenmos todo el objeto completo entonces le pasamos solo el nombre solamente
        queryString.select(from.get("nombre"));
        List<String> nombres = em.createQuery(queryString).getResultList();
        nombres.forEach(System.out::println);

        System.out.println("=================== Consulta solo el nombre de los clientes unicos que no se repitan con distinct ==============");

        queryString = criteria.createQuery(String.class);
        from = queryString.from(Cliente.class);
        queryString.select(criteria.upper(from.get("nombre"))).distinct(true);
        nombres = em.createQuery(queryString).getResultList();
        nombres.forEach(System.out::println);

        System.out.println("=================== Consulta por nombre y apellidos concatenados ================");

        queryString = criteria.createQuery(String.class);
        from = queryString.from(Cliente.class);
        queryString.select(criteria.concat(criteria.concat(from.get("nombre"), " "), from.get("apellido")));
        nombres = em.createQuery(queryString).getResultList();
        nombres.forEach(System.out::println);


        System.out.println("=================== Consulta por nombre y apellidos concatenados upper o lower ================");

        queryString = criteria.createQuery(String.class);
        from = queryString.from(Cliente.class);
        queryString.select(criteria.upper(criteria.concat(criteria.concat(from.get("nombre"), " "), from.get("apellido"))));
        nombres = em.createQuery(queryString).getResultList();
        nombres.forEach(System.out::println);

        System.out.println("================ Consulta de campos personalizados del entity cliente ==================");
        CriteriaQuery<Object[]> queryObject = criteria.createQuery(Object[].class);
        from = queryObject.from(Cliente.class);
        queryObject.multiselect(from.get("id"), from.get("nombre"), from.get("apellido"));
        List<Object[]> registros = em.createQuery(queryObject).getResultList();
        registros.forEach(reg -> {
            Long id = (Long) reg[0];
            String nombre = (String) reg[1];
            String apellido = (String) reg[2];
            System.out.println("id= " + id + ", nombre= " + nombre + ", apellido= " + apellido);
        });

        System.out.println("================ Consulta de campos personalizados del entity cliente con where ==================");
        queryObject = criteria.createQuery(Object[].class);
        from = queryObject.from(Cliente.class);
        // con el where esta filtrando solos que que concidan con lu
        queryObject.multiselect(from.get("id"), from.get("nombre"), from.get("apellido")).where(criteria.like(from.get("nombre"), "%lu%"));
        registros = em.createQuery(queryObject).getResultList();
        registros.forEach(reg -> {
            Long id = (Long) reg[0];
            String nombre = (String) reg[1];
            String apellido = (String) reg[2];
            System.out.println("id= " + id + ", nombre= " + nombre + ", apellido= " + apellido);
        });


        System.out.println("================ Consulta de campos personalizados del entity cliente con where id ==================");
        queryObject = criteria.createQuery(Object[].class);
        from = queryObject.from(Cliente.class);
        queryObject.multiselect(from.get("id"), from.get("nombre"), from.get("apellido")).where(criteria.equal(from.get("id"), 2L));
        Object[] registro = em.createQuery(queryObject).getSingleResult();

        Long id = (Long) registro[0];
        String nombre = (String) registro[1];
        String apellido = (String) registro[2];
        System.out.println("id= " + id + ", nombre= " + nombre + ", apellido= " + apellido);


        System.out.println("=========== Contar registros de la consulta con count ============= ");
        CriteriaQuery<Long> queryLong = criteria.createQuery(Long.class);
        from = queryLong.from(Cliente.class);
        queryLong.select(criteria.count(from.get("id")));
        Long count = em.createQuery(queryLong).getSingleResult();
        System.out.println(count);

        System.out.println("========== Sumar datos de algun campo de la tabla ==========");
        queryLong = criteria.createQuery(Long.class);
        from = queryLong.from(Cliente.class);
        queryLong.select(criteria.sum(from.get("id")));
        Long sum = em.createQuery(queryLong).getSingleResult();
        System.out.println(sum);

        System.out.println("======== Consultar con el maximo id =======");
        queryLong = criteria.createQuery(Long.class);
        from = queryLong.from(Cliente.class);
        queryLong.select(criteria.max(from.get("id")));
        Long max = em.createQuery(queryLong).getSingleResult();
        System.out.println(max);

        System.out.println("======== Consultar con el minimo id =======");
        queryLong = criteria.createQuery(Long.class);
        from = queryLong.from(Cliente.class);
        queryLong.select(criteria.min(from.get("id")));
        Long min = em.createQuery(queryLong).getSingleResult();
        System.out.println(min);


        System.out.println("========== Ejemplo varios resultados de funciones de agregacion en una sola consulta ======");
        queryObject = criteria.createQuery(Object[].class);
        from = queryObject.from(Cliente.class);
        queryObject.multiselect(criteria.count(from.get("id"))
                , criteria.sum(from.get("id"))
                , criteria.max(from.get("id"))
                , criteria.min(from.get("id")));

        registro = em.createQuery(queryObject).getSingleResult();
        count = (Long) registro[0];
        sum = (Long) registro[1];
        max = (Long) registro[2];
        min = (Long) registro[3];

        System.out.println("count= " + count + ", sum= " + sum + ", max= " + max + ", min= " + min);


        em.close();
    }
}
