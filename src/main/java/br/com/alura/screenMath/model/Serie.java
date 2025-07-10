package br.com.alura.screenMath.model;

import br.com.alura.screenMath.service.traducao.ConsultaMyMemory;

import java.util.OptionalDouble;

public class Serie {
  private String titulo;
  private Integer totalTemporadas;
  private Double avaliacao;
  private Categoria genero;
  private String atores;
  private String poster;
  private String sinopse;

  public Serie(DadosSerie dadosSerie){
      this.titulo = dadosSerie.titulo();
      this.totalTemporadas = dadosSerie.totalTemporadas();
      this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
      this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
      this.atores = dadosSerie.atores();
      this.poster = dadosSerie.poster();
      this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
  }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        return
                " genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'';

    }
}
