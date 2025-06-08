package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Usuario;
import com.example.educapp_proyecto.repository.UsuarioRepository;
import com.example.educapp_proyecto.service.UserDetailsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Implementación personalizada de {@link org.springframework.security.core.userdetails.UserDetailsService}
 * que permite cargar detalles de usuario a partir del email, utilizado para la autenticación en Spring Security.
 * Esta clase transforma el usuario en una instancia de {@link org.springframework.security.core.userdetails.User}
 * con sus correspondientes roles como autoridades.
 *
 * @author Noe
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsServiceInterface {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Cargar un usuario por su nombre de usuario
    /**
     * Carga los detalles de un usuario dado su email.
     *
     * @param email el email del usuario
     * @return los detalles del usuario como {@link UserDetails}
     * @throws UsernameNotFoundException si no se encuentra el usuario con el email proporcionado
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                        .collect(Collectors.toList())
        );
    }
}