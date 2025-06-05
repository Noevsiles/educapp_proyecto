package com.example.educapp_proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="educador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Educador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_educador")
    private Long idEducador;

    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String especializacion;
    private int experiencia;
    private String formacion;
    private String descripcion;

    @OneToMany(mappedBy = "educador")
    @JsonIgnore
    @JsonManagedReference
    private List<Cliente> clientes;

    @OneToMany(mappedBy = "educador")
    @JsonIgnore
    private List<Tarifa> tarifas;

    @OneToMany(mappedBy = "educador")
    @JsonIgnore
    private List<Sesion> sesiones;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
