package br.com.zup.xyinc.controller.exception;

public class FieldMessage {

	private final String fieldName;
	private final String message;
	public FieldMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}
	public String getFieldName() {
		return fieldName;
	}
	public String getMessage() {
		return message;
	}
	
}
