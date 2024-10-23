package net.zip3rz.firma_digital.api.service;

public interface FirmaDigitalService {

	String firmarDocumento(String nombreUsuario, String documento);

	boolean verificarFirma(String nombreUsuario, String documento, String firma);

}
