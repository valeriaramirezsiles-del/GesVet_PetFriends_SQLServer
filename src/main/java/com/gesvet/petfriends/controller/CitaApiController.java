package com.gesvet.petfriends.controller;

import com.gesvet.petfriends.dto.CitaDetalleDto;
import com.gesvet.petfriends.entity.Cita;
import com.gesvet.petfriends.service.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
public class CitaApiController {

    private final CitaService citaService;

    public CitaApiController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public List<CitaDetalleDto> listarCitas() {
        return citaService.obtenerTodasLasCitas();
    }

    @PostMapping
    public ResponseEntity<?> guardarCita(@RequestBody Cita cita) {
        if (cita.getIdMascota() == null ||
                cita.getIdServicio() == null ||
                cita.getFecha() == null ||
                cita.getHora() == null) {

            return ResponseEntity.badRequest().body(
                    Map.of("mensaje", "Debe completar la mascota, el servicio, la fecha y la hora.")
            );
        }

        cita.setIdCita(null);
        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado("Pendiente");
        }

        Cita guardada = citaService.guardarCita(cita);

        return ResponseEntity.ok(
                Map.of(
                        "ok", true,
                        "mensaje", "Cita registrada correctamente.",
                        "id_cita", guardada.getIdCita()
                )
        );
    }
}
