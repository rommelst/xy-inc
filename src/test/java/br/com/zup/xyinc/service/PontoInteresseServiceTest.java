package br.com.zup.xyinc.service;

import br.com.zup.xyinc.domain.PontoInteresse;
import br.com.zup.xyinc.repository.PontoInteresseRepository;
import br.com.zup.xyinc.service.exception.BadRequestException;
import br.com.zup.xyinc.service.exception.ObjectNotFoundException;
import net.bytebuddy.TypeCache;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {PontoInteresseRepository.class, PontoInteresseService.class})
@SpringBootTest
public class PontoInteresseServiceTest {

    @MockBean
    private PontoInteresseRepository repository;

    @Autowired
    private PontoInteresseService service;

    @Test
    public void createWhenDataIsValidExpectedPersistedData() {

        // Arrange
        PontoInteresse sentPoi = new PontoInteresse(7000L, "Ponto Interesse", 20, 30);
        BDDMockito.when(repository.save(sentPoi)).thenReturn(new PontoInteresse(125L, "Ponto Interesse", 20, 30));
        BDDMockito.when(repository.existsById(7000L)).thenReturn(false);
        BDDMockito.when(repository.existsById(125L)).thenReturn(true);

        // Action
        PontoInteresse savedPoi = service.insert(sentPoi);

        // Assert
        assertTrue(repository.existsById(125L));
        assertFalse(repository.existsById(7000L));
        assertNull(sentPoi.getId());
        assertNotSame(sentPoi, savedPoi);
        assertEquals("Ponto Interesse", savedPoi.getNomePontoInteresse());
        assertEquals(20, savedPoi.getCoordenadaX());
        assertEquals(30, savedPoi.getCoordenadaY());

    }

    @Test
    public void updateWhenDataIsValidExpectedPersistedData() {

        // Arrange
        PontoInteresse poi = new PontoInteresse(1L, "Ponto Interesse", 20, 30);
        BDDMockito.when(repository.findById(poi.getId())).thenReturn(Optional.of(poi.clone()));

        // Action
        service.update(poi);
        PontoInteresse savedPoi = service.find(poi.getId());

        // Assert
        assertNotNull(savedPoi);
        assertNotSame(poi, savedPoi);
        assertEquals(poi.getId(), savedPoi.getId());
        assertEquals(poi.getNomePontoInteresse(), savedPoi.getNomePontoInteresse());
        assertEquals(poi.getCoordenadaX(), savedPoi.getCoordenadaX());
        assertEquals(poi.getCoordenadaY(), savedPoi.getCoordenadaY());

    }

    @Test
    public void deleteWhenDataIsValidExpectedRecordIsRemoved() {

        // Arrange
        PontoInteresse poi = new PontoInteresse(1L, "Ponto Interesse", 20, 30);
        BDDMockito.doNothing().when(repository).deleteById(poi.getId());
        BDDMockito.when(repository.findById(poi.getId())).thenReturn(Optional.of(poi.clone()));
        BDDMockito.when(repository.existsById(poi.getId())).thenReturn(false);

        // Act
        service.delete(poi.getId());

        // Assert
        assertFalse(repository.existsById(poi.getId()));
        ;
    }

    @Test
    public void findPontoInteresseWhenIdIsSuppliedExpectsSpecificsRecords() {

        // Arrange
        BDDMockito
                .when(repository.findById(125L))
                .thenReturn( Optional.of(new PontoInteresse(125L, "Ponto Interesse", 20, 30)))
        ;

        // Action
        PontoInteresse poi = service.find(125L);

        // Assert
        assertEquals( 125, poi.getId());
        assertEquals( "Ponto Interesse", poi.getNomePontoInteresse());
        assertEquals( 20, poi.getCoordenadaX());
        assertEquals( 30, poi.getCoordenadaY());

    }

    @Test
    public void findPontoInteresseWhenIdIsInvalidExpectsException() {

        // Arrange
        BDDMockito
                .when(repository.findById(125L))
                .thenReturn(Optional.empty())
        ;

        // Assert
        assertThrows(ObjectNotFoundException.class, () -> {

            // Action
            PontoInteresse poi = service.find(125L);

        });

    }

    @Test
    public void listPontoInteresseExpectsAllRecords() {

        // Arrange
        List<PontoInteresse> list = asList(
                new PontoInteresse(1L, "Lanchonete", 27, 12),
                new PontoInteresse(41L, "Joalheria", 15, 12),
                new PontoInteresse(41L, "Pub", 15, 12),
                new PontoInteresse(41L, "Supermercado", 15, 12)
        );

        BDDMockito
                .when(repository.findAll())
                .thenReturn(list)
        ;
        Set<String> mustReturnSet = new HashSet<>();
        int index = 0;

        // Action
        list = service.list();

        // Assert
        assertEquals(4, list.size() );
    }

    @Test
    public void listPontoInteresseByPageExpectsAllRecords() {

        // Arrange
        Pageable pageable = PageRequest.of(1, 20);
        List<PontoInteresse> list = asList(
                new PontoInteresse(1L, "Lanchonete", 27, 12),
                new PontoInteresse(41L, "Joalheria", 15, 12),
                new PontoInteresse(41L, "Pub", 15, 12),
                new PontoInteresse(41L, "Supermercado", 15, 12)
        );
        Page<PontoInteresse> mockPage = new PageImpl<PontoInteresse>(list, pageable, list.size());
        BDDMockito
                .when(repository.findAll(pageable))
                .thenReturn(mockPage)
        ;

        // Action
        Page<PontoInteresse> page = service.page(pageable);

        // Assert
        assertEquals(4, page.getContent().size() );
    }

