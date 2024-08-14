package info.heitor.produtoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "PRODUTO")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int estoque;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(nullable = false, unique = true)
    private String codigo;

    @PrePersist
    public void prePersist() {
        // Gera o código automaticamente usando um UUID
        this.codigo = "PROD-" + UUID.randomUUID().toString();
    }
}
