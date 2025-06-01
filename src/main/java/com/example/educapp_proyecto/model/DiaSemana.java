package com.example.educapp_proyecto.model;

public enum DiaSemana {
    LUNES(1), MARTES(2), MIERCOLES(3), JUEVES(4), VIERNES(5), SABADO(6), DOMINGO(7);

    private final int numero;

    DiaSemana(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public static DiaSemana desdeNumero(int numero) {
        for (DiaSemana dia : values()) {
            if (dia.getNumero() == numero) {
                return dia;
            }
        }
        throw new IllegalArgumentException("Número inválido de día: " + numero);
    }
}

