package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.RegistroClienteDto;
import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Usuario;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.UsuarioRepository;
import com.example.educapp_proyecto.service.ClienteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado del registro de nuevos clientes en EducApp.
 * Este servicio se asegura de que el email no esté duplicado,
 * crea un nuevo usuario con contraseña encriptada y registra el cliente asociado.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class RegistroClienteService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registrar cliente por dto
    /**
     * Registra un nuevo cliente a partir de un DTO de registro.
     * Verifica que el email no exista previamente, crea el usuario y lo asocia al cliente.
     *
     * @param dto Objeto que contiene los datos del nuevo cliente a registrar.
     * @throws RuntimeException si ya existe un usuario con el mismo email.
     */
    public void registrarCliente(RegistroClienteDto dto) {
        if (usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuarioRepo.save(usuario);

        // Crear cliente asociado
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidos(dto.getApellidos());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setUsuario(usuario);

        clienteRepo.save(cliente);
    }
}
