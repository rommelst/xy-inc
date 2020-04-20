package br.com.zup.xyinc.controller;

import br.com.zup.xyinc.DefaultValues;
import br.com.zup.xyinc.domain.PontoInteresse;
import br.com.zup.xyinc.service.PontoInteresseService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PontoInteresseControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private HttpHeaders headers;
	private PontoInteresseService service;
	private JSONObject poiJson;
	private PontoInteresse poi;
	private String endpoint = "/v1/pontointeresse";

	private String errorValue(String jsonString, String key) {
		if (jsonString == null) return null;
		if (key == null) return null;
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		try {
			JSONObject json = (JSONObject) parser.parse(jsonString);
			JSONArray array = ((JSONArray)json.get("errors"));
			JSONObject jsonError = (JSONObject) array.get(0);
			return jsonError.getAsString(key);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  null;
	}

	private String errorType(String jsonString) {
		if (jsonString == null) return null;
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		try {
			JSONObject json = (JSONObject) parser.parse(jsonString);
			return json.getAsString("msg");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  null;
	}

	@BeforeEach
	public void setup() {

		/*
		 * PontoInteresse object of a Default Ponto de Interesse
		 */
		poi = new PontoInteresse(null, "Ponto de Interesse default", 50, 50);


		/*
		 * jasonObject of a Default Ponto de Interesse
		 */
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);

		poiJson = new JSONObject();
		poiJson.put("nomePontoInteresse", "Novo Ponto de Interesse");
		poiJson.put("coordenadaX", 27);
		poiJson.put("coordenadaY", 12);

	}

	@Test
	void getPontoInteresseByIdWhenIdIsValidExpectsFullBeanAndStatus200() throws Exception {

		// Arrange
		endpoint += "/{id}";
		poi = new PontoInteresse(1L, "Lanchonete", 27, 12);

		// Action
		ResponseEntity<PontoInteresse> response = restTemplate.getForEntity(endpoint, PontoInteresse.class, poi.getId() );

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(poi.getId(), response.getBody().getId() );
		assertEquals(poi.getNomePontoInteresse(), response.getBody().getNomePontoInteresse() );
		assertEquals(poi.getCoordenadaX(), response.getBody().getCoordenadaX() );
		assertEquals(poi.getCoordenadaY(), response.getBody().getCoordenadaY() );

	}

	@Test
	void getPontoInteresseByIdWhenIdIsInvalidExpectsStatus404() throws Exception {

		// Arrange
		endpoint += "/{id}";
		poi.setId(-1L);

		// Action
		ResponseEntity<PontoInteresse> response = restTemplate.getForEntity(endpoint, PontoInteresse.class, poi.getId() );

		// Assert
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue() );
	}

	@Test
	void listAllPontoInteresseWhenNoParamIsSuppliedExpectsAllRecordsAndStatus200() throws Exception {

		// Arrange

		// Action
		ResponseEntity<PontoInteresse[]> response = restTemplate.getForEntity(endpoint, PontoInteresse[].class );
		PontoInteresse[] list = response.getBody();

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(DefaultValues.ALL_RECORDS_QYT, list.length );

	}

	@Test
	void listAllPontoInteresseWhenNoPageIsSuppliedExpectsDefaultPageSizeRecordsAndStatus200() throws Exception {

		// Arrange
		endpoint += "/page";
		ParameterizedTypeReference<HelperPage> responseType = new ParameterizedTypeReference<HelperPage>() { };

		// Action
		ResponseEntity<HelperPage> response = restTemplate.exchange(endpoint, HttpMethod.GET, null/*httpEntity*/, responseType);
		List<PontoInteresse> list = response.getBody().getContent();

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(DefaultValues.DEFAULT_PAGE_SIZE, list.size() );
	}

	@Test
	void listAllPontoInteresseWhenPage0IsSuppliedExpectsDefaultSizeRecordsAndStatus200() throws Exception {
		// Arrange
		endpoint += "/page?page=0";
		ParameterizedTypeReference<HelperPage> responseType = new ParameterizedTypeReference<HelperPage>() { };

		// Action
		ResponseEntity<HelperPage> response = restTemplate.exchange(endpoint, HttpMethod.GET, null/*httpEntity*/, responseType);
		List<PontoInteresse> list = response.getBody().getContent();

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(DefaultValues.DEFAULT_PAGE_SIZE, list.size() );
	}

	@Test
	void listAllPontoInteresseWhenPage1IsSuppliedExpectsDefaultSizeRecordsAndStatus200() throws Exception {
		// Arrange
		endpoint += "/page?page=1";
		ParameterizedTypeReference<HelperPage> responseType = new ParameterizedTypeReference<HelperPage>() { };

		// Action
		ResponseEntity<HelperPage> response = restTemplate.exchange(endpoint, HttpMethod.GET, null/*httpEntity*/, responseType);
		List<PontoInteresse> list = response.getBody().getContent();

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(DefaultValues.DEFAULT_PAGE_SIZE, list.size() );
	}

	@Test
	void listAllPontoInteresseWhenLastPageIsSuppliedExpectsDefaultSizeRecordsAndStatus200() throws Exception {

		// Arrange
		endpoint += "/page?page=2";
		ParameterizedTypeReference<HelperPage> responseType = new ParameterizedTypeReference<HelperPage>() { };

		// Action
		ResponseEntity<HelperPage> response = restTemplate.exchange(endpoint, HttpMethod.GET, null/*httpEntity*/, responseType);
		List<PontoInteresse> list = response.getBody().getContent();

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(DefaultValues.DEFAULT_LAST_PAGE_SIZE, list.size() );

	}

	@Test
	void listAllPontoInteresseWhenPageSizeIsSuppliedExpectsSizeRecordsAndStatus200() throws Exception {

		// Arrange
		endpoint += "/page?size=10";
		ParameterizedTypeReference<HelperPage> responseType = new ParameterizedTypeReference<HelperPage>() { };

		// Action
		ResponseEntity<HelperPage> response = restTemplate.exchange(endpoint, HttpMethod.GET, null/*httpEntity*/, responseType);
		List<PontoInteresse> list = response.getBody().getContent();

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(10, list.size() );

	}

	@Test
	void listAllPontoInteresseWhenPageSizeIs10AndLastPageIsSuppliedExpects7RecordsAndStatus200() throws Exception {

		// Arrange
		endpoint += "/page?page=5&size=10";
		ParameterizedTypeReference<HelperPage> responseType = new ParameterizedTypeReference<HelperPage>() { };

		// Action
		ResponseEntity<HelperPage> response = restTemplate.exchange(endpoint, HttpMethod.GET, null/*httpEntity*/, responseType);
		List<PontoInteresse> list = response.getBody().getContent();

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(7, list.size() );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateXIs20AndCoordenateYIs10AndDistanceIs10ExpectsSpecificsRecordsAndStatus200() throws Exception {

		// Arrange
		endpoint += "/near?x={actualX}&y={actualY}&d={distance}";
		Integer actualX = 20;
		Integer actualY = 10;
		Integer distance = 10;
		Set<String> mustReturnSet = new HashSet<>();
		int index = 0;

		// Action
		ResponseEntity<PontoInteresse[]> response = restTemplate.getForEntity(endpoint, PontoInteresse[].class, actualX, actualY, distance  );
		PontoInteresse[] list = response.getBody();
		for ( PontoInteresse item : list )
			mustReturnSet.add(item.getNomePontoInteresse());

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(4, list.length );
		assertTrue( mustReturnSet.contains("Lanchonete") );
		assertTrue( mustReturnSet.contains("Joalheria") );
		assertTrue( mustReturnSet.contains("Pub") );
		assertTrue( mustReturnSet.contains("Supermercado") );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateIs5000x5000AndDistanceIs150ExpectsPointsOfCircunferenceAndStatus200() throws Exception {

		// Arrange
		endpoint += "/near?&x={actualX}&y={actualY}&d={distance}";
		Integer actualX = 5000;
		Integer actualY = 6000;
		Integer distance = 150;
		Set<String> mustReturnSet = new HashSet<>();
		int index = 0;

		// Action
		ResponseEntity<PontoInteresse[]> response = restTemplate.getForEntity(endpoint, PontoInteresse[].class, actualX, actualY, distance  );
		PontoInteresse[] list = response.getBody();
		for ( PontoInteresse item : list )
			mustReturnSet.add(item.getNomePontoInteresse());

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(6, list.length );
		assertTrue( mustReturnSet.contains("Teste d=0 - Ponto Central") );
		assertTrue( mustReturnSet.contains("Teste d<150 - Ponto dentro do círculo 01") );
		assertTrue( mustReturnSet.contains("Teste d<150 - Ponto dentro do círculo 02") );
		assertTrue( mustReturnSet.contains("Teste d=150 - Ponto na circunferência 01") );
		assertTrue( mustReturnSet.contains("Teste d=150 - Ponto na circunferência 02") );
		assertTrue( mustReturnSet.contains("Teste d=150 - Ponto na circunferência 03") );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateIs5000x5000AndDistanceIs149ExpectsInnerCircleRecordsAndStatus200() throws Exception {

		// Arrange
		endpoint += "/near?&x={actualX}&y={actualY}&d={distance}";
		Integer actualX = 5000;
		Integer actualY = 6000;
		Integer distance = 149;
		Set<String> mustReturnSet = new HashSet<>();
		int index = 0;

		// Action
		ResponseEntity<PontoInteresse[]> response = restTemplate.getForEntity(endpoint, PontoInteresse[].class, actualX, actualY, distance  );
		PontoInteresse[] list = response.getBody();
		for ( PontoInteresse item : list )
			mustReturnSet.add(item.getNomePontoInteresse());

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(3, list.length );
		assertTrue( mustReturnSet.contains("Teste d=0 - Ponto Central") );
		assertTrue( mustReturnSet.contains("Teste d<150 - Ponto dentro do círculo 01") );
		assertTrue( mustReturnSet.contains("Teste d<150 - Ponto dentro do círculo 02") );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateAndDistanceAreValidExpectsNoOuterCircleRecordsAndStatus200() throws Exception {

		// Arrange
		endpoint += "/near?&x={actualX}&y={actualY}&d={distance}";
		Integer actualX = 5000;
		Integer actualY = 6000;
		Integer distance = 150;
		Set<String> mustReturnSet = new HashSet<>();
		int index = 0;

		// Action
		ResponseEntity<PontoInteresse[]> response = restTemplate.getForEntity(endpoint, PontoInteresse[].class, actualX, actualY, distance  );
		PontoInteresse[] list = response.getBody();
		for ( PontoInteresse item : list )
			mustReturnSet.add(item.getNomePontoInteresse());

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(6, list.length );
		assertFalse( mustReturnSet.contains("Teste d=150 - Ponto no quadrilátero Q1") );
		assertFalse( mustReturnSet.contains("Teste d=150 - Ponto no quadrilátero Q2") );
		assertFalse( mustReturnSet.contains("Teste d=150 - Ponto no quadrilátero Q3") );
		assertFalse( mustReturnSet.contains("Teste d=150 - Ponto no quadrilátero Q4") );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateXIsNullExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		endpoint += "/near?&y={actualY}&d={distance}";
		Integer actualY = 10;
		Integer distance = 10;

		// Action
		ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class, actualY, distance  );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("x", errorValue(response.getBody(), "fieldName") );
		assertEquals("O parâmetro x do tipo Integer é obrigatório e não foi informado.", errorValue(response.getBody(), "message") );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateYIsNullExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		endpoint += "/near?&x={actualX}&d={distance}";
		Integer actualX = 10;
		Integer distance = 10;

		// Action
		ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class, actualX, distance  );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("y", errorValue(response.getBody(), "fieldName") );
		assertEquals("O parâmetro y do tipo Integer é obrigatório e não foi informado.", errorValue(response.getBody(), "message") );

	}

	@Test
	void listNearPontoInteresseWhenDistanceIsNullExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		endpoint += "/near?&x={actualX}&y={actualY}";
		Integer actualX = 20;
		Integer actualY = 10;

		// Action
		ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class, actualX, actualY  );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("d", errorValue(response.getBody(), "fieldName") );
		assertEquals("O parâmetro d do tipo Integer é obrigatório e não foi informado.", errorValue(response.getBody(), "message") );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateXIsNegativeExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		endpoint += "/near?x={actualX}&y={actualY}&d={distance}";
		Integer actualX = -20;
		Integer actualY = 10;
		Integer distance = 10;

		// Action
		ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class, actualX, actualY, distance  );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("listByProximity.actualX", errorValue(response.getBody(), "fieldName") );
		assertEquals("x não pode ser negativo. Valor atual é: "+actualX+".", errorValue(response.getBody(), "message") );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateYIsNegativeExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		endpoint += "/near?x={actualX}&y={actualY}&d={distance}";
		Integer actualX = 20;
		Integer actualY = -10;
		Integer distance = 10;

		// Action
		ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class, actualX, actualY, distance  );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("listByProximity.actualY", errorValue(response.getBody(), "fieldName") );
		assertEquals("y não pode ser negativo. Valor atual é: "+actualY+".", errorValue(response.getBody(), "message") );

	}

	@Test
	void listNearPontoInteresseWhenCoordenateAndDistanceAreValidAndNoPageIsSuppliedExpectsDefaultSizeRecordsAndStatus200() throws Exception {

		// Arrange
		endpoint += "/near/page?x={actualX}&y={actualY}&d={distance}";
		ParameterizedTypeReference<HelperPage> responseType = new ParameterizedTypeReference<HelperPage>() { };
		Integer actualX = 20;
		Integer actualY = 10;
		Integer distance = 1000000;

		// Action
		ResponseEntity<HelperPage> response = restTemplate.exchange(endpoint, HttpMethod.GET, null/*httpEntity*/, responseType, actualX, actualY, distance);
		List<PontoInteresse> list = response.getBody().getContent();

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(DefaultValues.DEFAULT_PAGE_SIZE, list.size() );

	}

	@Test
	void listNearPontoInteresseByPageWhenCoordenateIs5000x5000AndDistanceIs150ExpectsPointsOfCircunferenceAndStatus200() throws Exception {

		// Arrange
		endpoint += "/near/page?&x={actualX}&y={actualY}&d={distance}&page=0";
		ParameterizedTypeReference<HelperPage> responseType = new ParameterizedTypeReference<HelperPage>() { };
		Integer actualX = 5000;
		Integer actualY = 6000;
		Integer distance = 150;
		Set<String> mustReturnSet = new HashSet<>();
		int index = 0;

		// Action
		ResponseEntity<HelperPage> response = restTemplate.exchange(endpoint, HttpMethod.GET, null/*httpEntity*/, responseType, actualX, actualY, distance);
		List<PontoInteresse> list = response.getBody().getContent();
		for ( PontoInteresse item : list )
			mustReturnSet.add(item.getNomePontoInteresse());

		// Assert
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue() );
		assertEquals(6, list.size() );
		assertTrue( mustReturnSet.contains("Teste d=0 - Ponto Central") );
		assertTrue( mustReturnSet.contains("Teste d<150 - Ponto dentro do círculo 01") );
		assertTrue( mustReturnSet.contains("Teste d<150 - Ponto dentro do círculo 02") );
		assertTrue( mustReturnSet.contains("Teste d=150 - Ponto na circunferência 01") );
		assertTrue( mustReturnSet.contains("Teste d=150 - Ponto na circunferência 02") );
		assertTrue( mustReturnSet.contains("Teste d=150 - Ponto na circunferência 03") );

	}

	@Test
	void createPontoInteresseWhenDataIsValidExpectsStatus201AndHeaderLocation() throws Exception {

		// Arrange

		// Action
		ResponseEntity<PontoInteresse> response = restTemplate.postForEntity(endpoint, poi, PontoInteresse.class );
		PontoInteresse newPoi = response.getBody();

		// Assert
		assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue() );
		assertTrue( response.getHeaders().containsKey("Location") );
	}

	@Test
	void createPontoInteresseWhenXIsNullExpectsStatus404AndErrorMessage() throws Exception {

		// Arrange
		poiJson.remove("coordenadaX");

		// Action
		ResponseEntity<String> response = restTemplate.postForEntity(endpoint, poiJson, String.class );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de validação", errorType(response.getBody()) );
		assertEquals("coordenadaX", errorValue(response.getBody(), "fieldName") );
		assertEquals("A coordenada X deve ser informada.", errorValue(response.getBody(), "message") );
	}

	@Test
	void createPontoInteresseWhenYIsNullExpectsStatus404AndErrorMessage() throws Exception {

		// Arrange
		poiJson.remove("coordenadaY");

		// Action
		ResponseEntity<String> response = restTemplate.postForEntity(endpoint, poiJson, String.class );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de validação", errorType(response.getBody()) );
		assertEquals("coordenadaY", errorValue(response.getBody(), "fieldName") );
		assertEquals("A coordenada Y deve ser informada.", errorValue(response.getBody(), "message") );
	}

	@Test
	void createPontoInteresseWhenCoordenadaXIsNegativeExpectsStatus404AndErrorMessage() throws Exception {

		// Arrange
		poiJson.put("coordenadaX", -27);

		// Action
		ResponseEntity<String> response = restTemplate.postForEntity(endpoint, poiJson, String.class );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de validação", errorType(response.getBody()) );
		assertEquals("coordenadaX", errorValue(response.getBody(), "fieldName") );
		assertEquals("A coordenada X deve maior ou igual a zero.", errorValue(response.getBody(), "message") );

	}

	@Test
	void createPontoInteresseWhenCoordenadaYIsNegativeExpectsStatus404AndErrorMessage() throws Exception {

		// Arrange
		poiJson.put("coordenadaY", -12);

		// Action
		ResponseEntity<String> response = restTemplate.postForEntity(endpoint, poiJson, String.class );

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de validação", errorType(response.getBody()) );
		assertEquals("coordenadaY", errorValue(response.getBody(), "fieldName") );
		assertEquals("A coordenada Y deve maior ou igual a zero.", errorValue(response.getBody(), "message") );

	}

	@Test
	void deletePontoInteresseWhenIdIsValidExpectsStatus204() throws Exception {

		// Arrange
		endpoint += "/{id}";
		poi.setId(1L);

		// Action
		ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.DELETE, null, String.class, poi.getId());

		// Assert
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue() );

	}

	@Test
	void deletePontoInteresseWhenIdIsInvalidExpectsStatus404() throws Exception {

		// Arrange
		endpoint += "/{id}";
		poi.setId(2L);

		// Action
		ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.DELETE, null, String.class, poi.getId());

		// Assert
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue() );

	}

	@Test
	void deletePontoInteresseWhenParamIdIsNotSuppliedExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		poi = new PontoInteresse(1121L, "Nome do Ponto de Interesse foi alterado", 1150, 5011);
		HttpEntity<PontoInteresse> entity = new HttpEntity<PontoInteresse>(poi, headers);

		// Action
		ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.DELETE, entity, String.class);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("id", errorValue(response.getBody(), "fieldName") );
		assertEquals("Id do ponto de interesse não informado na URL.", errorValue(response.getBody(), "message") );

	}

	@Test
	void updatePontoInteresseWhenIdIsValidExpectsStatus200() throws Exception {

		// Arrange
		endpoint += "/{id}";
		poi = new PontoInteresse(1121L, "Nome do Ponto de Interesse foi alterado", 1150, 5011);
		HttpEntity<PontoInteresse> entity = new HttpEntity<PontoInteresse>(poi, headers);

		// Action
		ResponseEntity<String> responsePut = restTemplate.exchange(endpoint, HttpMethod.PUT, entity, String.class, poi.getId());
		int putCode = responsePut.getStatusCodeValue();
		ResponseEntity<PontoInteresse> response = restTemplate.getForEntity(endpoint, PontoInteresse.class, poi.getId() );

		// Assert
		assertEquals(HttpStatus.OK.value(), putCode );
		assertEquals(poi.getId(), response.getBody().getId() );
		assertEquals(poi.getNomePontoInteresse(), response.getBody().getNomePontoInteresse() );
		assertEquals(poi.getCoordenadaX(), response.getBody().getCoordenadaX() );
		assertEquals(poi.getCoordenadaY(), response.getBody().getCoordenadaY() );

	}

	@Test
	void updatePontoInteresseWhenIdIsInvalidExpectsStatus404AndErrorMessage() throws Exception {

		// Arrange
		endpoint += "/{id}";
		poi = new PontoInteresse(2L, "Nome do Ponto de Interesse foi alterado", 1150, 5011);
		HttpEntity<PontoInteresse> entity = new HttpEntity<PontoInteresse>(poi, headers);

		// Action
		ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.PUT, entity, String.class, poi.getId());

		// Assert
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue() );
		assertEquals("Objeto não encontrado! Id: 2, Tipo: br.com.zup.xyinc.domain.PontoInteresse", errorType(response.getBody()) );

	}

	@Test
	void updatePontoInteresseWhenParamIdNotEqualBodyIdExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		endpoint += "/{id}";
		poi = new PontoInteresse(1121L, "Nome do Ponto de Interesse foi alterado", 1150, 5011);
		int idUrl = 2;
		HttpEntity<PontoInteresse> entity = new HttpEntity<PontoInteresse>(poi, headers);

		// Action
		ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.PUT, entity, String.class, idUrl);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("id", errorValue(response.getBody(), "fieldName") );
		assertEquals("Id informado na URL difere do Id enviado no corpo da requisição.", errorValue(response.getBody(), "message") );

	}

	@Test
	void updatePontoInteresseWhenUrlHasNoIdAndCoordenadaXIsNegativeIdExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		poiJson.put("coordenadaX", -27);
		HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(poiJson, headers);

		// Action
		ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.PUT, entity, String.class);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("id", errorValue(response.getBody(), "fieldName") );
		assertEquals("Id do ponto de interesse não informado na URL.", errorValue(response.getBody(), "message") );

	}

	@Test
	void updatePontoInteresseWhenParamIdIsNotSuppliedExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		poi = new PontoInteresse(1121L, "Nome do Ponto de Interesse foi alterado", 1150, 5011);
		HttpEntity<PontoInteresse> entity = new HttpEntity<PontoInteresse>(poi, headers);

		// Action
		ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.PUT, entity, String.class);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("id", errorValue(response.getBody(), "fieldName") );
		assertEquals("Id do ponto de interesse não informado na URL.", errorValue(response.getBody(), "message") );

	}

	@Test
	void updatePontoInteresseWhenParamIdIsNotSuppliedAndHasNoBodyExpectsStatus400AndErrorMessage() throws Exception {

		// Arrange
		poi = new PontoInteresse(1121L, "Nome do Ponto de Interesse foi alterado", 1150, 5011);
		HttpEntity<PontoInteresse> entity = new HttpEntity<PontoInteresse>(poi, headers);

		// Action
		ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.PUT, null, String.class);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue() );
		assertEquals("Erro de requisição", errorType(response.getBody()) );
		assertEquals("None", errorValue(response.getBody(), "fieldName") );
		assertEquals("Required request body is missing: public org.springframework.http.ResponseEntity<?> br.com.zup.xyinc.controller.PontoInteresseController.updateBadFormed(br.com.zup.xyinc.domain.PontoInteresse)", errorValue(response.getBody(), "message") );

	}

}
