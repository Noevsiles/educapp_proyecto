package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.dto.HuecoAgendaCompletoDto;
import com.example.educapp_proyecto.dto.ReservaSesionDto;
import com.example.educapp_proyecto.dto.SesionRequestDto;
import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.*;
import com.example.educapp_proyecto.service.impl.RecordatorioService;
import com.example.educapp_proyecto.service.impl.SesionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SesionServiceTest {
    @InjectMocks
    private SesionService sesionService;

    @Mock
    private SesionRepository sesionRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EducadorRepository educadorRepository;

    @Mock
    private PerroRepository perroRepository;

    @Mock
    private PlanTrabajoRepository planTrabajoRepository;

    @Mock
    private DisponibilidadRepository disponibilidadRepository;

    @Mock
    private RecordatorioService recordatorioService;

    private Cliente cliente;
    private Educador educador;
    private Perro perro;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNombre("Cliente Test");

        educador = new Educador();
        educador.setIdEducador(2L);
        educador.setNombre("Edu Test");

        perro = new Perro();
        perro.setIdPerro(3L);
        perro.setNombre("Firulais");

        PlanTrabajo plan = new PlanTrabajo();
        setId(plan, 4L);
        plan.setCliente(cliente);
    }

    private void setId(Object entity, Long idValue) throws Exception {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getName().startsWith("id")) {
                field.setAccessible(true);
                field.set(entity, idValue);
                return;
            }
        }
        throw new RuntimeException("No se encontró ningún campo que empiece por 'id' en " + entity.getClass().getSimpleName());
    }

    //Test para comprobar que se puede crear una sesion
    @Test
    void testCrearSesion_SinSolapamiento() throws Exception {
        // DTO de entrada
        SesionRequestDto dto = new SesionRequestDto();
        dto.setClienteId(1L);
        dto.setEducadorId(2L);
        dto.setPerroId(3L);
        dto.setPlanTrabajoId(4L);
        dto.setFechaHora(LocalDateTime.of(2025, 6, 1, 10, 0));
        dto.setTipoSesion("evaluación");

        // Mocks de entidades relacionadas
        Cliente cliente = new Cliente();
        setId(cliente, 1L);

        Educador educador = new Educador();
        setId(educador, 2L);

        Perro perro = new Perro();
        setId(perro, 3L);

        PlanTrabajo plan = new PlanTrabajo();
        setId(plan, 4L);
        plan.setCliente(cliente);

        // Mocks de repositorios
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(educadorRepository.findById(2L)).thenReturn(Optional.of(educador));
        when(perroRepository.findById(3L)).thenReturn(Optional.of(perro));
        when(planTrabajoRepository.findById(4L)).thenReturn(Optional.of(plan));

        when(sesionRepository.save(any(Sesion.class))).thenAnswer(invocation -> {
            Sesion sesion = invocation.getArgument(0);
            setId(sesion, 100L);
            return sesion;
        });

        // Llamada al servicio
        Sesion resultado = sesionService.crearSesion(dto);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(100L, resultado.getIdSesion());
        assertEquals("evaluación", resultado.getTipoSesion());

        verify(sesionRepository, times(1)).save(any(Sesion.class));
    }

    // Test para comprobar que si ya hay una sesion para esa hora, lanzara una excepcion
    @Test
    void testReservarSesion_ConSolapamiento() {
        ReservaSesionDto reserva = new ReservaSesionDto();
        reserva.setIdEducador(1L);
        reserva.setFechaHora(LocalDateTime.now().plusDays(1));
        reserva.setIdCliente(1L);
        reserva.setIdPerro(1L);
        reserva.setIdPlanTrabajo(1L);

        when(sesionRepository.buscarPorEducadorIdYFechaHoraEntre(
                anyLong(), any(), any()
        )).thenReturn(List.of(new Sesion())); // Ya hay sesión en ese horario

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            sesionService.reservarSesion(reserva, "educador@correo.com");
        });

        assertEquals("Ese horario ya está reservado.", ex.getMessage());

        verify(sesionRepository).buscarPorEducadorIdYFechaHoraEntre(anyLong(), any(), any());
    }

    // Test para comprobar que funciona el metodo marcarComoRealizada()
    @Test
    void testMarcarComoRealizada() {
        Sesion sesion = new Sesion();
        sesion.setIdSesion(1L);
        sesion.setRealizada(false);

        when(sesionRepository.findById(1L)).thenReturn(Optional.of(sesion));
        when(sesionRepository.save(any(Sesion.class))).thenAnswer(inv -> inv.getArgument(0));

        Sesion resultado = sesionService.marcarComoRealizada(1L);

        assertTrue(resultado.isRealizada());
        verify(sesionRepository).save(sesion);
    }

    // Test para comprobar si marcarComoRealizada lanza excepcion si no la encuentra
    @Test
    void testMarcarComoRealizada_SesionNoEncontrada() {
        when(sesionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            sesionService.marcarComoRealizada(99L);
        });
    }

    // Test para filtrarSesionesPorCliente
    @Test
    void testFiltrarSesionesPorCliente() {
        Cliente cliente1 = new Cliente();
        cliente1.setIdCliente(1L);
        Cliente cliente2 = new Cliente();
        cliente2.setIdCliente(2L);

        Sesion s1 = new Sesion();
        s1.setCliente(cliente1);
        Sesion s2 = new Sesion();
        s2.setCliente(cliente2);

        when(sesionRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Sesion> resultado = sesionService.filtrarSesiones(1L, null, null);

        assertEquals(1, resultado.size());
        assertEquals(cliente1, resultado.get(0).getCliente());
    }

    // Test para filtrarSesionesPorPerroYEducador
    @Test
    void testFiltrarSesionesPorPerroYEducador() {
        Perro perro1 = new Perro();
        perro1.setIdPerro(1L);
        Educador educador1 = new Educador();
        educador1.setIdEducador(10L);

        Perro perro2 = new Perro();
        perro2.setIdPerro(2L);
        Educador educador2 = new Educador();
        educador2.setIdEducador(20L);

        Sesion s1 = new Sesion();
        s1.setPerro(perro1);
        s1.setEducador(educador1);

        Sesion s2 = new Sesion();
        s2.setPerro(perro2);
        s2.setEducador(educador2);

        Sesion s3 = new Sesion();
        s3.setPerro(perro1);
        s3.setEducador(educador2);

        when(sesionRepository.findAll()).thenReturn(List.of(s1, s2, s3));

        List<Sesion> resultado = sesionService.filtrarSesiones(null, 1L, 10L);

        assertEquals(1, resultado.size());
        assertEquals(perro1, resultado.get(0).getPerro());
        assertEquals(educador1, resultado.get(0).getEducador());
    }

    // Test para obtener la agenda completa con disponibilidad
    @Test
    void testObtenerAgendaCompleta_conDisponibilidadYUnaSesionOcupada() {
        Long idEducador = 1L;
        LocalDate fecha = LocalDate.of(2025, 5, 20); // por ejemplo un martes

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setHoraInicio(LocalTime.of(9, 0));
        disponibilidad.setHoraFin(LocalTime.of(12, 0));

        when(disponibilidadRepository.buscarPorEducadorYDia(idEducador, fecha.getDayOfWeek()))
                .thenReturn(List.of(disponibilidad));

        Sesion sesionOcupada = new Sesion();
        sesionOcupada.setFechaHora(LocalDateTime.of(fecha, LocalTime.of(10, 0)));

        when(sesionRepository.buscarPorEducadorIdYFechaHoraEntre(
                eq(idEducador),
                eq(LocalDateTime.of(fecha, LocalTime.of(9, 0))),
                eq(LocalDateTime.of(fecha, LocalTime.of(12, 0)))
        )).thenReturn(List.of(sesionOcupada));

        List<HuecoAgendaCompletoDto> huecos = sesionService.obtenerAgendaCompleta(idEducador, fecha);

        assertEquals(3, huecos.size());
        assertFalse(huecos.get(0).isOcupado()); // 09:00
        assertTrue(huecos.get(1).isOcupado());  // 10:00
        assertFalse(huecos.get(2).isOcupado()); // 11:00
    }

    // Test para comprobar si marca la sesion como realizada
    @Test
    void testMarcarSesionComoRealizada_correctamente() {
        Long idSesion = 1L;

        Sesion sesion = new Sesion();
        sesion.setIdSesion(idSesion);
        sesion.setRealizada(false);
        sesion.setFechaHora(LocalDateTime.now());

        when(sesionRepository.findById(idSesion)).thenReturn(Optional.of(sesion));
        when(sesionRepository.save(any(Sesion.class))).thenAnswer(inv -> inv.getArgument(0));

        Sesion resultado = sesionService.marcarComoRealizada(idSesion);

        assertTrue(resultado.isRealizada());
        verify(sesionRepository).save(sesion);
    }

    // Test para si la sesion no existe
    @Test
    void testMarcarSesionComoRealizada_sesionNoExiste() {
        Long idSesion = 99L;

        when(sesionRepository.findById(idSesion)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            sesionService.marcarComoRealizada(idSesion);
        });

        assertEquals("Sesión no encontrada", ex.getMessage());
    }

    // Test de filtrar sesiones por cliente, perro y educador
    @Test
    void testFiltrarSesiones_porClienteYPerroYeducador() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Perro perro = new Perro();
        perro.setIdPerro(2L);

        Educador educador = new Educador();
        educador.setIdEducador(3L);

        Sesion sesion = new Sesion();
        sesion.setCliente(cliente);
        sesion.setPerro(perro);
        sesion.setEducador(educador);

        List<Sesion> sesiones = List.of(sesion);

        when(sesionRepository.findAll()).thenReturn(sesiones);

        List<Sesion> resultado = sesionService.filtrarSesiones(1L, 2L, 3L);

        assertEquals(1, resultado.size());
        assertSame(sesion, resultado.get(0));
    }

    // Con parametros nulos no filtra
    @Test
    void testFiltrarSesiones_parametrosNulos_noFiltra() {
        Sesion sesion1 = new Sesion();
        Sesion sesion2 = new Sesion();

        when(sesionRepository.findAll()).thenReturn(List.of(sesion1, sesion2));

        List<Sesion> resultado = sesionService.filtrarSesiones(null, null, null);

        assertEquals(2, resultado.size());
    }

    // Los parametros no coinciden
    @Test
    void testFiltrarSesiones_noCoincide() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Sesion sesion = new Sesion();
        sesion.setCliente(cliente);

        when(sesionRepository.findAll()).thenReturn(List.of(sesion));

        List<Sesion> resultado = sesionService.filtrarSesiones(99L, null, null);

        assertTrue(resultado.isEmpty());
    }

    // Test para comprobar si se envian los recordatorios de las sesiones
    @Test
    void testEnviarRecordatorios_soloDentroDe24Horas() {
        // Sesión valida no realizada y dentro de las proximas 24 horas
        Sesion sesionValida = new Sesion();
        sesionValida.setFechaHora(LocalDateTime.now().plusHours(2));
        sesionValida.setRealizada(false);

        // Sesion fuera del rango de 24h
        Sesion sesionFueraRango = new Sesion();
        sesionFueraRango.setFechaHora(LocalDateTime.now().plusHours(30));
        sesionFueraRango.setRealizada(false);

        // Sesion ya realizada
        Sesion sesionYaRealizada = new Sesion();
        sesionYaRealizada.setFechaHora(LocalDateTime.now().plusHours(1));
        sesionYaRealizada.setRealizada(true);

        when(sesionRepository.findAll()).thenReturn(List.of(sesionValida, sesionFueraRango, sesionYaRealizada));

        sesionService.enviarRecordatorios();

        verify(recordatorioService, times(1)).enviarRecordatorio(sesionValida);
        verify(recordatorioService, never()).enviarRecordatorio(sesionFueraRango);
        verify(recordatorioService, never()).enviarRecordatorio(sesionYaRealizada);
    }
}
