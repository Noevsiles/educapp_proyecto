package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.RegistroRequestDto;
import com.example.educapp_proyecto.model.Rol;
import com.example.educapp_proyecto.model.Usuario;
import com.example.educapp_proyecto.repository.RolRepository;
import com.example.educapp_proyecto.repository.UsuarioRepository;
import com.example.educapp_proyecto.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody RegistroRequestDto dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        Rol rol = rolRepository.findByNombre(dto.getRol().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        nuevoUsuario.getRoles().add(rol);

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegistroRequestDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        String rol = usuario.getRoles().iterator().next().getNombre();
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getNombre(), rol);
        return ResponseEntity.ok(token);
    }

}
