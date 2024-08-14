package info.heitor.produtoservice.controller;


import info.heitor.produtoservice.model.HistoricoProduto;
import info.heitor.produtoservice.service.HistoricoProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/historicos")
@RequiredArgsConstructor
public class HistoricoProdutoController {

    private final HistoricoProdutoService historicoProdutoService;

    @GetMapping
    public List<HistoricoProduto> listar() {
        return historicoProdutoService.buscarTodos();
    }
}