    @Test
    public void listNearPontoInteresseWhenCoordenada20x10AndDistance10ExpectsSpecificsRecords() {

        // Arrange
        PageRequest pageRequest = PageRequest.of(1, 20);
        int actualX = 20;
        int actualY = 10;
        int distance = 10;
        List<PontoInteresse> list = asList(
                new PontoInteresse(1L, "Lanchonete", 27, 12),
                new PontoInteresse(41L, "Joalheria", 15, 12),
                new PontoInteresse(41L, "Pub", 15, 12),
                new PontoInteresse(41L, "Supermercado", 15, 12)
                );

        BDDMockito
            .when(repository.findByCoordenadaXBetweenAndCoordenadaYBetween(
                actualX - distance,
                actualX + distance,
                actualY - distance,
                actualY + distance))
            .thenReturn(list)
        ;
        Set<String> mustReturnSet = new HashSet<>();
        int index = 0;

        // Action
        list = service.listNear(actualX, actualY, distance);
        for ( PontoInteresse item : list )
            mustReturnSet.add(item.getNomePontoInteresse());

        // Assert
        assertEquals(4, list.size() );
        assertTrue( mustReturnSet.contains("Lanchonete") );
        assertTrue( mustReturnSet.contains("Joalheria") );
        assertTrue( mustReturnSet.contains("Pub") );
        assertTrue( mustReturnSet.contains("Supermercado") );

    }

    @Test
    public void listNearPontoInteresseByPageWhenParamAndPageIsSuppliedExpectsSpecificsRecords() {

        // Arrange
        PageRequest pageable = PageRequest.of(1, 20);
        int actualX = 20;
        int actualY = 10;
        int distance = 10;
        List<PontoInteresse> list = asList(
                new PontoInteresse(1L, "Lanchonete", 27, 12),
                new PontoInteresse(41L, "Joalheria", 15, 12),
                new PontoInteresse(41L, "Pub", 15, 12),
                new PontoInteresse(41L, "Supermercado", 15, 12)
        );
        Page<PontoInteresse> mockPage = new PageImpl<PontoInteresse>(list, pageable, list.size());
        BDDMockito
            .when(repository.searchNear(actualX, actualY, distance, pageable))
            .thenReturn(mockPage)
        ;

        // Action
        Page<PontoInteresse> page = service.listNearWithPage(actualX, actualY, distance, pageable);

        // Assert
        assertEquals(4, page.getContent().size() );

    }

    @Test
    public void validateUrlHasIdWhenParamAreValidExpectsNothing() {

        // Arrange
        PontoInteresse poi = new PontoInteresse(90L, "Ponto Interesse", 20, 30);

        // Assert
        assertDoesNotThrow( () -> {

            // Act
            service.validateUrlHasId(poi, 90L);

        });

    }

    @Test
    public void validateUrlHasIdWhenParamAreInvalidExpectsBadRequestException() {

        // Arrange
        PontoInteresse poi = new PontoInteresse(90L, "Ponto Interesse", 20, 30);

        // Assert
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {

            // Act
            service.validateUrlHasId(poi, 10L);

        });
        assertEquals("id", thrown.getFieldName());
        assertEquals("Id informado na URL difere do Id enviado no corpo da requisição.", thrown.getMessage());

    }

    @Test
    public void validateUrlHasIdWhenObjIdIsNullExpectsBadRequestException() {

        // Arrange
        PontoInteresse poi = new PontoInteresse(null, "Ponto Interesse", 20, 30);

        // Assert
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {

            // Act
            service.validateUrlHasId(poi, 10L);

        });
        assertEquals("id", thrown.getFieldName());
        assertEquals("Id informado na URL difere do Id enviado no corpo da requisição.", thrown.getMessage());

    }

    @Test
    public void validateSortWhenParamIsValidExpectsNothing() {

        // Arrange

        // Assert
        assertDoesNotThrow( () -> {

            // Act
            service.validateSortColumn(PageRequest.of(0,20, Sort.Direction.ASC, "id").getSort());
            service.validateSortColumn(PageRequest.of(0,20, Sort.Direction.ASC, "nomePontoInteresse").getSort());
            service.validateSortColumn(PageRequest.of(0,20, Sort.Direction.ASC, "coordenadaX").getSort());
            service.validateSortColumn(PageRequest.of(0,20, Sort.Direction.ASC, "coordenadaY").getSort());

        });

    }

    @Test
    public void validateSortWhenParamIsInvalidExpectsException() {

        // Arrange

        // Assert
        BadRequestException thrown = assertThrows( BadRequestException.class, () -> {

            // Act
            service.validateSortColumn(PageRequest.of(0,20, Sort.Direction.ASC, "nome_ponto_interesse").getSort());

        });
        assertEquals("sort", thrown.getFieldName());
        assertEquals("O parâmetro sort aceita os seguintes valores nomePontoInteresse | coordenadaX | coordenadaY.", thrown.getMessage());

    }

}