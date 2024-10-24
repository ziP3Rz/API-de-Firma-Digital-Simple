package net.zip3rz.firma_digital.api.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.zip3rz.firma_digital.api.model.Usuario;
import net.zip3rz.firma_digital.api.repository.UsuarioRepository;

/**
 * Servicio que implementa la interfaz UserDetailsService de Spring Security para cargar un usuario a partir de su nombre.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
   
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombre(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        return new User(usuario.getNombre(), usuario.getContrasena(), new ArrayList<>());
    }
}
