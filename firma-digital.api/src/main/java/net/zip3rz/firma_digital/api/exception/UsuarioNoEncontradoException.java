package net.zip3rz.firma_digital.api.exception;

/**
 * Excepción para usuarios no encontrados.
 */
public class UsuarioNoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }

}