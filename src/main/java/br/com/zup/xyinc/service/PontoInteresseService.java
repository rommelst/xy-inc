package br.com.zup.xyinc.service;

import br.com.zup.xyinc.domain.PontoInteresse;
import br.com.zup.xyinc.repository.PontoInteresseRepository;
import br.com.zup.xyinc.service.exception.BadRequestException;
import br.com.zup.xyinc.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PontoInteresseService {

    private final PontoInteresseRepository repository;

    @Autowired
    public PontoInteresseService(PontoInteresseRepository repository){
        this.repository = repository;
    }

    public PontoInteresse insert(PontoInteresse obj) {
        obj.setId(null);
        return repository.save(obj);
    }

    public PontoInteresse update(PontoInteresse obj) {
        find(obj.getId());
        return repository.save(obj);
    }

    public void delete(Long id) {
        find(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly=true)
    public PontoInteresse find(Long id) {
        Optional<PontoInteresse> obj = repository.findById(id);
        return obj.orElseThrow(
                () -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + PontoInteresse.class.getName())
        );
    }

    @Transactional(readOnly=true)
    public List<PontoInteresse> list() {
        return repository.findAll();
    }

    @Transactional(readOnly=true)
    public Page<PontoInteresse> page(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly=true)
    public List<PontoInteresse> listNear(Integer atualX, Integer atualY, Integer distancia) {

        //Obtem todos os pontos de interesse do quadrilátero formado a partir da coordenada e da distância
        List<PontoInteresse> list = repository.findByCoordenadaXBetweenAndCoordenadaYBetween(atualX-distancia, atualX+distancia, atualY-distancia, atualY+distancia);

        // Remove todos os pontos de interesse que estão no quadrilátero mas não estão no raio da circunferência
        list.removeIf( e -> {
            double value = Math.sqrt(
                (e.getCoordenadaX()-atualX)*(e.getCoordenadaX()-atualX) +
                (e.getCoordenadaY()-atualY)*(e.getCoordenadaY()-atualY)
            );
            return value>distancia;
        });

        return list;
    }

    @Transactional(readOnly=true)
    public Page<PontoInteresse> listNearWithPage(Integer atualX, Integer atualY, Integer distancia, Pageable pageable) {
        Page<PontoInteresse> page = repository.searchNear(atualX, atualY, distancia, pageable);
        return page;
    }

    public void validateUrlHasId(PontoInteresse obj, Long id) {
        if (obj.getId().compareTo(id) != 0) {
            throw new BadRequestException("id", "Id informado na URL difere do Id enviado no corpo da requisição.");
        }
        return;
    }

    public void validateSortColumn(Sort sort) {
        if (sort == null) return;
        if (sort.isUnsorted()) return;
        boolean valid = true;
        for (Sort.Order item : sort) {
            String fieldName = item.getProperty();
            if ( fieldName.compareTo("id")==0 ) continue;
            if ( fieldName.compareTo("nomePontoInteresse")==0 ) continue;
            if ( fieldName.compareTo("coordenadaX")==0 ) continue;
            if ( fieldName.compareTo("coordenadaY")==0 ) continue;
            valid=false;
            break;
        }
        if (valid) return;
        throw new BadRequestException("sort", "O parâmetro sort aceita os seguintes valores nomePontoInteresse | coordenadaX | coordenadaY.");
    }
}
