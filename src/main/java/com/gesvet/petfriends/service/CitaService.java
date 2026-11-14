package com.gesvet.petfriends.service;

import com.gesvet.petfriends.dto.CitaDetalleDto;
import com.gesvet.petfriends.entity.*;
import com.gesvet.petfriends.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final PropietarioRepository propietarioRepository;
    private final ServicioRepository servicioRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final AtencionRepository atencionRepository;

    public CitaService(CitaRepository citaRepository,
                       MascotaRepository mascotaRepository,
                       PropietarioRepository propietarioRepository,
                       ServicioRepository servicioRepository,
                       VeterinarioRepository veterinarioRepository,
                       AtencionRepository atencionRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.propietarioRepository = propietarioRepository;
        this.servicioRepository = servicioRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.atencionRepository = atencionRepository;
    }

    public List<CitaDetalleDto> obtenerTodasLasCitas() {
        return citaRepository.findAllByOrderByFechaDescHoraDesc()
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    public List<CitaDetalleDto> obtenerCitasPendientes() {
        return citaRepository.findByEstadoOrderByFechaAscHoraAsc("Pendiente")
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    @Transactional
    public Cita guardarCita(Cita cita) {
        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado("Pendiente");
        }
        return citaRepository.save(cita);
    }

    @Transactional
    public boolean actualizarEstado(Integer idCita, String nuevoEstado) {
        Optional<Cita> opt = citaRepository.findById(idCita);
        if (opt.isEmpty()) return false;

        Cita cita = opt.get();
        cita.setEstado(nuevoEstado);
        citaRepository.save(cita);
        return true;
    }

    @Transactional
    public boolean asignarVeterinario(Integer idCita, Integer idVeterinario) {
        Optional<Cita> opt = citaRepository.findById(idCita);
        if (opt.isEmpty()) return false;

        Cita cita = opt.get();
        cita.setIdVeterinario(idVeterinario);
        citaRepository.save(cita);
        return true;
    }

    private CitaDetalleDto convertirADto(Cita c) {
        Mascota mascota = mascotaRepository.findById(c.getIdMascota()).orElse(null);
        Propietario prop = null;
        if (mascota != null && mascota.getIdPropietario() != null) {
            prop = propietarioRepository.findById(mascota.getIdPropietario()).orElse(null);
        }

        Servicio serv = servicioRepository.findById(c.getIdServicio()).orElse(null);
        Veterinario vet = c.getIdVeterinario() != null
                ? veterinarioRepository.findById(c.getIdVeterinario()).orElse(null)
                : null;

        boolean tieneAtencion = atencionRepository.findByIdCita(c.getIdCita()).isPresent();

        return new CitaDetalleDto(
                c.getIdCita(),
                c.getFecha(),
                c.getHora(),
                c.getEstado(),
                c.getObservaciones(),
                mascota != null ? mascota.getIdMascota() : c.getIdMascota(),
                mascota != null ? mascota.getNombre() : "Mascota #" + c.getIdMascota(),
                mascota != null ? mascota.getTipo() : "-",
                mascota != null && mascota.getRaza() != null ? mascota.getRaza() : "-",
                prop != null ? prop.getIdPropietario() : null,
                prop != null ? prop.getNombre() : "No registrado",
                prop != null ? prop.getCedula() : "-",
                prop != null ? prop.getTelefono() : "-",
                serv != null ? serv.getIdServicio() : c.getIdServicio(),
                serv != null ? serv.getNombre() : "Servicio #" + c.getIdServicio(),
                vet != null ? vet.getIdVeterinario() : null,
                vet != null ? vet.getNombre() : "Sin asignar",
                tieneAtencion
        );
    }
}
