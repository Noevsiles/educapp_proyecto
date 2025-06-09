package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.repository.PlanTrabajoRepository;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.io.font.PdfEncodings;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Servicio encargado de generar informes PDF con los datos
 * del cliente, perro, problemas de conducta, causas, soluciones y planes de trabajo.
 * Utiliza la librería iText para la creación de documentos PDF.
 *
 * @author Noelia Vázquez Siles
 */
@Service
@RequiredArgsConstructor
public class InformePdfService {

    private final PerroRepository perroRepository;

    private final PlanTrabajoRepository planTrabajoRepository;

    @Autowired
    private ProblemaDeConductaService problemaDeConductaService;


    // Generar un informe por perro a través de su id
    /**
     * Genera un informe PDF con todos los detalles relevantes sobre el perro, sus problemas de conducta,
     * causas, soluciones aplicadas y planes de trabajo asignados.
     *
     * @param idPerro ID del perro para el cual se generará el informe.
     * @return Un array de bytes que representa el documento PDF generado.
     * @throws Exception si ocurre un error durante la generación del documento.
     */
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

        // Problemas de conducta
        var problemas = perro.getProblemasDeConducta();
        if (problemas != null && !problemas.isEmpty()) {
            document.add(new Paragraph("Problemas de Conducta").setBold().setFontSize(14));
            for (var problema : problemas) {
                document.add(new Paragraph(" - " + problema.getNombre()).setBold());
                document.add(new Paragraph("Descripción: " + problema.getDescripcion()).setBold());

                if (problema.getCausaDeProblemas() != null && !problema.getCausaDeProblemas().isEmpty()) {
                    document.add(new Paragraph("Causas:").setBold());
                    for (var causa : problema.getCausaDeProblemas()) {
                        document.add(new Paragraph("    • " + causa.getCausa().getDescripcion()));
                    }
                }

                if (problema.getSolucionAplicadas() != null && !problema.getSolucionAplicadas().isEmpty()) {
                    document.add(new Paragraph("Soluciones Aplicadas:").setBold());
                    for (var solucionAplicada : problema.getSolucionAplicadas()) {
                        var solucion = solucionAplicada.getSolucion();
                        document.add(new Paragraph("    • " + solucion.getNombre() + ": " + solucion.getDescripcion()));
                    }
                }

                document.add(new Paragraph("\n"));
            }
        }

        // Planes del perro
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
