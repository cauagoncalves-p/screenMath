package br.com.alura.screenMath.repository;

import br.com.alura.screenMath.dto.EpisodioDTO;
import br.com.alura.screenMath.model.Categoria;
import br.com.alura.screenMath.model.Episodio;
import br.com.alura.screenMath.model.Serie;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long>{
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor,Double avaliacao);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

    @Query("select e from  Serie s JOIN s.episodios e WHERE  e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodioTrecho(String trechoEpisodio);

    @Query("select e from  Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("select e from Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);


    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodios e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> lancamentosMaisRecentes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodioPorTemporada(Long id, Long numero);

    @Query("select e from  Serie s JOIN s.episodios e WHERE s.id = :id ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> top5EpisodiosPorSerie(Long id);

}