package br.com.zup.xyinc.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PontoInteresseTest {

	@Test
	void setIdWithNormalValueExpectsNormalValue() {

		// Arrange
		PontoInteresse poi = new PontoInteresse(500L, "Lan House", 27, 12);

		// Act

		// Assert
		assertEquals(500, poi.getId());

	}

	@Test
	void setIdWithNullValueExpectsNullValue() {

		// Arrange
		PontoInteresse poi = new PontoInteresse(null, "Lan House", 27, 12);

		// Act

		// Assert
		assertNull(poi.getId());

	}

	@Test
	void setNomePontoInteresseWithNullValueExpectsNullPointerException() {

		// Assert
		assertThrows( NullPointerException.class, () -> {

			// Arrange
			// Act
			PontoInteresse poi = new PontoInteresse(500L, null, 27, 12);

		});

	}

	@Test
	void setCoordenadaXWithNullValueExpectsNullPointerException() {

		// Assert
		assertThrows( NullPointerException.class, () -> {

			// Arrange
			// Act
			PontoInteresse poi = new PontoInteresse(500L, "Lan house", null, 12);

		});

	}

	@Test
	void setCoordenadaYWithNullValueExpectsNullPointerException() {

		// Assert
		assertThrows( NullPointerException.class, () -> {

			// Arrange
			// Act
			PontoInteresse poi = new PontoInteresse(500L, "Lan house", 27, null);

		});

	}

	@Test
	void setCoordenadaXWithNegativeValueExpectsIllegalArgumentException() {

		// Assert
		assertThrows( IllegalArgumentException.class, () -> {

			// Arrange
			// Act
			PontoInteresse poi = new PontoInteresse(500L, "Lan house", -10, null);

		});

	}

	@Test
	void setCoordenadaYWithNegativeValueExpectsIllegalArgumentException() {

		// Assert
		assertThrows( IllegalArgumentException.class, () -> {

			// Arrange
			// Act
			PontoInteresse poi = new PontoInteresse(500L, "Lan house", 10, -27);

		});

	}

	@Test
	void fillPontoInteresseWithNormalValuesExpectsNormalValues() {

		// Arrange
		PontoInteresse poi = new PontoInteresse(1L, "Lan House", 27, 12);

		// Act

		// Assert
		assertEquals(1, poi.getId());
		assertEquals("Lan House", poi.getNomePontoInteresse());
		assertEquals(27, poi.getCoordenadaX());
		assertEquals(12, poi.getCoordenadaY());

	}

	@Test
	void checkIsGeneratingToStringWillSuppliedValues() {
		// Arrange
		PontoInteresse poi = new PontoInteresse(1L, "Lan House", 27, 12);

		// Act
		String toString = poi.toString();

		// Assert
		assertEquals("PontoInteresse{id=1, coordenadaX=27, coordenadaY=12, nomePontoInteresse='Lan House'}", toString);

	}

	@Test
	void setTwoObjectsWithSameIdExpectsEquality() {
		// Arrange
		PontoInteresse poi1 = new PontoInteresse(1L, "Lan House", 27, 12);
		PontoInteresse poi2 = new PontoInteresse(1L, "Lan House", 27, 12);

		// Act


		// Assert
		assertTrue(poi1.equals(poi2));
		assertFalse(poi1 == poi2);

	}

	@Test
	void checkPontoInteresseGeneratesHashCode() {
		// Arrange
		PontoInteresse poi = new PontoInteresse(1L, "Lan House", 27, 12);

		// Act
		Integer hashCode = poi.hashCode();

		// Assert
		assertTrue(hashCode > 0);

	}

	@Test
	void checkPontoInteresseCloneWorks() {
		// Arrange
		PontoInteresse poi = new PontoInteresse(1L, "Lan House", 27, 12);

		// Act
		PontoInteresse clonedPoi = poi.clone();

		// Assert
		assertNotNull(clonedPoi);
		assertNotSame(poi, clonedPoi);
		assertEquals(poi.getId(), clonedPoi.getId() );
		assertEquals(poi.getNomePontoInteresse(), clonedPoi.getNomePontoInteresse() );
		assertEquals(poi.getCoordenadaX(), clonedPoi.getCoordenadaX() );
		assertEquals(poi.getCoordenadaY(), clonedPoi.getCoordenadaY() );
		assertEquals(poi, clonedPoi);
	}

}
