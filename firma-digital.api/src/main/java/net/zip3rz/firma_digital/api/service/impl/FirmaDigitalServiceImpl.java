package net.zip3rz.firma_digital.api.service.impl;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            Usuario usuario = usuarioService.getUsuarioByNombre(nombreUsuario);
            if (usuario == null) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado: " + nombreUsuario);
            }

            byte[] documentoBytes = Base64.getDecoder().decode(documentoBase64);

            PrivateKey clavePrivada = KeyFactory.getInstance(ALGORITMO_CLAVE_PUBLICA)
                    .generatePrivate(new PKCS8EncodedKeySpec(usuario.getParClaves().getPrivate().getEncoded()));

            Signature firma = Signature.getInstance(ALGORITMO_FIRMA);
            firma.initSign(clavePrivada);
            firma.update(documentoBytes);

            byte[] firmaBytes = firma.sign();
            return Base64.getEncoder().encodeToString(firmaBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new FirmaDigitalException("Algoritmo de firma no encontrado", e);
        } catch (InvalidKeySpecException e) {
            throw new FirmaDigitalException("Especificación de clave inválida", e);
        } catch (SignatureException e) {
            throw new FirmaDigitalException("Error al firmar el documento", e);
        } catch (IllegalArgumentException e) {
            throw new FirmaDigitalException("Documento en Base64 inválido", e);
        } catch (Exception e) {
            throw new FirmaDigitalException("Error inesperado al firmar el documento", e);
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
        try {
            Usuario usuario = usuarioService.getUsuarioById(usuarioId);
            PublicKey clavePublica = usuario.getParClaves().getPublic();

            byte[] documentoBytes = Base64.getDecoder().decode(documentoBase64);
            byte[] firmaBytes = Base64.getDecoder().decode(firmaBase64);

            Signature firma = Signature.getInstance(ALGORITMO_FIRMA);
            firma.initVerify(clavePublica);
            firma.update(documentoBytes);

            return firma.verify(firmaBytes);

        } catch (UsuarioNoEncontradoException e) {
            throw new UsuarioNoEncontradoException("Usuario " + usuarioId + " no encontrado");
        } catch (Exception e) {
            e.printStackTrace();
            throw new FirmaDigitalException("Error al verificar la firma del documento", e);
        }
    }

}
