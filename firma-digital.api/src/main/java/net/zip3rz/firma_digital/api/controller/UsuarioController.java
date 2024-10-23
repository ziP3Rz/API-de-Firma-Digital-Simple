package net.zip3rz.firma_digital.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.zip3rz.firma_digital.api.model.ParClaves;
import net.zip3rz.firma_digital.api.model.Usuario;
import net.zip3rz.firma_digital.api.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("/registrar")
	public ResponseEntity<Usuario> registerUser(@RequestParam String username, @RequestParam String password) {
		return ResponseEntity.ok(usuarioService.registrarUsuario(username, password));
	}

	@PostMapping("/{nombre}/generar-claves")
	public ResponseEntity<ParClaves> generateKey(@PathVariable String nombre) throws Exception {
		ParClaves userKey = usuarioService.generarParClaves(nombre);
		return ResponseEntity.ok(userKey);
	}

}
