package br.com.alura.screenMath.principal;

import br.com.alura.screenMath.model.DadosEpisodio;
import br.com.alura.screenMath.model.DadosSerie;
import br.com.alura.screenMath.model.DadosTemporadas;
import br.com.alura.screenMath.model.Episodio;
import br.com.alura.screenMath.service.ConsumoAPI;
import br.com.alura.screenMath.service.ConvertDado;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;

import javax.sound.midi.Soundbank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
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

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        List<DadosEpisodio> dadosEpisodios = temporadas.stream().
                flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        dadosEpisodios.add(new DadosEpisodio("teste", 3, "10",
                "2020-01-0-"));

        System.out.println("Melhores episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println( "Primeiro filtro(N/A)" + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println(" Ordenação" + e)).limit(10)
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println(" Mapeamento" + e)).limit(10).
                forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);
        
        System.out.println("Digite um trecho do titulo do episodio: ");
        var trechoTitulo = leitura.nextLine();


        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();


        if (episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get());
        } else{
            System.out.println("Episódio não encotrado!");
        }

//        System.out.println("A partir de que ano você deseja ver os episódios?");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate databusca = LocalDate.of(ano, 1,1);
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream().filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(databusca))
//                .forEach( e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                    " Data de lançamento: " + e.getDataLancamento().format(formatador)
//                ));

    Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
            .filter(e -> e.getAvaliacao() > 0.0)
            .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio
                ::getAvaliacao));

        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());




    }
}