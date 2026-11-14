package com.gesvet.petfriends.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "veterinarios")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veterinario")
    private Integer idVeterinario;

    @Column(name = "id_usuario", nullable = false, unique = true)
    private Integer idUsuario;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "especialidad", length = 120)
    private String especialidad;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public Veterinario() {}

    public Veterinario(Integer idUsuario, String nombre, String especialidad, String telefono) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.activo = true;
    }

    public Integer getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Integer idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
