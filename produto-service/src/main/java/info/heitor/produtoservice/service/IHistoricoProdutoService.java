package info.heitor.produtoservice.service;


import info.heitor.produtoservice.model.HistoricoProduto;

import java.util.List;

public interface IHistoricoProdutoService {

    public List<HistoricoProduto> buscarTodos();
}
