package info.heitor.pedidoservice.controller;

import feign.FeignException;
import info.heitor.pedidoservice.model.DescontoResponsePayload;
import info.heitor.pedidoservice.model.Pedido;
import info.heitor.pedidoservice.repository.PedidoRepository;
import info.heitor.pedidoservice.service.DescontoService;
import info.heitor.pedidoservice.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final DescontoService descontoService;

    @PostMapping
    public ResponseEntity<Pedido> addPedido(@RequestBody Pedido pedido) {
        BigDecimal valorTotal = pedidoService.valorTotalPedido(pedido);
        pedido.setValorTotal(valorTotal);

        if (pedido.getCupom() != null && !pedido.getCupom().isEmpty()) {
            try {
                DescontoResponsePayload valorComDesconto = descontoService.getTotalDesconto(pedido);
                BigDecimal result = valorComDesconto.getValorTotalComDesconto();
                pedido.setValorTotalComDesconto(result);
            } catch (Exception e) {
                pedido.setValorTotalComDesconto(valorTotal);
            }
        } else {
            pedido.setValorTotalComDesconto(BigDecimal.ZERO);
        }

        Pedido saved = pedidoService.salvarPedido(pedido);
        return ResponseEntity.ok(saved);
    }
}
