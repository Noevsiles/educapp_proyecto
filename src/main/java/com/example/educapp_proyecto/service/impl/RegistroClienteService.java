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

@Service
public class RegistroClienteService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
