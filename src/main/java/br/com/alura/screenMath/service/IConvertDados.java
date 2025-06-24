package br.com.alura.screenMath.service;

public interface IConvertDados {
    <T> T obterDados(String json, Class<T> Class);
}
