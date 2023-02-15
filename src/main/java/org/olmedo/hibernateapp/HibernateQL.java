package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import org.olmedo.hibernateapp.dominio.ClienteDto;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.Arrays;
import java.util.List;

public class HibernateQL {
    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();
        System.out.println("========= Consultar todos =============");
        List<Cliente> clientes = em.createQuery("select c from Cliente c", Cliente.class).getResultList();
        clientes.forEach(System.out::println);

        //constultamos por el id
        System.out.println("========= Consulta por id ===========");
        Cliente cliente = em.createQuery("select c from Cliente c where c.id=:idCliente", Cliente.class).setParameter("idCliente", 1L).getSingleResult();
        System.out.println(cliente);

        System.out.println("====== Consulta solo el nombre por el id =======");
        String nombreCliente = em.createQuery("select c.nombre from Cliente c where c.id=:idNombre", String.class).setParameter("idNombre", 2L).getSingleResult();
        System.out.println(nombreCliente);

        System.out.println("====== Consulta por campos personalizados =======");
        // le pasamos de tipo generico Object de arreglo porque vamos a obtener varios tipos de datos
        Object[] objetoCliente = em.createQuery("select c.id, c.nombre, c.apellido from Cliente c where c.id=:id", Object[].class).setParameter("id", 1L).getSingleResult();

        Long id = (Long) objetoCliente[0];
        String nombre = (String) objetoCliente[1];
        String apellido = (String) objetoCliente[2];
        System.out.println("id= " + id + ", nombre= " + nombre + ", apellido= " + apellido);

        System.out.println("====== Consulta por campos personalizados con lista =======");
        List<Object[]> registros = em.createQuery("select c.id, c.nombre, c.apellido from Cliente c ", Object[].class).getResultList();
        //for (Object[] reg : registros) { // otra forma tambien con el for
        registros.forEach(reg -> {
                    ;
                    Long idCli = (Long) reg[0];
                    String nombreCli = (String) reg[1];
                    String apellidoCli = (String) reg[2];
                    System.out.println("id= " + idCli + ", nombre= " + nombreCli + ", apellido= " + apellidoCli);
                });
        //}

        System.out.println("========= Constulta por cliente y forma de pago ============");
        // estamos usando registros porque ya lo tenemos definido arriba
        registros = em.createQuery("select c, c.formaPago from Cliente c", Object[].class).getResultList();
        registros.forEach(reg -> {
            Cliente c = (Cliente) reg[0];
            String formaPago = (String) reg[1];
            System.out.println("fomapago= " + formaPago + "," + c);
        });

        System.out.println(" ========= Consulta que puebla y devuelve objeto entity de una clase personalizada ===============");
        // para que esto funcione tenemos que tener el constructor en cliente con los dos parametros que le estamos pasando
        // en la cosulta en este caso nombre y apellido
        clientes = em.createQuery("select new Cliente(c.nombre, c.apellido) from Cliente c",Cliente.class).getResultList();
        clientes.forEach(System.out::println);


        System.out.println(" ========= Consulta que puebla y devuelve objeto Dto de una clase personalizada ===============");
        List<ClienteDto> clientesDto = em.createQuery("select new org.olmedo.hibernateapp.dominio.ClienteDto(c.nombre, c.apellido) from Cliente c", ClienteDto.class).getResultList();
        clientesDto.forEach(System.out::println);


        System.out.println("======== Consulta con nombre de clientes =========");
        List<String> nombres = em.createQuery("select c.nombre from Cliente c", String.class).getResultList();
        nombres.forEach(System.out::println);


        System.out.println("========= Consulta con nombres unicos de clientes ==========");
        // solo muestra un solo nombre de todos lo que alla en la base de datos asi que no va mostrar los que contenctan el mismo nombre
        nombres = em.createQuery("select distinct(c.nombre) from Cliente c", String.class).getResultList();
        nombres.forEach(System.out::println);

