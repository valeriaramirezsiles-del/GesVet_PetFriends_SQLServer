package com.gesvet.petfriends.controller;

import com.gesvet.petfriends.entity.Mascota;
import com.gesvet.petfriends.entity.Propietario;
import com.gesvet.petfriends.repository.MascotaRepository;
import com.gesvet.petfriends.repository.PropietarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MascotaApiController {

    private final MascotaRepository mascotaRepository;
    private final PropietarioRepository propietarioRepository;

    public MascotaApiController(
            MascotaRepository mascotaRepository,
            PropietarioRepository propietarioRepository) {
        this.mascotaRepository = mascotaRepository;
        this.propietarioRepository = propietarioRepository;
    }

    @GetMapping("/mascotas")
    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }

    @GetMapping("/mascotas/{cedula}")
    public Map<String, Object> buscarMascotas(@PathVariable String cedula) {
        Optional<Propietario> propietario = propietarioRepository.findByCedula(cedula.trim());

        if (propietario.isEmpty()) {
            return Map.of(
                    "ok", true,
                    "mascotas", List.of()
            );
        }

        List<Map<String, Object>> mascotas = mascotaRepository
                .findByIdPropietario(propietario.get().getIdPropietario())
                .stream()
                .map(mascota -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id_mascota", mascota.getIdMascota());
                    m.put("nombre", mascota.getNombre());
                    m.put("tipo", mascota.getTipo());
                    m.put("raza", mascota.getRaza() != null ? mascota.getRaza() : "");
                    m.put("edad", mascota.getEdad() != null ? mascota.getEdad() : 0);
                    return m;
                })
                .toList();

        return Map.of(
                "ok", true,
                "mascotas", mascotas
        );
    }
}
