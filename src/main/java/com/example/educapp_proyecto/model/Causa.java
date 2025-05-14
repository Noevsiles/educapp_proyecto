package com.example.educapp_proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name="causa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Causa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_causa")
    private Long idCausa;

    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "causa")
    private List<CausaDeProblema> causaDeProblemas;

}
