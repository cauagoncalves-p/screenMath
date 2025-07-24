package br.com.alura.screenMath.service;

import br.com.alura.screenMath.dto.EpisodioDTO;
import br.com.alura.screenMath.dto.SerieDTO;
import br.com.alura.screenMath.model.Categoria;
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
        return convertDados(repository.lancamentosMaisRecentes());
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

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporaPorNumero(long id, Long numero) {
        return repository.obterEpisodioPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriePorCategoria(String nomeGenero) {
        Categoria categoria = Categoria.fromPortuues(nomeGenero);
        return convertDados(repository.findByGenero(categoria));
    }


    public List<EpisodioDTO> obterTop5Episodios(long id) {
        return repository.top5EpisodiosPorSerie(id).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(),e.getNumeroEpisodio(), e.getTitulo())).collect(Collectors.toList());
    }
}
