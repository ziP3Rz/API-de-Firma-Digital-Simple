package net.zip3rz.firma_digital.api.exception;

/**
 * Excepción para claves públicas inexistentes.
 */
public class ClavePublicaInexistenteException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ClavePublicaInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
