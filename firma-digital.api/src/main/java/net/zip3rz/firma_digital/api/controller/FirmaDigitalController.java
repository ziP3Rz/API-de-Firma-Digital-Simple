package net.zip3rz.firma_digital.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.zip3rz.firma_digital.api.dto.DocumentoFirmadoDTO;
import net.zip3rz.firma_digital.api.exception.FirmaDigitalException;
import net.zip3rz.firma_digital.api.exception.UsuarioNoEncontradoException;
import net.zip3rz.firma_digital.api.service.FirmaDigitalService;

/**
 * Controlador para la firma digital de documentos y verificación de firmas.
 */
@RestController
@RequestMapping("/firma-digital")
public class FirmaDigitalController {

    @Autowired
    private FirmaDigitalService firmaDigitalService;

    /**
     * Endpoint para firmar un documento.
     * @param usuario nombre del usuario.
     * @param documento documento a firmar en base64.
     * @return firma del documento.
     */
    @PostMapping("/firmar")
    public ResponseEntity<String> firmarDocumento(@RequestParam String usuario, @RequestBody String documento) {
        try {
            String firma = firmaDigitalService.firmarDocumento(usuario, documento);
            return ResponseEntity.ok(firma);
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FirmaDigitalException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint para verificar la firma de un documento.
     * @param nombre nombre del usuario.
     * @param documento documento a verificar en base64.
     * @param firma firma del documento.
     * @return true si la firma es válida, false en caso contrario.
     */
    @PostMapping("/verificar-documento")
    public ResponseEntity<Boolean> verificarFirma(@RequestBody DocumentoFirmadoDTO request) {
        try {
            boolean isValida = firmaDigitalService.verificarFirma(request.getUsuarioId(), request.getDocumentoBase64(), request.getFirmaBase64());
            return ResponseEntity.ok(isValida);
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        } catch (FirmaDigitalException e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

}
