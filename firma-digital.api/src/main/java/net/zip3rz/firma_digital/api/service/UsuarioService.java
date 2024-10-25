package net.zip3rz.firma_digital.api.service;

import net.zip3rz.firma_digital.api.model.Usuario;

/**
 * Servicio para gestionar usuarios.
 */
public interface UsuarioService {

	static final String RUTA_PRIVATE_KEY = "/secure/privateKeys/";
	
	Usuario getUsuarioById(Long id);

	Usuario getUsuarioByNombre(String nombre);

	void generarParClaves(String nombre);

	String registrarUsuario(String nombre, String contrasena);

	void autenticarUsuario(String username, String password);

}
