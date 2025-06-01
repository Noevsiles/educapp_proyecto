package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.CausaDeProblema;
import com.example.educapp_proyecto.repository.CausaDeProblemaRepository;
import com.example.educapp_proyecto.service.CausaDeProblemaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CausaDeProblemaService implements CausaDeProblemaServiceInterface {
    @Autowired
    private CausaDeProblemaRepository causaDeProblemaRepository;

    @Override
    public List<CausaDeProblema> findAll() {
        return causaDeProblemaRepository.findAll();
    }

    // Encontrar una causa de problema por su id
    @Override
    public CausaDeProblema findById(Long id) {
        Optional<CausaDeProblema> causaDeProblema = causaDeProblemaRepository.findById(id);
        if (causaDeProblema.isPresent()) {
            return causaDeProblema.get();
        } else {
            throw new RuntimeException("Causa de problema no encontrada con el id: " + id);
        }
    }

    // Guardar una causa de problema
    @Override
    public CausaDeProblema save(CausaDeProblema causaDeProblema) {
        return causaDeProblemaRepository.save(causaDeProblema);
    }

    // Eliminar una causa de problema por su id
    @Override
    public void deleteById(Long id) {
        if (causaDeProblemaRepository.existsById(id)) {
            causaDeProblemaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, causa de problema no encontrada con el id: " + id);
        }
    }

    // Actualizar una causa de problema
    public CausaDeProblema updateCausaDeProblema(Long id, CausaDeProblema causaDeProblema) {
        if (causaDeProblemaRepository.existsById(id)) {
            causaDeProblema.setIdCausaDeProblema(id);  // Aseguramos que el ID es el mismo
            return causaDeProblemaRepository.save(causaDeProblema);
        } else {
            throw new RuntimeException("Causa de problema no encontrada con el id: " + id);
        }
    }
}
