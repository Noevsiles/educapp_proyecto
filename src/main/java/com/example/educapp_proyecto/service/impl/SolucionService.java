package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Solucion;
import com.example.educapp_proyecto.repository.SolucionRepository;
import com.example.educapp_proyecto.service.SolucionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolucionService implements SolucionServiceInterface {
    @Autowired
    private SolucionRepository solucionRepository;

    @Override
    public List<Solucion> findAll() {
        return solucionRepository.findAll();
    }

    @Override
    public Solucion findById(Long id) {
        Optional<Solucion> solucion = solucionRepository.findById(id);
        if (solucion.isPresent()) {
            return solucion.get();
        } else {
            throw new RuntimeException("Solución no encontrada con el id: " + id);
        }
    }

    @Override
    public Solucion save(Solucion solucion) {
        return solucionRepository.save(solucion);
    }

    @Override
    public void deleteById(Long id) {
        if (solucionRepository.existsById(id)) {
            solucionRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, solución no encontrada con el id: " + id);
        }
    }

    // Actualizar una solución
    public Solucion updateSolucion(Long id, Solucion solucion) {
        if (solucionRepository.existsById(id)) {
            solucion.setIdSolucion(id);  // Aseguramos que el ID es el mismo
            return solucionRepository.save(solucion);
        } else {
            throw new RuntimeException("Solución no encontrada con el id: " + id);
        }
    }
}
