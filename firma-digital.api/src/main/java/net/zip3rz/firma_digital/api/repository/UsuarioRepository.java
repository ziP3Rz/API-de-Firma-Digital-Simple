package net.zip3rz.firma_digital.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.zip3rz.firma_digital.api.model.Usuario;

/**
 * Repositorio de la entidad Usuario que define los métodos para acceder a la base de datos.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByNombre(String nombre);
}
