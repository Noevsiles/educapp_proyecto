package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByPlanesTrabajo_Id(Long idPlan);

}
