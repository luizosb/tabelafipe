package br.com.alura.tabelafipe.principal;

import br.com.alura.tabelafipe.modelos.Dados;
import br.com.alura.tabelafipe.modelos.Modelos;
import br.com.alura.tabelafipe.modelos.Veiculo;
import br.com.alura.tabelafipe.service.ConsumoAPI;
import br.com.alura.tabelafipe.service.ConverteDados;
import ch.qos.logback.core.joran.spi.NoAutoStartUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    public static final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private Scanner leitura = new Scanner(System.in);

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();

    public void exibeMenu(){

        System.out.println("""
				**** OPÇÕES ****
				
				Carros
				
				Motos
				
				Caminhoes
				
				Digite uma das opções para consultar valores:
				""");
        String opcoes = leitura.nextLine();
        String busca = URL_BASE + opcoes.toLowerCase() + "/marcas";

        var json = consumoAPI.obterDados(busca);
        System.out.println(json);

        var marcas = converteDados.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite o número do modelo: ");
        var idModelo = leitura.nextLine();

        String buscaModelo = busca + "/" + idModelo + "/modelos";
        var jsonModelo = consumoAPI.obterDados(buscaModelo);

        var modeloLista = converteDados.obterDados(jsonModelo, Modelos.class);

        System.out.println("Modelos dessa marca");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite um trecho do nome do carro a ser buscado:");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m-> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("Modelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite por favor o codigo do modelo:");
        var codigoModelo = leitura.nextLine();

        var buscaCarro = buscaModelo + "/" + codigoModelo + "/anos";
        var jsonCarro = consumoAPI.obterDados(buscaCarro);
        List<Dados> anos = converteDados.obterLista(jsonCarro, Dados.class);

        List<Veiculo> veiculoList = new ArrayList<>();
        for (int i = 0; i < anos.size(); i++) {
            var buscaAnos = buscaCarro + "/" + anos.get(i).codigo();
            var jsonAnos = consumoAPI.obterDados(buscaAnos);
            Veiculo veiculo = converteDados.obterDados(jsonAnos, Veiculo.class);
            veiculoList.add(veiculo);
        }

        System.out.println("Todos os veiculos filtrados por ano");
        veiculoList.forEach(System.out::println);

    }
}
