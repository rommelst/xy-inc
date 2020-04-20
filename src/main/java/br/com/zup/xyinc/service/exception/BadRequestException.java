package br.com.zup.xyinc.service.exception;



public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String fieldName;

	public BadRequestException(String fieldName, String msg ) {
		super(msg);
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return this.fieldName;
	}
}