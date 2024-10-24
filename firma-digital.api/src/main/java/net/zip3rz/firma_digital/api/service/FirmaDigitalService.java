package net.zip3rz.firma_digital.api.service;

/**
 * Servicio para firmar y verificar documentos digitales.
 */
public interface FirmaDigitalService {

	static final String ALGORITMO_FIRMA = "SHA256withRSA";

	static final String ALGORITMO_CLAVE_PUBLICA = "RSA";

	static final int LONGITUD_CLAVE = 2048;

	String firmarDocumento(String nombreUsuario, String documentoBase64);

	boolean verificarFirma(Long usuarioId, String dataBase64, String firmaBase64);

}
