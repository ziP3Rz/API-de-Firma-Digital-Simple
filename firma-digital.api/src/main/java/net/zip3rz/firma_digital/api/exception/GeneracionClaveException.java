package net.zip3rz.firma_digital.api.exception;

/**
 * Excepción para errores en la generación de claves.
 */
public class GeneracionClaveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GeneracionClaveException(String message, Throwable cause) {
        super(message, cause);
    }

}