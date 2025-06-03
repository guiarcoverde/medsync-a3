CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) UNIQUE,
                          email VARCHAR(100),
                          senha_hash VARCHAR(255),
                          data_nascimento DATE,
                          genero VARCHAR(20),
                          tipo_sanguineo VARCHAR(3),
                          altura_cm DECIMAL(5,2),
                          peso_kg DECIMAL(5,2),
                          doencas_cronicas TEXT,
                          alergias TEXT,
                          medicamentos TEXT,
                          contato_emergencia_nome VARCHAR(100),
                          contato_emergencia_telefone VARCHAR(20),
                          criado_em DATETIME
);

CREATE TABLE consultas (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           usuario_id BIGINT NOT NULL,
                           nome_medico VARCHAR(100),
                           especialidade VARCHAR(100),
                           data DATE,
                           hora TIME,
                           eh_recorrente TINYINT(1),
                           observacoes TEXT,
                           criado_em DATETIME,
                           FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE consultas_recorrentes (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       consulta_id BIGINT NOT NULL,
                                       tipo_recorrencia VARCHAR(20),
                                       intervalo INT,
                                       data_fim DATE,
                                       FOREIGN KEY (consulta_id) REFERENCES consultas(id)
);
