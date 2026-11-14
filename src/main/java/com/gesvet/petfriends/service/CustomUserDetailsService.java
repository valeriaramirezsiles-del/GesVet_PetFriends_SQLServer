package com.gesvet.petfriends.service;

import com.gesvet.petfriends.entity.Usuario;
import com.gesvet.petfriends.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${gesvet.admin.username:admin_demo}")
    private String adminUsername;

    @Value("${gesvet.admin.password:DemoAdmin2026!}")
    private String adminPassword;

    @Value("${gesvet.vet.username:veterinario_demo}")
    private String vetUsername;

    @Value("${gesvet.vet.password:DemoVet2026!}")
    private String vetPassword;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscar en la base de datos
        Optional<Usuario> opt = usuarioRepository.findByNombreUsuario(username);
        if (opt.isPresent()) {
            Usuario u = opt.get();
            if (Boolean.FALSE.equals(u.getActivo())) {
                throw new UsernameNotFoundException("Usuario inactivo: " + username);
            }
            String roleName = u.getRol().startsWith("ROLE_") ? u.getRol() : "ROLE_" + u.getRol().toUpperCase();
            return new User(
                    u.getNombreUsuario(),
                    u.getClaveHash(),
                    List.of(new SimpleGrantedAuthority(roleName))
            );
        }

        // 2. Fallback de usuarios demo si no existen en la BD todavía
        if (adminUsername.equalsIgnoreCase(username)) {
            return User.withUsername(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles("ADMIN")
                    .build();
        }

        if (vetUsername.equalsIgnoreCase(username)) {
            return User.withUsername(vetUsername)
                    .password(passwordEncoder.encode(vetPassword))
                    .roles("VETERINARIO")
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
