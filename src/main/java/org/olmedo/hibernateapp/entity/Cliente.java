package org.olmedo.hibernateapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //aca le estamos diciendo que auto incremental el id
    private Long id;

    private String nombre;
    private String apellido;

    @Column(name = "forma_pago") // como en la tabla lo tenemos con _ le tenemos que pasar el nombre que tenemos en la tabla de la base de datos
    private String formaPago;

    // todo lo que contenga esta clase, atributos son tambien parte de esta clase entity
    @Embedded
    private Auditoria audit = new Auditoria(); // simpre es importante crear la instancia con el newAuditoria para usarla


    // cuando tenemos un constructor que recive parametros siempre tenemos que tener un constructor vacio
    public Cliente() {
    }

    // aca pasamos un contructor con estos dos parametros para pasarlo a la consulta
    // que puebal y devuelve un objeto de una clase personalizada
    public Cliente(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Cliente(Long id, String nombre, String apellido, String formaPago) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.formaPago = formaPago;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }


    @Override
    public String toString() {
        /* cuando generamos una consulta preguntamos si la tiene fecha null, para que no nos tire error, como teniamos
        en la base de datos Clientes con el creado y editado en null nos lanzaba un error*/
        LocalDateTime creado = this.audit != null ? audit.getCreadoEn() : null;
        LocalDateTime editado = this.audit != null ? audit.getEditadoEn() : null;
        return "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", formaPago='" + formaPago + '\'' +
                ", creadoEn='" + creado + '\'' +
                ", editadoEn='" + editado + '\'';
    }
}
