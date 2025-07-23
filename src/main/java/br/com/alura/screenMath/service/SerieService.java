package br.com.alura.screenMath.service;

import br.com.alura.screenMath.dto.SerieDTO;
import br.com.alura.screenMath.model.Serie;
import br.com.alura.screenMath.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SerieService {
    @Autowired
    private SerieRepository repository;

    public List<SerieDTO>  obterTodasAsSeries(){
        return convertDados(repository.findAll());

    }

    public List<SerieDTO> obterTop5Series() {
        return convertDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> convertDados(List<Serie> series){
        return series.stream().map(s -> new SerieDTO(s.getId(),
                s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse())).collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamento() {
        return convertDados(repository.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repository.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),
                    s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                    s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse());
        }
        return null;
    }
}
