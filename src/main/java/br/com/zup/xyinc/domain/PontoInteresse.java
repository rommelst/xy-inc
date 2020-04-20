package br.com.zup.xyinc.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Access(AccessType.FIELD)
@Entity(name="pontointeresse")
@Table(indexes = {
		@Index(columnList = "coordenadax, coordenaday", name = "idx_XY")
})
public class PontoInteresse implements Serializable {
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="poi_g")
	@SequenceGenerator(name="poi_g", sequenceName = "pontointeresse_seq", allocationSize = 20)
	private Long id;

	@NotNull(message = "A coordenada X deve ser informada.")
	@Min( value = 0, message = "A coordenada X deve maior ou igual a zero.")
	private final Integer coordenadaX;

	@NotNull(message = "A coordenada Y deve ser informada.")
	@Min( value = 0, message = "A coordenada Y deve maior ou igual a zero.")
	private final Integer coordenadaY;

	@NotNull(message = "O nome do ponto de interesse deve ser informado.")
	private final String nomePontoInteresse;

	/*
	 * 	Is private to keep class immutable while hibernate uses the default constructor.
	 */
	private PontoInteresse() {
		this.nomePontoInteresse = null;
		this.coordenadaX = null;
		this.coordenadaY = null;
	}

	public PontoInteresse(Long id, String nomePontoInteresse, Integer coordenadaX, Integer coordenadaY) {
		if (nomePontoInteresse == null) throw new NullPointerException();
		if (coordenadaX == null) throw new NullPointerException();
		if (coordenadaX < 0 ) throw new IllegalArgumentException("Coordenada X deve ser maior ou igual a zero.");
		if (coordenadaY == null) throw new NullPointerException();
		if (coordenadaY < 0 ) throw new IllegalArgumentException("Coordenada Y deve ser maior ou igual a zero.");
		this.id = id;
		this.coordenadaX = coordenadaX;
		this.coordenadaY = coordenadaY;
		this.nomePontoInteresse = nomePontoInteresse;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long key) {
		this.id = key;
	}

	public Integer getCoordenadaX() {
		return coordenadaX;
	}

	public Integer getCoordenadaY() {
		return coordenadaY;
	}

	public String getNomePontoInteresse() {
		return this.nomePontoInteresse;
	}

	@Override
	public String toString() {
		return "PontoInteresse{" +
				"id=" + id +
				", coordenadaX=" + coordenadaX +
				", coordenadaY=" + coordenadaY +
				", nomePontoInteresse='" + nomePontoInteresse + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PontoInteresse that = (PontoInteresse) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public PontoInteresse clone() {
		return new PontoInteresse(this.id, this.nomePontoInteresse, this.coordenadaX, this.coordenadaY);
	}
}
