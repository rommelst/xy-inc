package br.com.zup.xyinc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.zup.xyinc.domain.PontoInteresse;


@Repository
public interface PontoInteresseRepository extends JpaRepository<PontoInteresse, Long> {

	
	@Query(
		value = " SELECT * FROM PontoInteresse WHERE " +
				" coordenadaX BETWEEN :actualX - :distancia AND :actualX + :distancia AND coordenadaY BETWEEN :actualY - :distancia AND :actualY + :distancia " +
				" AND sqrt( (coordenadax-:actualX)*(coordenadax-:actualX) + (coordenaday-:actualY)*(coordenaday-:actualY) ) <= (:distancia) "
		, countQuery = " SELECT Count(*) FROM PontoInteresse WHERE coordenadaX BETWEEN :actualX - :distancia AND :actualX + :distancia AND coordenadaY BETWEEN :actualY - :distancia AND :actualY + :distancia AND sqrt( (coordenadax-:actualX)*(coordenadax-:actualX) + (coordenaday-:actualY)*(coordenaday-:actualY) ) <= (:distancia) "
		, nativeQuery = true
	)
	Page<PontoInteresse> searchNear(@Param("actualX") Integer actualX, @Param("actualY") Integer actualY, @Param("distancia") Integer distancia, Pageable page);

	// Retorna somente o quadrado baseado na coordenada e na distancia informada.
	// Obs: A tabela foi indexada por esses dois campos para otimizar o consulta.
	List<PontoInteresse> findByCoordenadaXBetweenAndCoordenadaYBetween(Integer minX, Integer maxX, Integer minY, Integer maxY);

}
