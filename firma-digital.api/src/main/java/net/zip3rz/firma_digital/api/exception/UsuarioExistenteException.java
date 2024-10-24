package net.zip3rz.firma_digital.api.exception;

/**
 * Excepción para usuarios existentes.
 */
public class UsuarioExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioExistenteException(String mensaje) {
        super(mensaje);
    }
	
}
