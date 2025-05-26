package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.repository.PlanTrabajoRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InformePdfService {

    private final PerroRepository perroRepository;

    private final PlanTrabajoRepository planTrabajoRepository;

    public byte[] generarInformePorPerro(Long idPerro) throws Exception {
        var perro = perroRepository.findById(idPerro)
                .orElseThrow(() -> new RuntimeException("Perro no encontrado"));
        var cliente = perro.getCliente();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        var font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);

        document.add(new Paragraph("Informe de Planes de Trabajo").setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos()));
        document.add(new Paragraph("Email: " + cliente.getEmail()));
        document.add(new Paragraph("Teléfono: " + cliente.getTelefono()));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Perro: " + perro.getNombre()));
        document.add(new Paragraph("Raza: " + perro.getRaza()));
        document.add(new Paragraph("Edad: " + perro.getEdad() + " años"));
        document.add(new Paragraph("Sexo: " + perro.getSexo()));
        document.add(new Paragraph("\n"));

        // Aquí obtenemos los planes del perro directamente
        List<PlanTrabajo> planes = planTrabajoRepository.findByPerro_IdPerro(idPerro);

        if (planes.isEmpty()) {
            document.add(new Paragraph("Este perro no tiene planes de trabajo asignados."));
        } else {
            for (var plan : planes) {
                document.add(new Paragraph("Plan ID: " + plan.getId() + " - Observaciones: " + plan.getObservaciones()).setBold());
                document.add(new Paragraph("Número total de sesiones: " + plan.getNumeroSesiones()));
                document.add(new Paragraph("Actividades:"));
                for (var actividad : plan.getActividades()) {
                    document.add(new Paragraph("   • " + actividad.getNombre() + " - " + actividad.getDescripcion() +
                            " (Duración: " + actividad.getDuracion() + " min, Completado: " + actividad.isCompletado() + ")"));
                }
                document.add(new Paragraph("\n"));
            }
        }

        document.close();
        return baos.toByteArray();
    }

}
