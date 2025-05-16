package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.repository.PerroRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class InformePdfService {

    private final PerroRepository perroRepository;

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

        document.add(new Paragraph("Informe de Conducta").setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
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

        document.add(new Paragraph("Problemas de conducta:").setBold());

        for (var problema : perro.getProblemasDeConducta()) {
            problema.getSolucionAplicadas().size();
            document.add(new Paragraph("- " + problema.getNombre() + ": " + problema.getDescripcion()));

            if (problema.getCausaDeProblemas() != null && !problema.getCausaDeProblemas().isEmpty()) {
                document.add(new Paragraph("  Causas:").setBold());
                for (var causa : problema.getCausaDeProblemas()) {
                    document.add(new Paragraph("    • " + causa.getCausa().getNombre() + ": " + causa.getCausa().getDescripcion()));
                }
            }

            if (problema.getSolucionAplicadas() != null && !problema.getSolucionAplicadas().isEmpty()) {
                document.add(new Paragraph("  Soluciones Aplicadas:").setBold());
                for (var sa : problema.getSolucionAplicadas()) {
                    var solucion = sa.getSolucion();
                    document.add(new Paragraph("    • " + solucion.getNombre() + ": " + solucion.getDescripcion()));
                }
            }
            document.add(new Paragraph("\n"));
        }
        document.close();
        return baos.toByteArray();
    }
}
