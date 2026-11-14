package com.gesvet.petfriends.service;

import com.gesvet.petfriends.repository.CitaRepository;
import com.gesvet.petfriends.repository.MascotaRepository;
import com.gesvet.petfriends.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class DashboardService {

    private final MascotaRepository mascotaRepository;
    private final PropietarioRepository propietarioRepository;
    private final CitaRepository citaRepository;

    public DashboardService(MascotaRepository mascotaRepository,
                            PropietarioRepository propietarioRepository,
                            CitaRepository citaRepository) {
        this.mascotaRepository = mascotaRepository;
        this.propietarioRepository = propietarioRepository;
        this.citaRepository = citaRepository;
    }

    public Map<String, Object> obtenerEstadisticas() {
        long totalMascotas = mascotaRepository.count();
        long totalPropietarios = propietarioRepository.count();
        long totalCitas = citaRepository.count();
        long pendientes = citaRepository.countByEstado("Pendiente");
        long atendidas = citaRepository.countByEstado("Atendida");
        long hoy = citaRepository.findByFecha(LocalDate.now()).size();

        return Map.of(
                "totalMascotas", totalMascotas,
                "totalPropietarios", totalPropietarios,
                "totalCitas", totalCitas,
                "citasPendientes", pendientes,
                "citasAtendidas", atendidas,
                "citasHoy", hoy
        );
    }
}
