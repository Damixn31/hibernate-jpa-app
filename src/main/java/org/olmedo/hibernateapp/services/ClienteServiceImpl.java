package org.olmedo.hibernateapp.services;

import jakarta.persistence.EntityManager;
import org.olmedo.hibernateapp.entity.Cliente;
import org.olmedo.hibernateapp.repositories.ClienteRepository;
import org.olmedo.hibernateapp.repositories.CrudRepository;

import java.util.List;
import java.util.Optional;

// un service es un patron de disenio que nos permite tres cosas principalmente:
//1. desacoplar cualquier logica de negocio que estemos utilizando directamente en una clase cliente por ej una clase main, servlet en cualquier tipo de controlador
//2. podemos trabajar con varios atributos repositorios o DAO dentro de una misma clase service, y todo estos DAO repositorios puede colaborar al mismo tiempo entre si en un mismo metodo
//3. el manejo de transacciones en su metodo, todos lo metodos del service que realizen operaciones o modifican una tabla deberian ser transaccionales

public class ClienteServiceImpl implements ClienteService{
   private EntityManager em;
   private CrudRepository<Cliente> repository;

    public ClienteServiceImpl(EntityManager em) {
        this.em = em;
        this.repository = new ClienteRepository(em);
    }

    @Override
    public List<Cliente> listar() {
        return repository.listar();
    }

    @Override
    public Optional<Cliente> porId(Long id) {
        return Optional.ofNullable(repository.porId(id));
    }

    @Override
    public void guardar(Cliente cliente) {

        try {
            em.getTransaction().begin();
            repository.guardar(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {

        try {
            em.getTransaction().begin();
            repository.eliminar(id);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
