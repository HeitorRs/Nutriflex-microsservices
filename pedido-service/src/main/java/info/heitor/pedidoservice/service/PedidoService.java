package info.heitor.pedidoservice.service;

import info.heitor.pedidoservice.model.ItemPedido;
import info.heitor.pedidoservice.model.Pedido;
import info.heitor.pedidoservice.model.Produto;
import info.heitor.pedidoservice.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ProdutoService produtoService;

    public Pedido salvarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public BigDecimal valorTotalPedido(Pedido pedido) {
        return pedido.getItens().stream().map(this::valorTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal valorTotalItem(ItemPedido itemPedido) {
        Produto produto = produtoService.findById(itemPedido.getProdutoId());
        return produto.getPreco().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
    }

    public Optional<Pedido> procurarPorId(String id) {
        return pedidoRepository.findById(id);
    }
}
