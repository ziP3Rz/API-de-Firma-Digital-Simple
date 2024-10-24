package net.zip3rz.firma_digital.api.dto;

import lombok.Data;

/**
 * DTO para documentos firmados.
 */
@Data
public class DocumentoFirmadoDTO {

	private Long usuarioId;

	private String documentoBase64;

	private String firmaBase64;

}
