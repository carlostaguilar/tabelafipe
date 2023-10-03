package br.com.alura.tabelafipe.principal;

import br.com.alura.tabelafipe.model.Dados;
import br.com.alura.tabelafipe.model.Modelos;
import br.com.alura.tabelafipe.model.Veiculo;
import br.com.alura.tabelafipe.service.ConsumoAPI;
import br.com.alura.tabelafipe.service.ConverteDados;
import org.springframework.ui.Model;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Principal {

    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private final String CARROS = "carros/marcas";
    private final String MOTOS = "motos/marcas";
    private final String CAMINHOES = "caminhoes/marcas";

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private String json;
    private String endereco;

    public void menu() {

        System.out.println("""
                *** OPÇÕES ***
                1 - Carros
                2 - Motos
                3 - Caminhões
                """);

        int opcao = sc.nextInt();

        switch (opcao) {

            case 1:

                endereco = ENDERECO + CARROS;
                json = consumoAPI.sendRequest(endereco);
                exec(endereco);
                break;

            case 2:

                endereco = ENDERECO + MOTOS;
                json = consumoAPI.sendRequest(endereco);
                exec(endereco);
                break;

            case 3:

                endereco = ENDERECO + CAMINHOES;
                json = consumoAPI.sendRequest(endereco);
                exec(endereco);
                break;


            default:
                System.out.println("Opção inválida!");
        }
    }

    private void exec(String endereco) {

        List<Dados> dados = conversor.obterLista(json, Dados.class);

        System.out.println("Lista de Veículos: ");
        dados.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
        System.out.println();

        System.out.print("Digite o Código da marca para pesquisa: ");
        int codigoMarca  = sc.nextInt();
        sc.nextLine();
        System.out.println();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumoAPI.sendRequest(endereco);

        var modeloLista = conversor.obterDados(json, Modelos.class);


        System.out.println("Lista de marcas: ");
        modeloLista.dados().stream()
                           .sorted(Comparator.comparing(Dados::codigo))
                           .forEach(System.out::println);
        System.out.println();

        System.out.print("Digite um modelo para ser pesquisado: ");
        String nomePesquisa = sc.nextLine();

        List<Dados> carroBuscado = modeloLista.dados().stream()
                                                        .filter(m -> m.nome().toUpperCase()
                                                                             .contains(nomePesquisa.toUpperCase()))
                                                        .sorted(Comparator.comparing(Dados::codigo))
                                                        .collect(Collectors.toList());

        System.out.println("Modelos filtrados: ");
        carroBuscado.forEach(System.out::println);
        System.out.println();

        System.out.print("Digite um código do modelo para busca: ");
        int codigoModelo  = sc.nextInt();
        sc.nextLine();
        System.out.println();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumoAPI.sendRequest(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {

            var enderecoAnos = endereco + "/" +  anos.get(i).codigo();
            json = consumoAPI.sendRequest(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);

        }

        System.out.println("Todos os veículos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);

    }


}
