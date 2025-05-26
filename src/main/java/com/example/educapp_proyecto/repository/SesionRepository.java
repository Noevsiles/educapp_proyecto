package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Sesion;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    @Query("SELECT s FROM Sesion s WHERE s.educador.idEducador = :educadorId AND s.fechaHora BETWEEN :inicio AND :fin")
    List<Sesion> buscarPorEducadorIdYFechaHoraEntre(
            @Param("educadorId") Long educadorId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    List<Sesion> findByPerro_Cliente_Educador(Educador educador);

    List<Sesion> findByEducador_IdEducador(Long idEducador);

    boolean existsByEducadorAndFechaHora(Educador educador, LocalDateTime fechaHora);




}
