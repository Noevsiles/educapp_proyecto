package com.example.educapp_proyecto.repository;

import com.example.educapp_proyecto.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    @Query("SELECT d FROM Disponibilidad d WHERE d.educador.idEducador = :educadorId AND d.diaSemana = :diaSemana")
    List<Disponibilidad> buscarPorEducadorYDia(@Param("educadorId") Long educadorId, @Param("diaSemana") DayOfWeek diaSemana);

}
