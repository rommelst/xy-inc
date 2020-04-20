package br.com.zup.xyinc.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.com.zup.xyinc.controller.exception.ValidationError;
import br.com.zup.xyinc.service.PontoInteresseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zup.xyinc.domain.PontoInteresse;


@RestController
@Validated
@RequestMapping(value = "/v1/pontointeresse")
public class PontoInteresseController {


	private final PontoInteresseService service;
	@Autowired
	public PontoInteresseController(PontoInteresseService service){
		this.service = service;
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<PontoInteresse> getById(@PathVariable("id") Long id) {
		PontoInteresse obj = service.find(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Iterable<PontoInteresse>> listAll() {
		return new ResponseEntity<>(service.list(), HttpStatus.OK);
	}

	@GetMapping(path = "page")
	public ResponseEntity<Page<PontoInteresse>> listAllWithPage(Pageable pageable) {
		service.validateSortColumn(pageable.getSort());
		return new ResponseEntity<Page<PontoInteresse>>(service.page(pageable), HttpStatus.OK);
	}

	@GetMapping( path = "near")
	public ResponseEntity<?> listByProximity(
		@RequestParam(value="x") @Min(value = 0, message = "x não pode ser negativo.") Integer actualX,
		@RequestParam(value="y") @Min(value = 0, message = "y não pode ser negativo.") Integer actualY,
		@RequestParam(value="d") Integer distancia
	) {
		return new ResponseEntity<>(service.listNear(actualX, actualY, distancia), HttpStatus.OK);
	}

	@GetMapping( path = "near/page")
	public ResponseEntity<Page<PontoInteresse>> listByProximityWithPage(
			@RequestParam(value="x") @Min(value = 0, message = "x não pode ser negativo.") Integer actualX,
			@RequestParam(value="y") @Min(value = 0, message = "y não pode ser negativo.") Integer actualY,
			@RequestParam(value="d") Integer distancia,
			Pageable pageable
	) {
		service.validateSortColumn(pageable.getSort());
		return new ResponseEntity<Page<PontoInteresse>>(service.listNearWithPage(actualX, actualY, distancia, pageable), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<PontoInteresse> create(@Valid @RequestBody PontoInteresse obj) {
		obj = service.insert(obj);
		Long id = obj.getId();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(uri).build();
	}


	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteBadFormed() {
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de requisição",
			System.currentTimeMillis());
		err.addError("id", "Id do ponto de interesse não informado na URL.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody PontoInteresse obj, @NotNull  @PathVariable Long id) {
		service.validateUrlHasId(obj, id);
		service.update(obj);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping
	public ResponseEntity<?> updateBadFormed(@RequestBody PontoInteresse obj) {
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de requisição",
				System.currentTimeMillis());
		err.addError("id", "Id do ponto de interesse não informado na URL.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

}
