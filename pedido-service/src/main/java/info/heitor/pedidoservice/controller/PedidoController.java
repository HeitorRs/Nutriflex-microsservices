package info.heitor.pedidoservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.Body;
import feign.FeignException;
import info.heitor.pedidoservice.model.DescontoResponsePayload;
import info.heitor.pedidoservice.model.Pedido;
import info.heitor.pedidoservice.model.PedidoStatus;
import info.heitor.pedidoservice.repository.PedidoRepository;
import info.heitor.pedidoservice.service.DescontoService;
import info.heitor.pedidoservice.service.PagamentoService;
import info.heitor.pedidoservice.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final DescontoService descontoService;
    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        log.info("Criando pedido: {}", pedido);
        pedido.criarSerial();
        pedido.setValorTotal(pedidoService.valorTotalPedido(pedido));

        if (pedido.getCupom() != null && !pedido.getCupom().isEmpty()) {
            try {
                DescontoResponsePayload valorComDesconto = descontoService.getTotalDesconto(pedido);
                BigDecimal result = valorComDesconto.getValorTotalComDesconto();
                pedido.setValorTotalComDesconto(result);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Cupom inválido");
            }
        } else {
            pedido.setValorTotalComDesconto(null);
        }
        pedido.setStatus(PedidoStatus.CRIADO);
        Pedido saved = pedidoService.salvarPedido(pedido);

        pagamentoService.processarPagamento(pedido);

        return ResponseEntity.ok(saved);
    }

   @GetMapping("/{id}")
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable("id") String id) {
        log.info("Buscando pedido por id: {}", id);
        Optional<Pedido> pedido = pedidoService.procurarPorId(id);
        return ResponseEntity.ok(pedido);
   }
}
