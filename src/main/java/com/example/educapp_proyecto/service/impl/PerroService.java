package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.PerroRequestDto;
import com.example.educapp_proyecto.dto.PerroResponseDto;
import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Perro;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.service.PerroServiceInterface;
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
import java.util.Optional;

@Service
public class PerroService implements PerroServiceInterface {
    @Autowired
    private PerroRepository perroRepository;

    @Autowired
    ClienteRepository clienteRepository;


    @Override
    public List<Perro> findAll() {
        return perroRepository.findAll();
    }

    @Override
    public Perro findById(Long id) {
        Optional<Perro> perro = perroRepository.findById(id);
        if (perro.isPresent()) {
            return perro.get();
        } else {
            throw new RuntimeException("Perro no encontrado con el id: " + id);
        }
    }

    @Override
    public Perro save(Perro perro) {
        return perroRepository.save(perro);
    }

    @Override
    public void deleteById(Long id) {
        if (perroRepository.existsById(id)) {
            perroRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, perro no encontrado con el id: " + id);
        }
    }

    // Actualizar un perro
    public Perro updatePerro(Long id, Perro perro) {
        if (perroRepository.existsById(id)) {
            perro.setIdPerro(id);  // Aseguramos que el ID es el mismo
            return perroRepository.save(perro);
        } else {
            throw new RuntimeException("Perro no encontrado con el id: " + id);
        }
    }

    @Override
    public PerroResponseDto crearPerro(PerroRequestDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID " + dto.getClienteId()));

        Perro perro = new Perro();
        perro.setNombre(dto.getNombre());
        perro.setRaza(dto.getRaza());
        perro.setSexo(dto.getSexo());
        perro.setEdad(dto.getEdad());
        perro.setEsterilizado(dto.isEsterilizado());
        perro.setCliente(cliente);

        Perro guardado = perroRepository.save(perro);

        PerroResponseDto response = new PerroResponseDto();
        response.setId(guardado.getIdPerro());
        response.setNombre(guardado.getNombre());
        response.setRaza(guardado.getRaza());
        response.setSexo(guardado.getSexo());
        response.setEdad(guardado.getEdad());
        response.setEsterilizado(guardado.isEsterilizado());
        response.setNombreCliente(cliente.getNombre());

        return response;
    }

    @Service
    @RequiredArgsConstructor
    public static class InformePdfService {

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
}
