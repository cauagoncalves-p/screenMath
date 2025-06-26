package br.com.alura.screenMath.principal;

import br.com.alura.screenMath.model.DadosEpisodio;
import br.com.alura.screenMath.model.DadosSerie;
import br.com.alura.screenMath.model.DadosTemporadas;
import br.com.alura.screenMath.service.ConsumoAPI;
import br.com.alura.screenMath.service.ConvertDado;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;

import javax.sound.midi.Soundbank;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConvertDado conversor = new ConvertDado();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b2f8e6b";


    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporadas> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);

            DadosTemporadas dadosTemporada = conversor.obterDados(json, DadosTemporadas.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
//
//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporadas.size(); j++) {
//                System.out.println(episodiosTemporadas.get(j).titulo());
//            }
//        }

        temporadas.forEach(t ->t.episodios().forEach(e -> System.out.println(e.titulo())));
        List<DadosEpisodio> dadosEpisodios = temporadas.stream().
                flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        dadosEpisodios.add(new DadosEpisodio("teste", 3, "10",
                "2020-01-0-"));

        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")).sorted
                        (Comparator.comparing(DadosEpisodio::avaliacao)
                        .reversed()).limit(5).
                        forEach(System.out::println);
    }

}