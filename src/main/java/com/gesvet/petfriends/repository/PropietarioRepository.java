package com.gesvet.petfriends.repository;

import com.gesvet.petfriends.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropietarioRepository
        extends JpaRepository<Propietario, Integer> {

    Optional<Propietario> findByCedula(String cedula);
}
