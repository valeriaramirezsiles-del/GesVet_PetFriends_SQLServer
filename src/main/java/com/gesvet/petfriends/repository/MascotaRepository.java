package com.gesvet.petfriends.repository;

import com.gesvet.petfriends.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository
        extends JpaRepository<Mascota, Integer> {

    List<Mascota> findByIdPropietario(Integer idPropietario);
}