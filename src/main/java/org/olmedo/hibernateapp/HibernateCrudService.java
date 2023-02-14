package org.olmedo.hibernateapp;

import jakarta.persistence.EntityManager;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.services.ClienteService;
import org.olmedo.hibernateapp.services.ClienteServiceImpl;
import org.olmedo.hibernateapp.util.JpaUtil;

import java.util.List;
import java.util.Optional;

public class HibernateCrudService {
    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();

        ClienteService service = new ClienteServiceImpl(em);

        System.out.println("========= Listar =========");
        List<Cliente> clientes = service.listar();
        clientes.forEach(System.out::println);

        System.out.println("========== Obtener por id ===========");
        Optional<Cliente> optionalCliente = service.porId(1L); // no hace una nueva consulta a la base de datos usa la primer consulta para sacar el por id
        optionalCliente.ifPresent(System.out::println); // esto seria una manera mas simplificada de hacer el if y optener el objeto

        System.out.println("========= Insertar un nuevo cliente ========");
        Cliente cliente = new Cliente();
        cliente.setNombre("Nicolas");
        cliente.setApellido("Olmedo");
        cliente.setFormaPago("paypal");

        service.guardar(cliente);
        System.out.println("Cliente guardado con exito!");

        service.listar().forEach(System.out::println); //lista denuevo los cliente para visualizar el nuevo cliente creado

        System.out.println("========= Editar Cliente ============");
        Long id = cliente.getId();
        optionalCliente = service.porId(id);
        optionalCliente.ifPresent(c -> {
            c.setFormaPago("mercado pago");
            service.guardar(c);
            System.out.println("Cliente editado con exito!");
            service.listar().forEach(System.out::println); // para q vuelva a listar y mostrar los cambios
        });


        System.out.println("========== Eliminar Cliente =========");
        id = cliente.getId(); // el Long lo sacamos porque ya lo tenemos inicializado arriba en editar
        optionalCliente = service.porId(id);
        optionalCliente.ifPresent(c -> {
             service.eliminar(c.getId());
            System.out.println("Cliente eliminado con exito!");
            service.listar().forEach(System.out::println);


        });

        //esta es otra manera valida de poner con un if para eliminar
        /*if (optionalCliente.isPresent()) {
            service.eliminar(id);
        }*/

        em.close();
    }
}
