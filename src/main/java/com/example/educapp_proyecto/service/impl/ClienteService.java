package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.ClienteRequestDto;
import com.example.educapp_proyecto.dto.ClienteResponseDto;
import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.service.ClienteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService implements ClienteServiceInterface {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EducadorRepository educadorRepository;

    // Encontrar todos
    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    // Encontrar cliente por su id
    @Override
    public Cliente findById(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            return cliente.get();
        } else {
            throw new RuntimeException("Cliente no encontrado con el id: " + id);
        }
    }

    // Guardar cliente
    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }


    // Eliminar cliente por id
    @Override
    public void deleteById(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, cliente no encontrado con el id: " + id);
        }
    }

    // Actualizar un cliente
    public Cliente updateCliente(Long id, Cliente datosActualizados) {
        Cliente clienteExistente = findById(id);

        clienteExistente.setNombre(datosActualizados.getNombre());
        clienteExistente.setApellidos(datosActualizados.getApellidos());
        clienteExistente.setEmail(datosActualizados.getEmail());
        clienteExistente.setTelefono(datosActualizados.getTelefono());

        // NO modifica educador
        return clienteRepository.save(clienteExistente);
    }

    // Crear un cliente a traves del dto
    @Override
    public ClienteResponseDto crearClienteDesdeDto(ClienteRequestDto dto, String emailEducador) {
        Educador educador = educadorRepository.findByEmail(emailEducador)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado con email: " + emailEducador));

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidos(dto.getApellidos());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEducador(educador);

        Cliente guardado = clienteRepository.save(cliente);

        ClienteResponseDto response = new ClienteResponseDto();
        response.setIdCliente(guardado.getIdCliente());
        response.setNombre(guardado.getNombre());
        response.setApellidos(guardado.getApellidos());
        response.setEmail(guardado.getEmail());
        response.setTelefono(guardado.getTelefono());
        response.setNombreEducador(educador.getNombre());

        return response;
    }

    // Obtener los clientes por el email del educador
    public List<ClienteResponseDto> obtenerClientesPorEmailEducador(String emailEducador) {
        Educador educador = educadorRepository.findByEmail(emailEducador)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado con email: " + emailEducador));

        List<Cliente> clientes = clienteRepository.findByEducador(educador);
        List<ClienteResponseDto> resultado = new ArrayList<>();

        for (Cliente cliente : clientes) {
            ClienteResponseDto response = new ClienteResponseDto();
            response.setIdCliente(cliente.getIdCliente());
            response.setNombre(cliente.getNombre());
            response.setApellidos(cliente.getApellidos());
            response.setEmail(cliente.getEmail());
            response.setTelefono(cliente.getTelefono());
            response.setNombreEducador(educador.getNombre());

            resultado.add(response);
        }

        return resultado;
    }

    // Obtener al cliente por su email
    public Cliente obtenerClientePorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email));
    }


}
