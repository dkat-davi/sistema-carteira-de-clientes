USE carteira_clientes;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE atendimento;
TRUNCATE TABLE cliente;
TRUNCATE TABLE usuario_loja;
TRUNCATE TABLE loja;
TRUNCATE TABLE usuario;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO usuario (
    id,
    nome,
    cpf,
    login,
    senha,
    role,
    ativo
) VALUES
(1, 'Administrador do Sistema', '00000000000', 'admin', 'admin123', 'ADMIN', TRUE),
(2, 'Ana Paula Martins', '11111111111', 'ana', '123456', 'USUARIO', TRUE),
(3, 'Bruno Henrique Silva', '22222222222', 'bruno', '123456', 'USUARIO', TRUE),
(4, 'Carla Fernanda Rocha', '33333333333', 'carla', '123456', 'USUARIO', TRUE),
(5, 'Daniel Oliveira Santos', '44444444444', 'daniel', '123456', 'USUARIO', TRUE),
(6, 'Elaine Cristina Lima', '55555555555', 'elaine', '123456', 'USUARIO', TRUE),
(7, 'Fernando Souza Almeida', '66666666666', 'fernando', '123456', 'USUARIO', TRUE);

INSERT INTO loja (
    id,
    nome,
    telefone,
    endereco,
    cidade,
    estado,
    ativo
) VALUES
(1, 'Loja Centro', '(38) 3218-5300', 'Rua Principal, 100 - Centro', 'Montes Claros', 'MG', TRUE),
(2, 'Loja Major Prates', '(38) 3218-5301', 'Avenida Major Prates, 250', 'Montes Claros', 'MG', TRUE),
(3, 'Loja Janaúba', '(38) 3218-5302', 'Rua das Flores, 50 - Centro', 'Janaúba', 'MG', TRUE);

INSERT INTO usuario_loja (
    id,
    usuario_id,
    loja_id,
    cargo
) VALUES
(1, 1, 1, 'GERENTE'),
(2, 1, 2, 'GERENTE'),
(3, 1, 3, 'GERENTE'),

(4, 2, 1, 'GERENTE'),
(5, 3, 1, 'VENDEDOR'),
(6, 4, 1, 'VENDEDOR'),

(7, 5, 2, 'GERENTE'),
(8, 6, 2, 'VENDEDOR'),

(9, 7, 3, 'GERENTE'),
(10, 4, 3, 'VENDEDOR');

INSERT INTO cliente (
    id,
    nome,
    cpf,
    telefone,
    email,
    endereco,
    loja_id,
    vendedor_id
) VALUES
(1, 'João Batista Ferreira', '77777777777', '(38) 99911-1001', 'joao.ferreira@email.com', 'Rua A, 120', 1, 3),
(2, 'Maria Aparecida Gomes', '88888888888', '(38) 99922-1002', 'maria.gomes@email.com', 'Rua B, 220', 1, 3),
(3, 'Carlos Eduardo Lima', '99999999999', '(38) 99933-1003', 'carlos.lima@email.com', 'Rua C, 330', 1, 4),
(4, 'Patrícia Alves Rocha', '12121212121', '(38) 99944-1004', 'patricia.rocha@email.com', 'Rua D, 440', 1, 4),

(5, 'Roberto Martins Souza', '13131313131', '(38) 99955-1005', 'roberto.souza@email.com', 'Rua E, 550', 2, 6),
(6, 'Fernanda Cristina Dias', '14141414141', '(38) 99966-1006', 'fernanda.dias@email.com', 'Rua F, 660', 2, 6),
(7, 'Marcelo Henrique Costa', '15151515151', '(38) 99977-1007', 'marcelo.costa@email.com', 'Rua G, 770', 2, 6),

(8, 'Sandra Regina Melo', '16161616161', '(38) 99988-1008', 'sandra.melo@email.com', 'Rua H, 880', 3, 4),
(9, 'Paulo César Andrade', '17171717171', '(38) 99999-1009', 'paulo.andrade@email.com', 'Rua I, 990', 3, 4),
(10, 'Luciana Teixeira Ramos', '18181818181', '(38) 98888-1010', 'luciana.ramos@email.com', 'Rua J, 1010', 3, 4);

INSERT INTO atendimento (
    id,
    cliente_id,
    usuario_id,
    data_atendimento,
    descricao
) VALUES
(1, 1, 3, '2026-06-01 09:15:00', 'Cliente entrou em contato para atualização cadastral.'),
(2, 1, 3, '2026-06-03 14:30:00', 'Realizado retorno e confirmado interesse em nova negociação.'),
(3, 2, 3, '2026-06-04 10:00:00', 'Cliente solicitou informações sobre atendimento presencial.'),
(4, 3, 4, '2026-06-05 11:20:00', 'Cliente atualizado na carteira do vendedor responsável.'),
(5, 4, 4, '2026-06-06 16:45:00', 'Atendimento realizado por WhatsApp. Cliente pediu retorno posterior.'),

(6, 5, 6, '2026-06-07 08:50:00', 'Primeiro contato realizado com sucesso.'),
(7, 6, 6, '2026-06-08 13:10:00', 'Cliente informou interesse e solicitou simulação.'),
(8, 7, 6, '2026-06-09 15:25:00', 'Cliente não respondeu. Agendado novo contato.'),

(9, 8, 4, '2026-06-10 09:40:00', 'Cliente da loja Janaúba vinculado ao vendedor Carla.'),
(10, 9, 4, '2026-06-11 12:00:00', 'Atendimento concluído. Cliente orientado sobre documentação.'),
(11, 10, 4, '2026-06-12 17:30:00', 'Cliente solicitou novo contato no próximo dia útil.'),

(12, 2, 2, '2026-06-13 10:15:00', 'Gerente acompanhou o atendimento do cliente.'),
(13, 5, 5, '2026-06-14 11:45:00', 'Gerente revisou o histórico do cliente.'),
(14, 8, 7, '2026-06-15 14:00:00', 'Gerente da loja Janaúba acompanhou atendimento.');