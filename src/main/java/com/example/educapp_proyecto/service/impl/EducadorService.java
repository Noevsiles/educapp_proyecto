package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Usuario;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.repository.UsuarioRepository;
import com.example.educapp_proyecto.service.EducadorServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EducadorService implements EducadorServiceInterface {
    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Educador> findAll() {
        return educadorRepository.findAll();
    }


    // Crear usuario a partir del email de registro
    @Override
    public Educador crearEducadorParaUsuario(Educador educador, String emailUsuario) {
        Optional<Educador> existente = educadorRepository.findByEmail(educador.getEmail());
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un educador con ese email");
        }

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        educador.setUsuario(usuario);
        educador.setEmail(emailUsuario);
        return educadorRepository.save(educador);
    }

    @Override
    public Educador crearOActualizarPerfilEducador(Educador educadorDatos, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + emailUsuario));

        Optional<Educador> educadorExistente = educadorRepository.findByEmail(emailUsuario);

        Educador educador;
        if (educadorExistente.isPresent()) {
            educador = educadorExistente.get();
            educador.setNombre(educadorDatos.getNombre());
            educador.setApellidos(educadorDatos.getApellidos());
            educador.setTelefono(educadorDatos.getTelefono());
            educador.setEspecializacion(educadorDatos.getEspecializacion());
            educador.setExperiencia(educadorDatos.getExperiencia());
            educador.setFormacion(educadorDatos.getFormacion());
            educador.setDescripcion(educadorDatos.getDescripcion());
        } else {
            educador = new Educador();
            educador.setNombre(educadorDatos.getNombre());
            educador.setApellidos(educadorDatos.getApellidos());
            educador.setTelefono(educadorDatos.getTelefono());
            educador.setEspecializacion(educadorDatos.getEspecializacion());
            educador.setExperiencia(educadorDatos.getExperiencia());
            educador.setFormacion(educadorDatos.getFormacion());
            educador.setDescripcion(educadorDatos.getDescripcion());
            educador.setEmail(emailUsuario);
            educador.setUsuario(usuario);
        }

        return educadorRepository.save(educador);
    }


    // Encontrar un educador por su id
    @Override
    public Educador findById(Long id) {
        Optional<Educador> educador = educadorRepository.findById(id);
        if (educador.isPresent()) {
            return educador.get();
        } else {
            throw new RuntimeException("Educador no encontrado con el id: " + id);
        }
    }

    // Obtener educador por su email
    @Override
    public Optional<Educador> findByEmail(String email) {
        return educadorRepository.findByEmail(email);
    }


    public Optional<Educador> obtenerEducadorPorEmailDesdeToken(String email) {
        return educadorRepository.findByEmail(email);
    }


    // Guardar educador
    @Override
    public Educador save(Educador educador) {
        return educadorRepository.save(educador);
    }

    // Borrar un educador por su id
    @Override
    public void deleteById(Long id) {
        if (educadorRepository.existsById(id)) {
            educadorRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, educador no encontrado con el id: " + id);
        }
    }

    // Actualizar un educador
    public Educador updateEducador(Long id, Educador nuevosDatos) {
        Educador existente = educadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado con el id: " + id));

        // Solo se actualizan los campos editables
        existente.setNombre(nuevosDatos.getNombre());
        existente.setApellidos(nuevosDatos.getApellidos());
        existente.setTelefono(nuevosDatos.getTelefono());
        existente.setEspecializacion(nuevosDatos.getEspecializacion());
        existente.setExperiencia(nuevosDatos.getExperiencia());
        existente.setFormacion(nuevosDatos.getFormacion());
        existente.setDescripcion(nuevosDatos.getDescripcion());

        // El email y el usuario se conservan del objeto existente
        return educadorRepository.save(existente);
    }
}
