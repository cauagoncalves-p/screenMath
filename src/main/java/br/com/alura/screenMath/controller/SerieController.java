package br.com.alura.screenMath.controller;

import br.com.alura.screenMath.dto.SerieDTO;
import br.com.alura.screenMath.model.Serie;
import br.com.alura.screenMath.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {
    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> obterSerie(){
        return repository.findAll()
                .stream().map(s -> new SerieDTO(s.getId(),
                s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse())).collect(Collectors.toList());
    }

    @GetMapping("/inicio")
    public String retornarInicio(){
        return "Bem vindo opa" ;

    }
}
