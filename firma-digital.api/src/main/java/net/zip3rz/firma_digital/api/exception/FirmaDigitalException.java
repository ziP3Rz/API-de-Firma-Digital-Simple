package net.zip3rz.firma_digital.api.exception;

/**
 * Excepción para errores en la firma digital.
 */
public class FirmaDigitalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FirmaDigitalException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}

