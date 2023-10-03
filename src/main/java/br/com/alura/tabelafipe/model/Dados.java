package br.com.alura.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Dados(@JsonAlias("codigo") String codigo,
                    @JsonAlias("nome") String nome) {

    @Override
    public String toString() {
        return "Código: " + codigo + " | Descrição: " + nome;
    }
}
