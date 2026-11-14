package com.gesvet.petfriends.repository;

import com.gesvet.petfriends.entity.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Integer> {
    Optional<Atencion> findByIdCita(Integer idCita);
    List<Atencion> findByIdMascotaOrderByFechaAtencionDesc(Integer idMascota);
}
