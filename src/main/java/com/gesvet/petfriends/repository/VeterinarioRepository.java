package com.gesvet.petfriends.repository;

import com.gesvet.petfriends.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Integer> {
    Optional<Veterinario> findByIdUsuario(Integer idUsuario);
    List<Veterinario> findByActivoTrue();
}
