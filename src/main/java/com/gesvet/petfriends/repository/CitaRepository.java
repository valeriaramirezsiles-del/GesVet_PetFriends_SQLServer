package com.gesvet.petfriends.repository;

import com.gesvet.petfriends.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findAllByOrderByFechaDescHoraDesc();
    List<Cita> findByEstadoOrderByFechaAscHoraAsc(String estado);
    List<Cita> findByFecha(LocalDate fecha);
    long countByEstado(String estado);
}
