package br.com.alura.screenMath.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertDado implements IConvertDados{
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> Class) {
        try {
            return mapper.readValue(json, Class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
