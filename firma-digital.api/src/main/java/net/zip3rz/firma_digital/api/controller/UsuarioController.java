package net.zip3rz.firma_digital.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.zip3rz.firma_digital.api.dto.UsuarioDTO;
import net.zip3rz.firma_digital.api.exception.GeneracionClaveException;
import net.zip3rz.firma_digital.api.exception.UsuarioExistenteException;
import net.zip3rz.firma_digital.api.model.Usuario;
import net.zip3rz.firma_digital.api.service.UsuarioService;

/**
 * Controlador para gestionar usuarios y sus claves.
 */ 
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	AuthenticationManager authenticationManager;
	
	/**
	 * Enpoint para registrar un usuario.
	 * @param nombre nombre del usuario.
	 * @param contrasena contraseña del usuario.
	 * @return mensaje de éxito o error.
	 */
	@PostMapping("/registrar")
	public ResponseEntity<String> registrarUsuario(@RequestParam String nombre, @RequestParam String contrasena) {
		try {
			return ResponseEntity.ok(usuarioService.registrarUsuario(nombre, contrasena));
		} catch (UsuarioExistenteException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Endpoint para autenticar un usuario.
	 * @param nombre nombre del usuario.
	 * @param contrasena contraseña del usuario.
	 * @return mensaje de éxito o error.
	 */
	@PostMapping("/autenticar")
	public ResponseEntity<UsuarioDTO> autenticarUsuario(@RequestParam String nombre, @RequestParam String contrasena) {
		try {
			usuarioService.autenticarUsuario(nombre, contrasena);
			
			Usuario usuario = usuarioService.getUsuarioByNombre(nombre);
			
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			
			usuarioDTO.setNombre(usuario.getNombre());
			usuarioDTO.setId(usuario.getId());
			
			return ResponseEntity.ok(usuarioDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	/**
	 * Endpoint para generar un par de claves para un usuario.
	 * @param nombre.
	 * @return mensaje de éxito o error.
	 */
	@PostMapping("/{nombre}/generar-claves")
	public ResponseEntity<String> generarClaves(@PathVariable String nombre) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nombreAutenticado = authentication.getName();
	    if (!nombreAutenticado.equals(nombre)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para generar claves para otro usuario");
	    }
		try {
            usuarioService.generarParClaves(nombre);
            return ResponseEntity.ok("Par de claves generado y asignado exitosamente");
        } catch (GeneracionClaveException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
	}

}
