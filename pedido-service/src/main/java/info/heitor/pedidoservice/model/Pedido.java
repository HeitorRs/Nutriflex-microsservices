package info.heitor.pedidoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "pedidos")
@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class Pedido {
    @Id
    private String id;
    private List<ItemPedido> items;
    private BigDecimal valorTotal;
    private String cupom;
    private BigDecimal valorTotalComDesconto;
}
