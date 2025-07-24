package br.com.alura.screenMath.controller;

import br.com.alura.screenMath.dto.EpisodioDTO;
import br.com.alura.screenMath.dto.SerieDTO;
import br.com.alura.screenMath.model.Episodio;
import br.com.alura.screenMath.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService repository;

    @GetMapping()
    public List<SerieDTO> obterSerie(){
      return repository.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Serie(){
        return repository.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamento(){
        return repository.obterLancamento();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id){
        return repository.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasAsTemporadas(@PathVariable long id){
        return repository.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable long id, @PathVariable Long numero){
        return  repository.obterTemporaPorNumero(id, numero);
    }

    @GetMapping("/categoria/{nomeGenero}")
    public List<SerieDTO> obterSeriesPorCategoria(@PathVariable String nomeGenero){
        return repository.obterSeriePorCategoria(nomeGenero);
    }

    @GetMapping("/{id}/temporadas/top5")
    public List<EpisodioDTO> obterTop5EpisodiosPorSerie(@PathVariable long id){
        return repository.obterTop5Episodios(id);
    }
}
