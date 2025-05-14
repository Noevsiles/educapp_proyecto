package com.example.educapp_proyecto.model;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="tarifa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_tarifa")
    private Long idTarifa;

    private String nombre;
    private Double precio;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "educador_id")
    private Educador educador;

}
