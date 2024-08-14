-- Tabela CATEGORIA
INSERT INTO CATEGORIA (nome) VALUES
                                 ('Suplementos'),
                                 ('Vitaminas');

-- Tabela PRODUTO
INSERT INTO PRODUTO (nome, descricao, preco, estoque, categoria_id, codigo) VALUES
                                                                                ('Whey Protein', 'Proteína do soro do leite', 150.00, 100, 1, CONCAT('PROD-', UUID())),
                                                                                ('Creatina', 'Suplemento para aumento de força', 90.00, 200, 1, CONCAT('PROD-', UUID())),
                                                                                ('Vitamina C', 'Suplemento vitamínico', 30.00, 300, 2, CONCAT('PROD-', UUID()));


-- Tabela HISTORICO_PRODUTO
-- Exemplos de ações de criação de produtos
INSERT INTO HISTORICO_PRODUTO (acao, codigo_produto, data_hora) VALUES
                                                                    ('CRIACAO', (SELECT codigo FROM PRODUTO WHERE id = 1), '2024-06-17T10:15:30'),
                                                                    ('CRIACAO', (SELECT codigo FROM PRODUTO WHERE id = 2), '2024-06-17T10:20:30'),
                                                                    ('CRIACAO', (SELECT codigo FROM PRODUTO WHERE id = 3), '2024-06-17T10:25:30');