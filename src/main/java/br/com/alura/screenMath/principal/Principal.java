package br.com.alura.screenMath.principal;

import br.com.alura.screenMath.model.*;
import br.com.alura.screenMath.repository.SerieRepository;
import br.com.alura.screenMath.service.ConsumoAPI;
import br.com.alura.screenMath.service.ConvertDado;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;

import javax.sound.midi.Soundbank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
public class Principal {
    private final SerieRepository repository;
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConvertDado conversor = new ConvertDado();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b2f8e6b";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private Optional<Serie> seriebusca;


    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repository = repositorio;
    }


    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar séries por titulo
                    5 - Buscar séries por ator
                    6 - Buscar top 5 avaliações
                    7 - Buscar série por gênero
                    8 - Buscar séries por temporadas
                    9 - Buscar séries por Trecho
                    10 - Top 5 episodios  por série
                    11 - Buscar episodios apartir de uma data
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTopSeries();
                    break;
                case 7:
                    buscarSeriePorGenero();
                    break;
                case 8:
                    buscarSeriesPorTemporadas();
                    break;
                case 9:
                    buscrSeriesPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha um série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);


        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporadas dadosTemporada = conversor.obterDados(json, DadosTemporadas.class);
                temporadas.add(dadosTemporada);
            }

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
            temporadas.forEach(System.out::println);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();

        seriebusca =  repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (seriebusca.isPresent()){
            System.out.println("Dados da série: " + seriebusca.get());
        }else{
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Qual o nome para busca");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações apartir de que valor?");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou");
        seriesEncontradas.forEach(s -> System.out.println( s.getTitulo() + " avaliação " + s.getAvaliacao()));
    }

    private void buscarTopSeries() {
        List<Serie> serieTop = repository.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s -> System.out.println( s.getTitulo() + " avaliação " + s.getAvaliacao()));
    }

    private void buscarSeriePorGenero() {
        System.out.println("Deseja buscar séries de que categoria/gênero?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortuues(nomeGenero);
        List<Serie> seriesCategoria = repository.findByGenero(categoria);

        System.out.println("Séries da categoria " + nomeGenero);
        seriesCategoria.forEach(System.out::println);
    }

    private void buscarSeriesPorTemporadas() {
        System.out.println("Deseja ver séries com até quantas temporadas");
        var temporadas = leitura.nextInt();
        System.out.println("Deseja ver series com que nota?");
        var nota  = leitura.nextDouble();

        List<Serie> serieTemporada = repository.seriesPorTemporadaEAvaliacao(temporadas,nota);
        System.out.println("Séries com o total de temporadas: " + temporadas + " e com a nota acima de : " + nota);

        serieTemporada.forEach(s -> System.out.println(s.getTitulo() + "Avaliações " + s.getAvaliacao()));
    }

    private void buscrSeriesPorTrecho() {
        System.out.println("Qual o nome do episódio para busca?");
        var trechoEpisodio = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repository.episodioTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (seriebusca.isPresent()) {
            Serie serie = seriebusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao() ));
        }
    }

    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if (seriebusca.isPresent()) {
            Serie serie = seriebusca.get();
            System.out.println("Digite o ano limite de lançamento ");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repository.episodiosPorSerieEAno(serie,anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }

}
