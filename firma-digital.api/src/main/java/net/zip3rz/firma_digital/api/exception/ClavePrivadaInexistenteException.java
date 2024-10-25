package net.zip3rz.firma_digital.api.exception;

public class ClavePrivadaInexistenteException extends Exception {

	private static final long serialVersionUID = 1L;

	public ClavePrivadaInexistenteException(String mensaje) {
		super(mensaje);
	}

}
