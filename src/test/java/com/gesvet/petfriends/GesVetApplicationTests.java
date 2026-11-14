package com.gesvet.petfriends;

import com.gesvet.petfriends.service.CitaService;
import com.gesvet.petfriends.service.DashboardService;
import com.gesvet.petfriends.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GesVetApplicationTests {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void contextLoads() {
        assertNotNull(dashboardService, "DashboardService debe estar inicializado");
        assertNotNull(citaService, "CitaService debe estar inicializado");
        assertNotNull(usuarioService, "UsuarioService debe estar inicializado");
    }

    @Test
    void testDashboardStats() {
        var stats = dashboardService.obtenerEstadisticas();
        assertNotNull(stats, "Las estadísticas no deben ser nulas");
        assertTrue(stats.containsKey("totalMascotas"));
        assertTrue(stats.containsKey("totalCitas"));
        assertTrue(stats.containsKey("citasPendientes"));
    }

    @Test
    void testUsuariosSincronizados() {
        assertTrue(usuarioService.buscarPorNombreUsuario("admin_demo").isPresent(), "El usuario admin_demo debe existir en la BD");
        assertTrue(usuarioService.buscarPorNombreUsuario("veterinario_demo").isPresent(), "El usuario veterinario_demo debe existir en la BD");
    }
}
