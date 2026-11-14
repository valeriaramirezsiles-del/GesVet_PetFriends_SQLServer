package com.gesvet.petfriends.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "atenciones")
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atencion")
    private Integer idAtencion;

    @Column(name = "id_cita", nullable = false, unique = true)
    private Integer idCita;

    @Column(name = "id_mascota", nullable = false)
    private Integer idMascota;

    @Column(name = "diagnostico", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String diagnostico;

    @Column(name = "tratamiento", columnDefinition = "NVARCHAR(MAX)")
    private String tratamiento;

    @Column(name = "observaciones", columnDefinition = "NVARCHAR(MAX)")
    private String observaciones;

    @Column(name = "fecha_atencion", insertable = false, updatable = false)
    private LocalDateTime fechaAtencion;

    public Atencion() {}

    public Atencion(Integer idCita, Integer idMascota, String diagnostico, String tratamiento, String observaciones) {
        this.idCita = idCita;
        this.idMascota = idMascota;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
    }

    public Integer getIdAtencion() {
        return idAtencion;
    }

    public void setIdAtencion(Integer idAtencion) {
        this.idAtencion = idAtencion;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDateTime fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }
}
