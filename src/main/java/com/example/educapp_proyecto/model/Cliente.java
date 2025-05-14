package com.example.educapp_proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cliente")
    private Long idCliente;

    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "educador_id")
    private Educador educador;

    @OneToMany(mappedBy = "cliente")
    private List<Perro> perros;
}
