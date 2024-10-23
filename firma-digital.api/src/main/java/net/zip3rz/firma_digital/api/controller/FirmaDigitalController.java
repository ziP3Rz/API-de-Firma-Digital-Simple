package net.zip3rz.firma_digital.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.zip3rz.firma_digital.api.service.FirmaDigitalService;

@RestController
@RequestMapping("/firma-digital")
public class FirmaDigitalController {

    @Autowired
    private FirmaDigitalService firmaDigitalService;

    @PostMapping("/firmar")
    public ResponseEntity<String> firmarDocumento(@RequestParam String username, @RequestBody String document) throws Exception {
        return ResponseEntity.ok(firmaDigitalService.firmarDocumento(username, document));
    }

    @PostMapping("/verificar-documento")
    public ResponseEntity<Boolean> verificarFirma(@RequestParam String nombre, @RequestBody String documento, @RequestParam String firma) throws Exception {
        boolean isValida = firmaDigitalService.verificarFirma(nombre, documento, firma);
        return ResponseEntity.ok(isValida);
    }
}
