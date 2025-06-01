package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Causa;
import com.example.educapp_proyecto.repository.CausaRepository;
import com.example.educapp_proyecto.service.CausaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CausaService implements CausaServiceInterface {

    @Autowired
    private CausaRepository causaRepository;

    @Override
    public List<Causa> findAll() {
        return causaRepository.findAll();
    }

    // Encontrar una causa por su id
    @Override
    public Causa findById(Long id) {
        Optional<Causa> causa = causaRepository.findById(id);
        if (causa.isPresent()) {
            return causa.get();
        } else {
            throw new RuntimeException("Causa no encontrada con el id: " + id);
        }
    }

    // Guardar una causa
    @Override
    public Causa save(Causa causa) {
        return causaRepository.save(causa);
    }

    @Override
    public void deleteById(Long id) {
        if (causaRepository.existsById(id)) {
            causaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, causa no encontrada con el id: " + id);
        }
    }

    // Actualizar una causa
    public Causa updateCausa(Long id, Causa causa) {
        if (causaRepository.existsById(id)) {
            causa.setIdCausa(id);
            return causaRepository.save(causa);
        } else {
            throw new RuntimeException("Causa no encontrada con el id: " + id);
        }
    }
}
