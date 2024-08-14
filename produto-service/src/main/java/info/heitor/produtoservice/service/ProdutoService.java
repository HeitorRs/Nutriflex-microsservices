package info.heitor.produtoservice.service;


import info.heitor.produtoservice.filters.ProdutoFiltros;
import info.heitor.produtoservice.model.AcaoHistorico;
import info.heitor.produtoservice.model.HistoricoProduto;
import info.heitor.produtoservice.model.Produto;
import info.heitor.produtoservice.repository.HistoricoProdutoRepository;
import info.heitor.produtoservice.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProdutoService implements IProdutoService {

    private final ProdutoRepository produtoRepository;
    private final HistoricoProdutoRepository historicoProdutoRepository;
    private final EntityManager entityManager;


    @Override
    public List<Produto> buscarFiltros(ProdutoFiltros filtros) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
        Root<Produto> produto = cq.from(Produto.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filtros.getNome().isPresent()) {
            String query = filtros.getNome().get() + "%";
            Predicate nome = cb.like(produto.get("nome"), query);
            predicates.add(nome);
        }

        if (filtros.getCategoria() != null && filtros.getCategoria().isPresent()) {
            Predicate categoria = cb.equal(produto.get("categoria"), filtros.getCategoria().get());
            predicates.add(categoria);
        }

        Predicate[] array = predicates.toArray(Predicate[]::new);
        cq.where(array);
        List<Produto> resultList = entityManager.createQuery(cq).getResultList();
        return resultList;
    }

    @Override
    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    @Override
    @Transactional
    public Produto salvarProduto(Produto produto) {
        validarProduto(produto);
        produto.setCodigo("PROD-" + UUID.randomUUID().toString());
        Produto produtoSalvo = produtoRepository.save(produto);
        registrarHistorico(produtoSalvo.getCodigo(), AcaoHistorico.CRIACAO);
        return produtoSalvo;
    }


    @Override
    @Transactional
    public Optional<Produto> encontrarProdutoPorId(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido!");
        }
        Optional<Produto> produto = produtoRepository.findById(id);
        produto.ifPresent(p -> registrarHistorico(p.getCodigo(), AcaoHistorico.CONSULTA));
        return produto;
    }

    @Override
    @Transactional
    public void deletarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        registrarHistorico(produto.getCodigo(),AcaoHistorico.EXCLUSAO);

        produtoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido!");
        }
        validarProduto(produtoAtualizado);
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto de ID: " + id + " não encontrado"));

        // Copia os dados atualizados para o produto existente
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setEstoque(produtoAtualizado.getEstoque());
        produtoExistente.setCategoria(produtoAtualizado.getCategoria());

        Produto produtoAtualizadoSalvo = produtoRepository.save(produtoExistente);
        registrarHistorico(produtoAtualizadoSalvo.getCodigo(), AcaoHistorico.ATUALIZACAO);

        return produtoAtualizadoSalvo;
    }

    private void validarProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode estar em branco");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero");
        }
        if (produto.getEstoque() < 0) {
            throw new IllegalArgumentException("Estoque do produto não pode ser negativo");
        }
    }

    private void registrarHistorico(String codigoProduto, AcaoHistorico acao) {
        HistoricoProduto historico = new HistoricoProduto();
        historico.setCodigoProduto(codigoProduto);
        historico.setAcao(acao);
        historico.setDataHora(LocalDateTime.now());
        historicoProdutoRepository.save(historico);
    }
}
