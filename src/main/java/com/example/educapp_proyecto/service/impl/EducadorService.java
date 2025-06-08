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

/**
 * Servicio para la gestión de educadores.
 * Permite crear, actualizar, eliminar y recuperar educadores,
 * así como asociarlos con usuarios del sistema.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class EducadorService implements EducadorServiceInterface {
    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Devuelve todos los educadores registrados.
     *
     * @return lista de educadores.
     */
    @Override
    public List<Educador> findAll() {
        return educadorRepository.findAll();
    }


    // Crear usuario a partir del email de registro
    /**
     * Crea un educador y lo asocia con un usuario existente a partir de su email.
     *
     * @param educador objeto educador con los datos básicos.
     * @param emailUsuario email del usuario al que se va a vincular.
     * @return educador guardado.
     */
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

    /**
     * Crea o actualiza el perfil de un educador asociado a un email de usuario.
     *
     * @param educadorDatos datos del educador a guardar o actualizar.
     * @param emailUsuario email del usuario autenticado.
     * @return educador actualizado o creado.
     */
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
    /**
     * Encontrar un educador por su ID.
     *
     * @param id ID del educador.
     * @return educador encontrado.
     */
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
    /**
     * Obtiene un educador por su email.
     *
     * @param email email del educador.
     * @return Optional con el educador, si existe.
     */
    @Override
    public Optional<Educador> findByEmail(String email) {
        return educadorRepository.findByEmail(email);
    }

    /**
     * Obtiene el educador por su email por su token.
     *
     * @param email email del educador.
     * @return Optional con el educador, si existe.
     */
    public Optional<Educador> obtenerEducadorPorEmailDesdeToken(String email) {
        return educadorRepository.findByEmail(email);
    }


    // Guardar educador
    /**
     * Guarda un educador en la base de datos.
     *
     * @param educador educador a guardar.
     * @return educador guardado.
     */
    @Override
    public Educador save(Educador educador) {
        return educadorRepository.save(educador);
    }

    // Borrar un educador por su id
    /**
     * Borra un educador por su ID.
     *
     * @param id ID del educador.
     */
    @Override
    public void deleteById(Long id) {
        if (educadorRepository.existsById(id)) {
            educadorRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, educador no encontrado con el id: " + id);
        }
    }

    // Actualizar un educador
    /**
     * Actualiza los datos editables de un educador.
     *
     * @param id ID del educador a actualizar.
     * @param nuevosDatos datos nuevos del educador.
     * @return educador actualizado.
     */
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
