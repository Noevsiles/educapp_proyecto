package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.service.impl.PerroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/informes")
@RequiredArgsConstructor
public class InformePdfController {
    private final PerroService.InformePdfService informePdfService;

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
