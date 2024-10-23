package net.zip3rz.firma_digital.api.service;

import net.zip3rz.firma_digital.api.model.ParClaves;
import net.zip3rz.firma_digital.api.model.Usuario;

public interface UsuarioService {

	Usuario getUsuario(String nombre);

	ParClaves generarParClaves(String nombre);

	Usuario registrarUsuario(String nombre, String contrasena);

}
