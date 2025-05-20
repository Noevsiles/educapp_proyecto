package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.service.impl.InformePdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InformePdfServiceTest {

    @Mock
    private PerroRepository perroRepository;

    @InjectMocks
    private InformePdfService informePdfService;

    private Perro perro;

    @BeforeEach
    void setUp() {
        // Cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Ana");
        cliente.setApellidos("García");
        cliente.setEmail("ana@example.com");
        cliente.setTelefono("123456789");

        // Causa
        Causa causa = new Causa();
        causa.setNombre("Aburrimiento");
        causa.setDescripcion("El perro se aburre fácilmente");

        CausaDeProblema causaDeProblema = new CausaDeProblema();
        causaDeProblema.setCausa(causa);

        // Solucion
        Solucion solucion = new Solucion();
        solucion.setNombre("Juegos mentales");
        solucion.setDescripcion("Actividades para estimular la mente del perro");

        SolucionAplicada solucionAplicada = new SolucionAplicada();
        solucionAplicada.setSolucion(solucion);

        // Problema
        ProblemaDeConducta problema = new ProblemaDeConducta();
        problema.setNombre("Ladrido excesivo");
        problema.setDescripcion("Ladra mucho cuando está solo");
        problema.setCausaDeProblemas(Collections.singletonList(causaDeProblema));
        problema.setSolucionAplicadas(Collections.singletonList(solucionAplicada));

        // Perro
        perro = new Perro();
        perro.setNombre("Rocky");
        perro.setRaza("Labrador");
        perro.setEdad(3);
        perro.setSexo("Macho");
        perro.setCliente(cliente);
        perro.setProblemasDeConducta(Collections.singletonList(problema));
    }

    // Test para generar un informe en pdf por perro
    @Test
    void testGenerarInformePorPerro() throws Exception {
        when(perroRepository.findById(1L)).thenReturn(Optional.of(perro));

        byte[] pdf = informePdfService.generarInformePorPerro(1L);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);

        verify(perroRepository).findById(1L);
    }

    // Test generacion de informe con perro que no existe (excepcion)
    @Test
    void testGenerarInformePerroNoExiste() {
        when(perroRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                informePdfService.generarInformePorPerro(99L));

        assertEquals("Perro no encontrado", ex.getMessage());
    }
}
