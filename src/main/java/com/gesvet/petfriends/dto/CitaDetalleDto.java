package com.gesvet.petfriends.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaDetalleDto(
        Integer idCita,
        LocalDate fecha,
        LocalTime hora,
        String estado,
        String observaciones,
        Integer idMascota,
        String nombreMascota,
        String tipoMascota,
        String razaMascota,
        Integer idPropietario,
        String nombrePropietario,
        String cedulaPropietario,
        String telefonoPropietario,
        Integer idServicio,
        String nombreServicio,
        Integer idVeterinario,
        String nombreVeterinario,
        boolean atendida
) {}
