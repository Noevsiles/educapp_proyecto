package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Perro;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.service.PerroServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerroService implements PerroServiceInterface {
    @Autowired
    private PerroRepository perroRepository;


    @Override
    public List<Perro> findAll() {
        return perroRepository.findAll();
    }

    @Override
    public Perro findById(Long id) {
        Optional<Perro> perro = perroRepository.findById(id);
        if (perro.isPresent()) {
            return perro.get();
        } else {
            throw new RuntimeException("Perro no encontrado con el id: " + id);
        }
    }

    @Override
    public Perro save(Perro perro) {
        return perroRepository.save(perro);
    }

    @Override
    public void deleteById(Long id) {
        if (perroRepository.existsById(id)) {
            perroRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, perro no encontrado con el id: " + id);
        }
    }

    // Actualizar un perro
    public Perro updatePerro(Long id, Perro perro) {
        if (perroRepository.existsById(id)) {
            perro.setIdPerro(id);  // Aseguramos que el ID es el mismo
            return perroRepository.save(perro);
        } else {
            throw new RuntimeException("Perro no encontrado con el id: " + id);
        }
    }
}