        System.out.println("========== Constulta con formas de pagos unicas =========");
        List<String> formasPago = em.createQuery("select distinct(c.formaPago) from Cliente c", String.class).getResultList();
        formasPago.forEach(System.out::println);

        System.out.println("========== Consulta con numero de formas de pagos unicas =========");
        Long totalFormasPago = em.createQuery("select count(distinct(c.formaPago)) from Cliente c", Long.class).getSingleResult();
        System.out.println(totalFormasPago);

        System.out.println("========== Consulta con nombre y apellido concatenados ==============");
        // el alias as es opcional solamente para la consulta
        // nombres = em.createQuery("select concat(c.nombre, ' ', c.apellido) as nombreCompleto from Cliente c", String.class).getResultList();

        // esta es otra manera de hacerlo con ||
        nombres = em.createQuery("select c.nombre || ' ' || c.apellido as nombreCompleto from Cliente c", String.class).getResultList();
        nombres.forEach(System.out::println);


        System.out.println("========== Consulta con nombre y apellido concatenados en mayuscula ==============");
        // tambien lo podemos convertir todo en minuscula con el lower en ves de upper
        // se puede aplicar a cualquier atributo de la clase entity siempre que sea de tipo string
        nombres = em.createQuery("select upper(concat(c.nombre, ' ', c.apellido)) as nombreCompleto from Cliente c", String.class).getResultList();
        nombres.forEach(System.out::println);

        System.out.println("======== consulta para buscar por nombres =============");
        String param = "NA";
        // el like : parametro que matchee con lo que estoy buscando y el % + param + % para que lo que encuentre de adelante o de atras con lo que busco que coincida con la busqueda
        // recomendable siempre comparar con mismo tipo de case --> upper(mayuscula) o lower(minuscula) , tanto en el nombre como en el parametro
        clientes = em.createQuery("select c from Cliente c where upper(c.nombre) like upper(:parametro)", Cliente.class).setParameter("parametro", "%" + param + "%").getResultList();
        clientes.forEach(System.out::println);


        // solo lo podemos usar en tipo de datos numericos rangos por ejemplo --> la edad de una persona, precio de algun producto
        System.out.println("========= Consulta por rangos ===========");
        // between que el cliente este en el rango de id 2 y 5
        //clientes = em.createQuery("select c from Cliente c where c.id between 2 and 5", Cliente.class).getResultList();

        // ahora que sea por rango de caracteres se aplica el tipo string, solamente desde la J hasta la P, pero la P no se incluye
        //tambien se puede hacer con fechas
        clientes = em.createQuery("select c from Cliente c where c.nombre between 'J' and 'P'", Cliente.class).getResultList();
        clientes.forEach(System.out::println);

        System.out.println("====== Consulta con order ========");
        // con el asc ordena de forma ascendente de la A hasta la Z
        //clientes = em.createQuery("select c from Cliente c order by c.nombre asc", Cliente.class).getResultList();

        // con el desc ordena de forma descendente de la Z a la A
        //clientes = em.createQuery("select c from Cliente c order by c.nombre desc", Cliente.class).getResultList();

        // tambien de manera encadenada tanto el nombre como el apellido de manera descendente de la Z a la A
        // tambien le podemos pasar en c.nombre asc, c.apellido desc
        clientes = em.createQuery("select c from Cliente c order by c.nombre desc, c.apellido desc", Cliente.class).getResultList();
        clientes.forEach(System.out::println);


        System.out.println("========== Consulta con total de registro de la tabla ============");
        // siempre es de tipo Long
        // siempre de un solo resultado con getSingleResult()
        Long total = em.createQuery("select count(c) as total from Cliente c", Long.class).getSingleResult();
        System.out.println(total);

        System.out.println("============ Consulta con valor minimo del id ===========");
        // donde se pasa c.id pordria ser cualquier tipo de campo numerico por ejemplo --> entero, decimal, flotante, edad, precio
        // el alias as es opcional
        Long minId = em.createQuery("select min(c.id) as minimo from Cliente c", Long.class).getSingleResult();
        System.out.println(minId);

