package com.example.educapp_proyecto.repository;

import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.PlanTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanTrabajoRepository extends JpaRepository<PlanTrabajo, Long> {
    @Query("SELECT DISTINCT pt FROM PlanTrabajo pt " +
            "LEFT JOIN FETCH pt.actividades " +
            "LEFT JOIN FETCH pt.problemas " +
            "LEFT JOIN FETCH pt.cliente c " +
            "LEFT JOIN FETCH c.educador " +
            "WHERE pt.cliente.idCliente = :clienteId")
    List<PlanTrabajo> findByClienteIdWithRelations(@Param("clienteId") Long clienteId);


    List<PlanTrabajo> findByPerro_IdPerro(Long idPerro);

    List<PlanTrabajo> findByPerro_Cliente(Cliente cliente);
}
