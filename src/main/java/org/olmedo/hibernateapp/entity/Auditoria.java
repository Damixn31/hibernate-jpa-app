package org.olmedo.hibernateapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;


/*@Embeddable y @Enbedded -> son dos anotaciones que trabajan en conjunto nos permite
 * desacoplar algun codigo que se repite entre las clase entity
 * llevarlos a otra clase que la vamos anotar con @Embeddable que se puede incrustar en
 * nuetras clases entity para que se puedan reutilizar*/
@Embeddable
public class Auditoria {

    //creamos un atributo para probar el @PrePersist y @PreUpdate
    @Column(name = "creado_en") // en la tabla se va a llamar distinto por eso indicamos el colum
    private LocalDateTime creadoEn; // como tiene camelCase en la base de datos va a estar separado entonces lo pasamos por @Colum
    @Column(name = "editado_en")
    private LocalDateTime editadoEn;

    /* esto para ver los eventos del ciclo de vida de los entity usando anotaciones
     * por ejemplo se puede llamar un metodo de forma automatica del entity justo antes
     * de persistir el entity, crear un nuevo registro en la base de datos, para inicializar
     * algun atributo del entity de forma automatica, tambien para inicializar antes del update
     * y tambien antes de remove
     * @PrePersist -> antes de persistir un nuevo entity en la base de datos
     * @PostPersist -> hacer algo justo despues de persistir el entity en la base de datos
     *
     * @PreUpdate -> hacer algo justo antes de actualizar el objeto en la base de datos
     * @PostUpdate -> para el justo despues
     *
     * @PreRemove ->
     * @PostRemove ->
     *
     *  */
    @PrePersist
    public void prePersist() {
        System.out.println("Inicializar algo justo antes de persist");
        this.creadoEn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        System.out.println("Inicializar algo justo antes del update");
        this.editadoEn = LocalDateTime.now();
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public LocalDateTime getEditadoEn() {
        return editadoEn;
    }

    public void setEditadoEn(LocalDateTime editadoEn) {
        this.editadoEn = editadoEn;
    }
}


