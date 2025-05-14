package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.service.EducadorServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducadorService implements EducadorServiceInterface {
    @Autowired
    private EducadorRepository educadorRepository;


    @Override
    public List<Educador> findAll() {
        return educadorRepository.findAll();
    }

    @Override
    public Educador findById(Long id) {
        Optional<Educador> educador = educadorRepository.findById(id);
        if (educador.isPresent()) {
            return educador.get();
        } else {
            throw new RuntimeException("Educador no encontrado con el id: " + id);
        }
    }

    @Override
    public Educador save(Educador educador) {
        return educadorRepository.save(educador);
    }

    @Override
    public void deleteById(Long id) {
        if (educadorRepository.existsById(id)) {
            educadorRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, educador no encontrado con el id: " + id);
        }
    }

    // Actualizar un educador
    public Educador updateEducador(Long id, Educador educador) {
        if (educadorRepository.existsById(id)) {
            educador.setIdEducador(id);  // Aseguramos que el ID es el mismo
            return educadorRepository.save(educador);
        } else {
            throw new RuntimeException("Educador no encontrado con el id: " + id);
        }
    }
}
