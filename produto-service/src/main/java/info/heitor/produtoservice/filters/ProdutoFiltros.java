package info.heitor.produtoservice.filters;

import info.heitor.produtoservice.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class ProdutoFiltros {

    private Optional<String> nome;

    private Optional<Categoria> categoria;
}
