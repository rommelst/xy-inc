package br.com.zup.xyinc.handler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import br.com.zup.xyinc.controller.exception.StandardError;
import br.com.zup.xyinc.controller.exception.ValidationError;
import br.com.zup.xyinc.service.exception.BadRequestException;
import br.com.zup.xyinc.service.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> MethodArgumentNotValidException(MethodArgumentNotValidException e,
			HttpServletRequest request) {

		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de validação",
				System.currentTimeMillis());

		for (FieldError item : e.getBindingResult().getFieldErrors()) {
			err.addError(item.getField(), item.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<StandardError> MissingServletRequestParameterException(MissingServletRequestParameterException e,
																		 HttpServletRequest request) {
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de requisição",
				System.currentTimeMillis());
		err.addError(e.getParameterName(), "O parâmetro " + e.getParameterName() + " do tipo " + e.getParameterType() + " é obrigatório e não foi informado.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<StandardError> ConstraintViolationException(ConstraintViolationException e,
																				 HttpServletRequest request) {
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de requisição",
				System.currentTimeMillis());
		for ( ConstraintViolation<?> item : e.getConstraintViolations() ) {
			err.addError(item.getPropertyPath().toString(), item.getMessage() + " Valor atual é: " + item.getInvalidValue() + ".");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<StandardError> BadRequestException(BadRequestException e,
																	  HttpServletRequest request) {
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de requisição",
				System.currentTimeMillis());
		err.addError(e.getFieldName(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> BadRequestException(HttpMessageNotReadableException e,
															 HttpServletRequest request) {
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de requisição",
				System.currentTimeMillis());
		err.addError("None", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

}
