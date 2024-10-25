package net.zip3rz.firma_digital.api.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zip3rz.firma_digital.api.exception.ClavePublicaInexistenteException;
import net.zip3rz.firma_digital.api.exception.FirmaDigitalException;
import net.zip3rz.firma_digital.api.exception.UsuarioNoEncontradoException;
import net.zip3rz.firma_digital.api.model.Usuario;
import net.zip3rz.firma_digital.api.service.FirmaDigitalService;
import net.zip3rz.firma_digital.api.service.UsuarioService;

/**
 * Implementación de la interfaz FirmaDigitalService que define los métodos para firmar y verificar documentos.
 */
@Service
public class FirmaDigitalServiceImpl implements FirmaDigitalService {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Firma un documento con la clave privada de un usuario.
     * @param nombreUsuario nombre del usuario.
     * @param documentoBase64 documento a firmar en base64.
     * @throws FirmaDigitalException si ocurre un error al firmar el documento.
     * @return firma del documento en Base64.
     */
    @Override
    public String firmarDocumento(String nombreUsuario, String documentoBase64) {
    	try {
            byte[] documento = Base64.getDecoder().decode(documentoBase64);

            String rutaArchivo = UsuarioService.RUTA_PRIVATE_KEY + nombreUsuario + "_private.key";
            PrivateKey privateKey = cargarClavePrivada(rutaArchivo);

            Signature signature = Signature.getInstance(ALGORITMO_FIRMA);
            signature.initSign(privateKey);
            signature.update(documento);

            byte[] firmaBytes = signature.sign();
            
            return Base64.getEncoder().encodeToString(firmaBytes);
            
        } catch (Exception e) {
        	e.printStackTrace();
            throw new FirmaDigitalException("Error al firmar el documento", e);
        }
    }

    /**
     * Verifica la firma de un documento utilizando la clave pública del usuario.
     * @param usuarioId El nombre del usuario cuya clave pública debe ser utilizada.
     * @param documentoBase64 El documento codificado en Base64.
     * @param firmaBase64 La firma digital codificada en Base64.
     * @throws UsuarioNoEncontradoException si el usuario no existe.
     * @throws FirmaDigitalException si ocurre un error durante la verificación de la firma.
     * @return true si la firma es válida, false en caso contrario.
     */
    @Override
    public boolean verificarFirma(Long usuarioId, String documentoBase64, String firmaBase64) {
    	System.out.println("Documento original (Base64): " + documentoBase64);
    	System.out.println("Firma generada (Base64): " + firmaBase64);
    	try {
            Usuario usuario = usuarioService.getUsuarioById(usuarioId);
            byte[] documento = Base64.getDecoder().decode(documentoBase64);
            byte[] firma = Base64.getDecoder().decode(firmaBase64);

            byte[] clavePublicaBytes = usuario.getClavePublica();
			if (clavePublicaBytes == null) {
				throw new ClavePublicaInexistenteException("Usuario sin clave pública");
			}
			PublicKey clavePublica = KeyFactory.getInstance(ALGORITMO_CLAVE_PUBLICA)
	            .generatePublic(new X509EncodedKeySpec(clavePublicaBytes));

	        Signature signature = Signature.getInstance(ALGORITMO_FIRMA);
	        signature.initVerify(clavePublica);
	        signature.update(documento);

	        boolean resultadoVerificacion = signature.verify(firma);
	        System.out.println("Firma: " + Base64.getEncoder().encodeToString(firma));
	        System.out.println("Resultado de verificación: " + resultadoVerificacion);

	        return resultadoVerificacion;


		} catch (UsuarioNoEncontradoException e) {
			throw new UsuarioNoEncontradoException("Usuario no encontrado");
		} catch (Exception e) {
			e.printStackTrace();
			throw new FirmaDigitalException("Error al verificar la firma", e);
		}
    }
    
    /**
     * Carga la clave privada de un archivo.
     * @param rutaArchivo ruta del archivo que contiene la clave privada.
     * @throws Exception si ocurre un error al cargar la clave privada.
     * @return clave privada.
     */
    private PrivateKey cargarClavePrivada(String rutaArchivo) throws Exception {
        Path path = Paths.get(rutaArchivo);
        byte[] clavePrivadaBytes = Files.readAllBytes(path);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clavePrivadaBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITMO_CLAVE_PUBLICA);
        return keyFactory.generatePrivate(keySpec);
    }

}
