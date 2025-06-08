package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.SolucionAplicada;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.repository.SolucionAplicadaRepository;
import com.example.educapp_proyecto.service.SolucionAplicadaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las soluciones aplicadas
 * y sus actividades asociadas en el sistema EducApp.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class SolucionAplicadaService implements SolucionAplicadaServiceInterface {
    @Autowired
    private SolucionAplicadaRepository solucionAplicadaRepository;

    private ActividadRepository actividadRepository;

    // Encontrar todas las soluciones aplicadas
    /**
     * Recupera todas las soluciones aplicadas registradas en el sistema.
     *
     * @return lista de soluciones aplicadas
     */
    @Override
    public List<SolucionAplicada> findAll() {
        return solucionAplicadaRepository.findAll();
    }

    // Encontrar solucion aplicada por id
    /**
     * Busca una solución aplicada por su identificador.
     *
     * @param id identificador de la solución aplicada
     * @return solución aplicada encontrada
     * @throws RuntimeException si no se encuentra la solución con el ID dado
     */
    @Override
    public SolucionAplicada findById(Long id) {
        Optional<SolucionAplicada> solucionAplicada = solucionAplicadaRepository.findById(id);
        if (solucionAplicada.isPresent()) {
            return solucionAplicada.get();
        } else {
            throw new RuntimeException("Solución aplicada no encontrada con el id: " + id);
        }
    }

    // Guardar solucion aplicada
    /**
     * Guarda una nueva solución aplicada en la base de datos.
     *
     * @param solucionAplicada solución a guardar
     * @return solución aplicada guardada
     */
    @Override
    public SolucionAplicada save(SolucionAplicada solucionAplicada) {
        return solucionAplicadaRepository.save(solucionAplicada);
    }

    // Eliminar solucion aplicada por id
    /**
     * Elimina una solución aplicada por su identificador.
     *
     * @param id identificador de la solución a eliminar
     * @throws RuntimeException si no se encuentra la solución con el ID proporcionado
     */
    @Override
    public void deleteById(Long id) {
        if (solucionAplicadaRepository.existsById(id)) {
            solucionAplicadaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, solución aplicada no encontrada con el id: " + id);
        }
    }

    // Actualizar una solución aplicada
    /**
     * Actualiza los datos de una solución aplicada existente.
     *
     * @param id identificador de la solución a actualizar
     * @param solucionAplicada nueva información de la solución
     * @return solución aplicada actualizada
     * @throws RuntimeException si no se encuentra la solución con el ID dado
     */
    public SolucionAplicada updateSolucionAplicada(Long id, SolucionAplicada solucionAplicada) {
        if (solucionAplicadaRepository.existsById(id)) {
            solucionAplicada.setIdSolucionAplicada(id);
            return solucionAplicadaRepository.save(solucionAplicada);
        } else {
            throw new RuntimeException("Solución aplicada no encontrada con el id: " + id);
        }
    }

    // Agregar una actividad a una solución aplicada
    /**
     * Asocia una actividad a una solución aplicada concreta.
     *
     * @param solucionAplicadaId ID de la solución aplicada
     * @param actividad actividad a agregar
     * @return actividad guardada y asociada
     * @throws RuntimeException si no se encuentra la solución aplicada
     */
    @Override
    public Actividad agregarActividad(Long solucionAplicadaId, Actividad actividad) {
        SolucionAplicada solucionAplicada = solucionAplicadaRepository.findById(solucionAplicadaId)
                .orElseThrow(() -> new RuntimeException("Solución aplicada no encontrada con el id: " + solucionAplicadaId));

        actividad.setSolucionAplicada(solucionAplicada);
        return actividadRepository.save(actividad);
    }

    // Actualizar el progreso de una actividad
    /**
     * Actualiza el estado de completado de una actividad concreta.
     *
     * @param actividadProgresoId ID de la actividad a actualizar
     * @param completado nuevo estado de la actividad (completada o no)
     * @throws RuntimeException si no se encuentra la actividad
     */
    @Override
    public void actualizarProgreso(Long actividadProgresoId, boolean completado) {
        Actividad actividadProgreso = actividadRepository.findById(actividadProgresoId)
                .orElseThrow(() -> new RuntimeException("Progreso de actividad no encontrado con el id: " + actividadProgresoId));
        actividadProgreso.setCompletado(completado);
        actividadRepository.save(actividadProgreso);
    }
}
