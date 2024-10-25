package net.zip3rz.firma_digital.api.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

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

            usuario.setClavePublica(parClaves.getPublic().getEncoded());

            String rutaArchivo = RUTA_PRIVATE_KEY + nombre + "_private.key";
            guardarClavePrivada(parClaves.getPrivate(), rutaArchivo);

            usuarioRepository.save(usuario);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el par de claves", e);
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
	 * @param nombre nombre del usuario.
	 * @param contrasena contraseña del usuario.
	 * @throws RuntimeException si la autenticación falla.
	 */
	@Override
    public void autenticarUsuario(String nombre, String contrasena) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(nombre, contrasena);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Autenticación fallida");
        }
    }

    /**
     * Guarda la clave privada de un usuario en el sistema de archivos.
     * @param clavePrivada clave privada a guardar.
     * @param rutaArchivo ruta del archivo donde se guardará la clave.
     * @param nombreUsuario nombre del usuario.
     */
	private void guardarClavePrivada(PrivateKey clavePrivada, String rutaArchivo) {
	    try {
	    	Path path = Paths.get(rutaArchivo);
	        Files.createDirectories(path.getParent());
	        Files.write(path, clavePrivada.getEncoded());
	    } catch (IOException e) {
	        throw new RuntimeException("Error al guardar la clave privada", e);
	    }
	}
	
}
