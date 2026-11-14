package com.gesvet.petfriends.service;

import com.gesvet.petfriends.entity.Atencion;
import com.gesvet.petfriends.entity.Cita;
import com.gesvet.petfriends.repository.AtencionRepository;
import com.gesvet.petfriends.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final CitaRepository citaRepository;

    public AtencionService(AtencionRepository atencionRepository, CitaRepository citaRepository) {
        this.atencionRepository = atencionRepository;
        this.citaRepository = citaRepository;
    }

    public Optional<Atencion> obtenerPorCita(Integer idCita) {
        return atencionRepository.findByIdCita(idCita);
    }

    @Transactional
    public Atencion registrarAtencion(Integer idCita, Integer idMascota, String diagnostico, String tratamiento, String observaciones) {
        Optional<Atencion> existente = atencionRepository.findByIdCita(idCita);
        Atencion atencion;
        if (existente.isPresent()) {
            atencion = existente.get();
            atencion.setDiagnostico(diagnostico);
            atencion.setTratamiento(tratamiento);
            atencion.setObservaciones(observaciones);
        } else {
            atencion = new Atencion(idCita, idMascota, diagnostico, tratamiento, observaciones);
        }
        atencion = atencionRepository.save(atencion);

        // Actualizar el estado de la cita a "Atendida"
        citaRepository.findById(idCita).ifPresent(c -> {
            c.setEstado("Atendida");
            citaRepository.save(c);
        });

        return atencion;
    }
}
