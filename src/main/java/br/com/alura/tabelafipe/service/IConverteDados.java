package br.com.alura.tabelafipe.service;

import java.util.List;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
    /*
    Usamos o <T> T justamente quado não sabemos qual entidade será devolvida.
     */

    /*
    Como não especificamos o tipo no início do cabeçalho do metodo,
    em algum momento teremos que indicar o tipo que queremos. Por isso passaremos essa classe.
     */

}
