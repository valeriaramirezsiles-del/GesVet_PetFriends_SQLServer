package com.gesvet.petfriends.controller;

import com.gesvet.petfriends.dto.CitaDetalleDto;
import com.gesvet.petfriends.entity.Atencion;
import com.gesvet.petfriends.entity.Veterinario;
import com.gesvet.petfriends.repository.VeterinarioRepository;
import com.gesvet.petfriends.service.AtencionService;
import com.gesvet.petfriends.service.CitaService;
import com.gesvet.petfriends.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/panel")
public class PanelApiController {

    private final DashboardService dashboardService;
    private final CitaService citaService;
    private final AtencionService atencionService;
    private final VeterinarioRepository veterinarioRepository;

    public PanelApiController(DashboardService dashboardService,
                              CitaService citaService,
                              AtencionService atencionService,
                              VeterinarioRepository veterinarioRepository) {
        this.dashboardService = dashboardService;
        this.citaService = citaService;
        this.atencionService = atencionService;
        this.veterinarioRepository = veterinarioRepository;
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticas());
    }

    @GetMapping("/citas")
    public ResponseEntity<List<CitaDetalleDto>> listarCitas() {
        return ResponseEntity.ok(citaService.obtenerTodasLasCitas());
    }

    @PostMapping("/citas/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Estado no proporcionado."));
        }
        boolean ok = citaService.actualizarEstado(id, nuevoEstado);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado correctamente.", "estado", nuevoEstado));
    }

    @PostMapping("/citas/{id}/veterinario")
    public ResponseEntity<?> asignarVeterinario(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        Integer idVeterinario = body.get("idVeterinario");
        boolean ok = citaService.asignarVeterinario(id, idVeterinario);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("mensaje", "Veterinario asignado correctamente."));
    }

    @GetMapping("/citas/{id}/atencion")
    public ResponseEntity<?> obtenerAtencion(@PathVariable Integer id) {
        return atencionService.obtenerPorCita(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/citas/{id}/atencion")
    public ResponseEntity<?> registrarAtencion(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Integer idMascota = body.get("idMascota") != null ? Integer.parseInt(body.get("idMascota").toString()) : null;
        String diagnostico = (String) body.get("diagnostico");
        String tratamiento = (String) body.get("tratamiento");
        String observaciones = (String) body.get("observaciones");

        if (idMascota == null || diagnostico == null || diagnostico.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Diagnóstico y mascota son obligatorios."));
        }

        Atencion atencion = atencionService.registrarAtencion(id, idMascota, diagnostico, tratamiento, observaciones);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Atención clínica registrada exitosamente.",
                "idAtencion", atencion.getIdAtencion()
        ));
    }

    @GetMapping("/veterinarios")
    public ResponseEntity<List<Veterinario>> listarVeterinarios() {
        return ResponseEntity.ok(veterinarioRepository.findByActivoTrue());
    }
}
