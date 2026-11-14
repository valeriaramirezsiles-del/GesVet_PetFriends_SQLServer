package com.gesvet.petfriends.service;

import com.gesvet.petfriends.entity.Usuario;
import com.gesvet.petfriends.entity.Veterinario;
import com.gesvet.petfriends.repository.UsuarioRepository;
import com.gesvet.petfriends.repository.VeterinarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${gesvet.admin.username:admin_demo}")
    private String adminUsername;

    @Value("${gesvet.admin.password:DemoAdmin2026!}")
    private String adminPassword;

    @Value("${gesvet.vet.username:veterinario_demo}")
    private String vetUsername;

    @Value("${gesvet.vet.password:DemoVet2026!}")
    private String vetPassword;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          VeterinarioRepository veterinarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void inicializarUsuarios() {
        // Asegurar que existan los usuarios en la base de datos
        crearSiNoExiste(adminUsername, adminPassword, "ADMIN", "Administrador Principal", null, null);
        crearSiNoExiste(vetUsername, vetPassword, "VETERINARIO", "Dr. Carlos Veterinario", "Medicina General y Cirugía", "8888-0001");
        
        // Usuarios estándar adicionales para facilidad de prueba
        crearSiNoExiste("admin", "admin123", "ADMIN", "Administrador del Sistema", null, null);
        crearSiNoExiste("veterinario", "vet123", "VETERINARIO", "Dra. Sofía Mendoza", "Dermatología y Nutrición", "8888-0002");
    }

    private void crearSiNoExiste(String username, String rawPassword, String rol, String nombreCompleto, String especialidad, String telefono) {
        if (!usuarioRepository.existsByNombreUsuario(username)) {
            Usuario u = new Usuario();
            u.setNombreUsuario(username);
            u.setClaveHash(passwordEncoder.encode(rawPassword));
            u.setRol(rol);
            u.setActivo(true);
            u = usuarioRepository.save(u);

            if ("VETERINARIO".equalsIgnoreCase(rol)) {
                if (veterinarioRepository.findByIdUsuario(u.getIdUsuario()).isEmpty()) {
                    Veterinario v = new Veterinario(u.getIdUsuario(), nombreCompleto, especialidad, telefono);
                    veterinarioRepository.save(v);
                }
            }
        }
    }

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }
}
