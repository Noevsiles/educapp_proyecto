package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.service.impl.InformePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author Noelia Vázquez Siles
 * Controlador REST para la generación de informes PDF relacionados con los perros.
 */
@RestController
@RequestMapping("/api/informes")
@RequiredArgsConstructor
public class InformePdfController {

    private final InformePdfService informePdfService;

    /**
     * Genera un informe en formato PDF para un perro específico.
     *
     * @param idPerro ID del perro del cual se desea generar el informe.
     * @return Archivo PDF como arreglo de bytes, con cabeceras para descarga.
     * @throws Exception Si ocurre un error durante la generación del informe.
     */
    @GetMapping("/perro/{idPerro}/pdf")
    public ResponseEntity<byte[]> generarInformePdf(@PathVariable Long idPerro) throws Exception {
        byte[] pdf = informePdfService.generarInformePorPerro(idPerro);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("informe_perro_" + idPerro + ".pdf")
                .build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
