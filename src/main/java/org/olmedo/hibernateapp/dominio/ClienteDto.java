package org.olmedo.hibernateapp.dominio;

public class ClienteDto {
    private String nombre;
    private String apellido;

    // como aca no es entity aca da lo mismo poner el contrustor vacio
    // porque esta va a ser una clase que vamos a instaciar nosotros y no jpa

    public ClienteDto(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
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

    @Override
    public String toString() {
        return "{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                '}';
    }
}
