package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Solucion;
import com.example.educapp_proyecto.repository.SolucionRepository;
import com.example.educapp_proyecto.service.SolucionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con las soluciones disponibles en el sistema.
 * Proporciona métodos CRUD para interactuar con el repositorio de soluciones.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class SolucionService implements SolucionServiceInterface {
    @Autowired
    private SolucionRepository solucionRepository;

    // Encontrar todas las soluciones
    /**
     * Recupera todas las soluciones registradas en la base de datos.
     *
     * @return lista de todas las soluciones
     */
    @Override
    public List<Solucion> findAll() {
        return solucionRepository.findAll();
    }

    // Encontrar solucion por su id
    /**
     * Busca una solución específica por su identificador.
     *
     * @param id identificador de la solución
     * @return solución encontrada
     * @throws RuntimeException si no se encuentra ninguna solución con ese ID
     */
    @Override
    public Solucion findById(Long id) {
        Optional<Solucion> solucion = solucionRepository.findById(id);
        if (solucion.isPresent()) {
            return solucion.get();
        } else {
            throw new RuntimeException("Solución no encontrada con el id: " + id);
        }
    }

    // Guardar soluciones
    /**
     * Guarda una nueva solución o actualiza una existente en la base de datos.
     *
     * @param solucion objeto solución a guardar
     * @return la solución guardada
     */
    @Override
    public Solucion save(Solucion solucion) {
        return solucionRepository.save(solucion);
    }

    // Borrar solucion por su id
    /**
     * Borra una solución de la base de datos por su identificador.
     *
     * @param id identificador de la solución a eliminar
     * @throws RuntimeException si no se encuentra la solución con el ID dado
     */
    @Override
    public void deleteById(Long id) {
        if (solucionRepository.existsById(id)) {
            solucionRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, solución no encontrada con el id: " + id);
        }
    }

    // Actualizar una solución
    /**
     * Actualiza los datos de una solución existente.
     *
     * @param id identificador de la solución a actualizar
     * @param solucion objeto con los nuevos datos de la solución
     * @return solución actualizada
     * @throws RuntimeException si no se encuentra la solución con el ID proporcionado
     */
    public Solucion updateSolucion(Long id, Solucion solucion) {
        if (solucionRepository.existsById(id)) {
            solucion.setIdSolucion(id);  // Aseguramos que el ID es el mismo
            return solucionRepository.save(solucion);
        } else {
            throw new RuntimeException("Solución no encontrada con el id: " + id);
        }
    }
}
