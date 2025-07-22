package br.com.alura.screenMath.dto;

import br.com.alura.screenMath.model.Categoria;

public record SerieDTO(
        long id, String titulo,
        Integer totalTemporadas,
        Double avaliacao,
        Categoria genero,
        String atores,
        String poster,
        String sinopse
) {
}
