package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.ClienteRequestDto;
import com.example.educapp_proyecto.dto.ClienteResponseDto;
import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.security.JwtUtil;
import com.example.educapp_proyecto.service.impl.ClienteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para la gestión de clientes en el sistema.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Crea un nuevo cliente asociado al educador autenticado.
     *
     * @param dto      Objeto DTO con los datos del cliente a registrar.
     * @param principal Principal de Spring Security que representa al educador autenticado.
     * @return DTO del cliente creado con estado HTTP 201.
     */
    // Crear un cliente
    @PostMapping
    public ResponseEntity<ClienteResponseDto> crearCliente(@RequestBody ClienteRequestDto dto, Principal principal) {
        String emailEducador = principal.getName();
        ClienteResponseDto nuevoCliente = clienteService.crearClienteDesdeDto(dto, emailEducador);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    /**
     * Obtiene los clientes asociados al educador autenticado.
     *
     * @param request Petición HTTP para extraer el token y obtener el email del educador.
     * @return Lista de clientes en formato DTO.
     */
    // Obtener los clientes asociados a un educador
    @GetMapping("/educador")
    public ResponseEntity<List<ClienteResponseDto>> obtenerClientesDelEducador(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        List<ClienteResponseDto> clientes = clienteService.obtenerClientesPorEmailEducador(email);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Obtiene todos los clientes registrados (uso general).
     *
     * @return Lista completa de entidades {@link Cliente}.
     */
    // Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodosClientes() {
        List<Cliente> clientes = clienteService.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    /**
     * Obtiene un cliente específico por su ID.
     *
     * @param id ID del cliente a buscar.
     * @return El cliente correspondiente o estado 404 si no se encuentra.
     */
    // Obtener un cliente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.findById(id);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id      ID del cliente a actualizar.
     * @param cliente Objeto {@link Cliente} con los datos actualizados.
     * @return El cliente actualizado o estado 404 si no se encuentra.
     */
    // Actualizar un cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = clienteService.updateCliente(id, cliente);
            return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id ID del cliente a eliminar.
     * @return HTTP 204 si se elimina correctamente, o HTTP 404 si no existe.
     */
    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener el perfil del cliente
    @GetMapping("/mi-perfil")
    public ResponseEntity<ClienteResponseDto> obtenerPerfilCliente(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        ClienteResponseDto dto = clienteService.obtenerPerfilCliente(email);
        return ResponseEntity.ok(dto);
    }


}
