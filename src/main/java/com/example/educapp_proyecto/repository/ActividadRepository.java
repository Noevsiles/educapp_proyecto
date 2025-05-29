package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByPlanesTrabajo_Id(Long idPlan);

    @Query("SELECT a FROM Actividad a WHERE a.solucionAplicada.planTrabajo.id = :planId")
    List<Actividad> findByPlanTrabajoIdFromSoluciones(Long planId);

}
