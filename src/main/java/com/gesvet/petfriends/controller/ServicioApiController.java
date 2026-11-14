package com.gesvet.petfriends.controller;

import com.gesvet.petfriends.entity.Servicio;
import com.gesvet.petfriends.repository.ServicioRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ServicioApiController {

    private final ServicioRepository servicioRepository;

    public ServicioApiController(
            ServicioRepository servicioRepository) {

        this.servicioRepository = servicioRepository;
    }

    @GetMapping("/servicios")
    public Map<String, Object> obtenerServicios() {

        List<Map<String, Object>> servicios =
                servicioRepository.findByActivoTrue()
                        .stream()
                        .map(servicio -> Map.<String, Object>of(
                                "id_servicio", servicio.getIdServicio(),
                                "nombre", servicio.getNombre()
                        ))
                        .toList();

        return Map.of(
                "ok", true,
                "servicios", servicios
        );
    }
}