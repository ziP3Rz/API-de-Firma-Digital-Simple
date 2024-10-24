package net.zip3rz.firma_digital.api.service.impl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.zip3rz.firma_digital.api.exception.GeneracionClaveException;
import net.zip3rz.firma_digital.api.exception.UsuarioExistenteException;
import net.zip3rz.firma_digital.api.exception.UsuarioNoEncontradoException;
import net.zip3rz.firma_digital.api.model.Usuario;
import net.zip3rz.firma_digital.api.repository.UsuarioRepository;
import net.zip3rz.firma_digital.api.service.FirmaDigitalService;
import net.zip3rz.firma_digital.api.service.UsuarioService;

/**
 * Implementación de la interfaz UsuarioService que define los métodos para gestionar usuarios.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
    private AuthenticationManager authenticationManager;

	/**
	 * Obtiene un usuario a partir de su id.
	 * @param id id del usuario.
	 * @throws UsuarioNoEncontradoException si el usuario no existe.
	 * @return usuario encontrado en el repositorio.
	 */
    @Override
	public Usuario getUsuarioById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con id " + id + " no encontrado"));
	}

	/**
	 * Obtiene un usuario a partir de su nombre.
	 * @param nombre nombre del usuario.
	 * @throws UsuarioNoEncontradoException si el usuario no existe.
	 * @return usuario encontrado en el repositorio.
	 */
	@Override
	public Usuario getUsuarioByNombre(String nombre) {
		return usuarioRepository.findByNombre(nombre).orElseThrow(() -> new UsuarioNoEncontradoException("Usuario " + nombre  + " no encontrado"));
	}

	/**
	 * Genera un par de claves para un usuario y las guarda en el repositorio.
	 * @param nombre nombre del usuario.
	 * @throws GeneracionClaveException si ocurre un error al generar las claves.
	 */
	@Override
	public void generarParClaves(String nombre) {
        Usuario usuario = getUsuarioByNombre(nombre);

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(FirmaDigitalService.ALGORITMO_CLAVE_PUBLICA);
            keyGen.initialize(FirmaDigitalService.LONGITUD_CLAVE);
            KeyPair parClaves = keyGen.generateKeyPair();

            usuario.setParClaves(parClaves);

            usuarioRepository.save(usuario);

        } catch (NoSuchAlgorithmException e) {
            throw new GeneracionClaveException("Error al generar el par de claves para el usuario: " + nombre, e);
        }
	}

	/**
	 * Registra un usuario en el repositorio.
	 * @param nombre nombre del usuario.
	 * @param contrasena contraseña del usuario.
	 * @throws UsuarioExistenteException si el usuario ya existe.
	 * @return mensaje de éxito o error.
	 */
	@Override
	public String registrarUsuario(String nombre, String contrasena) {
		usuarioRepository.findByNombre(nombre).ifPresent(usuario -> {
			throw new UsuarioExistenteException("El usuario " + nombre + " ya existe");
		});
        String contrasenaEncriptada = passwordEncoder.encode(contrasena);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setContrasena(contrasenaEncriptada);

        usuarioRepository.save(usuario);
	
        return "Usuario registrado exitosamente";
	}

	/**
	 * Autentica un usuario.
	 * @param username nombre del usuario.
	 * @param password contraseña del usuario.
	 * @throws RuntimeException si la autenticación falla.
	 */
	@Override
    public void autenticarUsuario(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Autenticación fallida");
        }
    }

}
