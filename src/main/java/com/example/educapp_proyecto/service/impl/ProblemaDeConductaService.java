package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.ProblemaDeConducta;
import com.example.educapp_proyecto.repository.ProblemaDeConductaRepository;
import com.example.educapp_proyecto.service.ProblemaDeConductaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemaDeConductaService implements ProblemaDeConductaServiceInterface {
    @Autowired
    private ProblemaDeConductaRepository problemaDeConductaRepository;


    @Override
    public List<ProblemaDeConducta> findAll() {
        return problemaDeConductaRepository.findAll();
    }

    @Override
    public ProblemaDeConducta findById(Long id) {
        Optional<ProblemaDeConducta> problemaDeConducta = problemaDeConductaRepository.findById(id);
        if (problemaDeConducta.isPresent()) {
            return problemaDeConducta.get();
        } else {
            throw new RuntimeException("Problema de conducta no encontrado con el id: " + id);
        }
    }

    @Override
    public ProblemaDeConducta save(ProblemaDeConducta problemaDeConducta) {
        return problemaDeConductaRepository.save(problemaDeConducta);
    }

    @Override
    public void deleteById(Long id) {
        if (problemaDeConductaRepository.existsById(id)) {
            problemaDeConductaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, problema de conducta no encontrado con el id: " + id);
        }
    }

    // Actualizar un problema de conducta
    public ProblemaDeConducta updateProblemaDeConducta(Long id, ProblemaDeConducta problemaDeConducta) {
        if (problemaDeConductaRepository.existsById(id)) {
            problemaDeConducta.setIdProblema(id);
            return problemaDeConductaRepository.save(problemaDeConducta);
        } else {
            throw new RuntimeException("Problema de conducta no encontrado con el id: " + id);
        }
    }
}
