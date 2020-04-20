package br.com.zup.xyinc.repository;

import br.com.zup.xyinc.DefaultValues;
import br.com.zup.xyinc.domain.PontoInteresse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PontoInteresseRepositoryTest {

    @Autowired
    private PontoInteresseRepository repository;

    @Test
    public void creteWhenDataIsValidExpectPersistedData() {

        // Arrange
        PontoInteresse obj = new PontoInteresse(null, "Burger King", 27, 12);

        // Action
        this.repository.save(obj);

        // Assert
        assertNotNull(obj.getId());
        assertEquals("Burger King", obj.getNomePontoInteresse());
        assertEquals(27, obj.getCoordenadaX());
        assertEquals(12, obj.getCoordenadaY());

    }

    @Test
    public void deleteWhenDataIsValidExpectsPersistedData() {

        // Arrange
        PontoInteresse obj = new PontoInteresse(null, "Burger King", 27, 12);

        // Action
        this.repository.save(obj);
        this.repository.delete(obj);
        Optional<PontoInteresse> opt = repository.findById(obj.getId());

        // Assert
        assertTrue(opt.isEmpty());
    }

    @Test
    public void updateWhenDataIsValidExpectsPersistedData() {

        // Arrange
        PontoInteresse newObj = new PontoInteresse(null, "Burger King", 27, 12);
        PontoInteresse updateObj = new PontoInteresse(null, "Burger King 2", 28, 13);

        // Action
        this.repository.save(newObj);
        updateObj.setId(newObj.getId());
        this.repository.save(updateObj);
        Optional<PontoInteresse> opt = repository.findById(updateObj.getId());

        // Assert
        assertTrue(opt.isPresent());
        assertEquals(newObj.getId(), opt.get().getId());
        assertEquals("Burger King 2", opt.get().getNomePontoInteresse());
        assertEquals( 28, opt.get().getCoordenadaX());
        assertEquals( 13, opt.get().getCoordenadaY());
    }

    @Test
    public void findAllExpectsReturnAllRecords() {

        // Arrange

        // Action
        List<PontoInteresse> list = repository.findAll();

        // Assert
        assertEquals(DefaultValues.ALL_RECORDS_QYT, list.size());
    }

    @Test
    public void createOrUpdateWhenNomePontoInteresseIsNullExpectsExceptionNullPointerException() {

        // Assert
        assertThrows( NullPointerException.class, () -> {

            // Arrange
            // Act
            System.out.println("Não é possível enviar null para o campo nome_ponto_interesse pois o model não aceita nulos.");
            PontoInteresse poi = new PontoInteresse(500L, null, 27, 12);

        });

    }

    @Test
    public void createOrUpdateWhenCoordenadaXIsNullExpectsException() {

        // Assert
        assertThrows( NullPointerException.class, () -> {

            // Arrange
            // Act
            System.out.println("Não é possível enviar null para o campo coordenadaX pois o model não aceita nulos.");
            PontoInteresse poi = new PontoInteresse(500L, "", null, 12);

        });

    }

    @Test
    public void createOrUpdateWhenCoordenadaXIsNegativeExpectsException() {

        // Assert
        assertThrows( IllegalArgumentException.class, () -> {

            // Arrange
            // Act
            System.out.println("Não é possível enviar # negativos para o campo coordenadaX pois o model não aceita nulos.");
            PontoInteresse poi = new PontoInteresse(500L, "", -9, 12);

        });

    }

    @Test
    public void createOrUpdateWhenCoordenadaYIsNullExpectsException() {

        // Assert
        assertThrows( NullPointerException.class, () -> {

            // Arrange
            // Act
            System.out.println("Não é possível enviar null para o campo coordenadaX pois o model não aceita nulos.");
            PontoInteresse poi = new PontoInteresse(500L, "", 10, null);

        });

    }

    @Test
    public void createOrUpdateWhenCoordenadaYIsNegativeExpectsException() {

        // Assert
        assertThrows( IllegalArgumentException.class, () -> {

            // Arrange
            // Act
            System.out.println("Não é possível enviar # negativos para o campo coordenadaX pois o model não aceita nulos.");
            PontoInteresse poi = new PontoInteresse(500L, "", 10, -12);

        });

    }

    @Test
    public void searchNearWhenDataIsValidExpectsValidRecords() {

        // Arrange
        int actualX = 20;
        int actualY = 10;
        int distance = 10;
        PageRequest pageable = PageRequest.of(0, 20);
        Set<String> mustReturnSet = new HashSet<>();
        int index = 0;

        // Action
        Page<PontoInteresse> page = repository.searchNear(actualX, actualY, distance, pageable);
        List<PontoInteresse> list = page.getContent();
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
    public void findByCoordenadaXBetweenAndCoordenadaYBetweenWhenDataIsValidExpectsRecordsOfQuadrilateralEvenOutsideCircle() {

        // Arrange
        int actualX = 5000;
        int actualY = 6000;
        int distance = 150;
        PageRequest pageable = PageRequest.of(0, 20);
        Set<String> mustReturnSet = new HashSet<>();
        int index = 0;

        // Action
        List<PontoInteresse> list = repository.findByCoordenadaXBetweenAndCoordenadaYBetween(
                actualX - distance,
                actualX + distance,
                actualY - distance,
                actualY + distance
        );
        for ( PontoInteresse item : list )
            mustReturnSet.add(item.getNomePontoInteresse());

        // Assert
        assertEquals(10, list.size() );
        assertTrue( mustReturnSet.contains("Teste d>150 - Ponto no quadrilátero Q1") );
        assertTrue( mustReturnSet.contains("Teste d>150 - Ponto no quadrilátero Q2") );
        assertTrue( mustReturnSet.contains("Teste d>150 - Ponto no quadrilátero Q3") );
        assertTrue( mustReturnSet.contains("Teste d>150 - Ponto no quadrilátero Q4") );


    }

}