        System.out.println("============ Consulta con valor maximo / ultimo id ===========");
        // donde se pasa c.id pordria ser cualquier tipo de campo numerico por ejemplo --> entero, decimal, flotante, edad, precio
        // el alias as es opcional
        Long maxId = em.createQuery("select max(c.id) as maximo from Cliente c", Long.class).getSingleResult();
        System.out.println(maxId);

        System.out.println("======= Consulta con nombre y su largo =========");
        // como registro lo tenemos definido arriba ya lo usamos ya que es un List<Object[]>
        registros = em.createQuery("select c.nombre, length(c.nombre) from Cliente c", Object[].class).getResultList();
        registros.forEach(reg -> {
            String name = (String) reg[0];
            Integer largo = (Integer) reg[1];
            System.out.println("nombre= " + name + ", largo= " + largo);
        });

        System.out.println("========== Consulta con el nombre mas corto ==========");
        // el length es de tipo Integer
        Integer minLargoNombre = em.createQuery("select min(length(c.nombre)) from Cliente c", Integer.class).getSingleResult();
        System.out.println(minLargoNombre);

        System.out.println("========== Consulta con el nombre mas largo ==========");
        // el length es de tipo Integer
        Integer maxLargoNombre = em.createQuery("select max(length(c.nombre)) from Cliente c", Integer.class).getSingleResult();
        System.out.println(maxLargoNombre);


        System.out.println("========== Consulta resumen funciones agregaciones count min max avg sum"); // avg es el promedio, sum es sumar
        // el promedio le pasamos dentro length para que sea distinto al largo del nombre
        Object[] estadisticas = em.createQuery("select min(c.id), max(c.id), sum(c.id), count(c.id), avg(length(c.nombre)) from Cliente c", Object[].class).getSingleResult();
        //aca tenemos que obtener cada uno
        Long min = (Long) estadisticas[0];
        Long max = (Long) estadisticas[1];
        Long sum = (Long) estadisticas[2];
        Long count = (Long) estadisticas[3];
        Double avg = (Double) estadisticas[4];

        System.out.println("min= " + min + ", max= " + max + ", sum= " + sum + ", count= " + count + ", avg= " + avg);


        System.out.println("---------------- SUBQUERY O SUBCONSULTAS EN JPLQ -----------------");

        System.out.println("======== Consulta con el nombre mas corto y su largo =========");
        // where --> cuando el largo del nombre sea igual al tamanio minimo de todos los clientes si queremos con el max lo cambiamos por el min
        registros = em.createQuery("select c.nombre, length(c.nombre) from Cliente c where " +
                " length(c.nombre) = (select min(length(c.nombre)) from Cliente c)", Object[].class).getResultList();

        registros.forEach(reg -> {
            String name = (String) reg[0];
            Integer largo = (Integer) reg[1];
            System.out.println("nombre= " + name + ", largo= " + largo);
        });

        System.out.println("========= Consulta para obtener el ultimo registro =============");
        Cliente ultimoCliente = em.createQuery("select c from Cliente c where c.id = (select max(c.id) from Cliente c)", Cliente.class).getSingleResult();
        System.out.println(ultimoCliente);

        System.out.println("========= Consulta where in ==========");
        // in -> para que el id se encuentre dentro de un subConjunto solo va a traer el id 1, 2 y 10 de manera literal
        // tambien lo podemos pasar como nombre de parametros con los :ids, si le pasamos un id que no exita lo va a omitir simplemente
        clientes = em.createQuery("select c from Cliente c where c.id in :ids", Cliente.class).setParameter("ids", Arrays.asList(1L, 2L, 10L, 6L)).getResultList();
        clientes.forEach(System.out::println);


        em.close(); //para cerrar la conexion siempre
    }
}
