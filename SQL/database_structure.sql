CREATE SCHEMA stage;

CREATE TABLE stage.pronome (
    id INT NOT NULL AUTO_INCREMENT,
    terceira_pessoa TEXT NOT NULL,
    posse TEXT NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO stage.pronome(terceira_pessoa, posse)
VALUES 
    ('ele', 'dele'),
    ('ela', 'dela'),
    ('elu', 'delu');

select * from stage.pronome;


CREATE TABLE stage.usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    public_id CHAR(36) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    pronome INT NOT NULL,
    unique_token CHAR(4) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (public_id),
    UNIQUE (email),
    UNIQUE (unique_token),
    FOREIGN KEY (pronome)
        REFERENCES stage.pronome(id)
);

CREATE TABLE stage.relacionamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_a_id BIGINT NOT NULL,
    usuario_b_id BIGINT NOT NULL,
    relacionamento_ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (usuario_a_id)
        REFERENCES stage.usuarios(id),
    FOREIGN KEY (usuario_b_id)
        REFERENCES stage.usuarios(id),
    CHECK (usuario_a_id <> usuario_b_id)
);

CREATE TABLE stage.categorias (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (nome)
);

INSERT INTO stage.categorias(nome)
VALUES 
    ('Restaurante'),
    ('Barzinho'),
    ('Sorveteria'),
    ('Cafeteria'),
    ('Parque'),
    ('Cinema'),
    ('Balada'),
    ('Livraria'),
    ('Museu'),
    ('Teatro'),
    ('Outros');

CREATE TABLE stage.lugares (
    id BIGINT NOT NULL AUTO_INCREMENT,
    public_id CHAR(36) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    categoria_id INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (public_id),
    FOREIGN KEY (categoria_id)
        REFERENCES stage.categorias(id)
);

INSERT INTO stage.lugares(public_id, nome, categoria_id)
VALUES 
    (UUID(), 'O Espanhol', 1),
    (UUID(), 'Boussolé Gastrobar', 1),
    (UUID(), 'Strassberg', 1),
    (UUID(), 'Barolo Trattoria', 1),
    (UUID(), 'Serafinni', 1),
    (UUID(), 'Empório Araçá', 1),
    (UUID(), 'Villa Fontana', 1),
    (UUID(), 'La Gondola', 1),
    (UUID(), 'Galpão Nelore', 1),
    (UUID(), 'Coco Bambu Londrina', 1),
    (UUID(), 'Koala Sushi', 1),
    (UUID(), 'Matsuri', 1),
    (UUID(), 'Guanciale', 1),
    (UUID(), 'Zuppa', 1),
    (UUID(), 'Bar do Tomio', 2),
    (UUID(), 'Pier Santa Monica', 2),
    (UUID(), 'Flannigan''s Irish Pub', 2),
    (UUID(), 'Menina Bar', 2),
    (UUID(), 'Bar Valentino', 2),
    (UUID(), 'Maximo Villa', 2),
    (UUID(), 'Cheers Irish Pub', 2),
    (UUID(), 'Oley', 2),
    (UUID(), 'Gelobel', 3),
    (UUID(), 'Freddo Gelateria', 3),
    (UUID(), 'Chiquinho Sorvetes', 3),
    (UUID(), 'Bacio di Latte', 3),
    (UUID(), 'Hachimitsu', 4),
    (UUID(), 'O Armazém Café', 4),
    (UUID(), 'Nelson Boulangerie', 4),
    (UUID(), 'Fura Bolo', 4),
    (UUID(), 'Doceria da Maria', 4),
    (UUID(), 'Kopenhagen', 4),
    (UUID(), 'Mais1 Café', 4),
    (UUID(), 'Lago Igapó', 5),
    (UUID(), 'Jardim Botânico de Londrina', 5),
    (UUID(), 'Zerão', 5),
    (UUID(), 'Bosque Marechal Cândido Rondon', 5),
    (UUID(), 'Praça Nishinomiya', 5),
    (UUID(), 'Multiplex Catuaí', 6),
    (UUID(), 'Cinemark Boulevard', 6),
    (UUID(), 'Cineflix Aurora', 6),
    (UUID(), 'Lumière', 6),
    (UUID(), 'FOLKS Pub Sertanejo', 7),
    (UUID(), 'Escritório Bar', 7),
    (UUID(), 'Livrarias Curitiba (Catuaí)', 8),
    (UUID(), 'Sebo Capricho', 8),
    (UUID(), 'Livraria da Vila (Aurora)', 8),
    (UUID(), 'Museu Histórico de Londrina', 9),
    (UUID(), 'Museu de Arte de Londrina', 9),
    (UUID(), 'Teatro Ouro Verde', 10),
    (UUID(), 'Teatro Marista', 10),
    (UUID(), 'Mercado Municipal de Londrina', 11),
    (UUID(), 'Catuaí Shopping', 11),
    (UUID(), 'Boulevard Shopping', 11),
    (UUID(), 'Aurora Shopping', 11),
    (UUID(), 'Calçadão de Londrina', 11);


CREATE TABLE stage.avaliacao_individual (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    lugar_id BIGINT NOT NULL,
    relacionamento_id bigint not null,
	nota_pergunta_1 DECIMAL(3,1) NOT NULL,
	nota_pergunta_2 DECIMAL(3,1) NOT NULL,
	nota_pergunta_3 DECIMAL(3,1) NOT NULL,
	nota_pergunta_4 DECIMAL(3,1) NOT NULL,
	nota_media DECIMAL(4,2)
	    GENERATED ALWAYS AS (
	        ROUND(
	            (
	                nota_pergunta_1 +
	                nota_pergunta_2 +
	                nota_pergunta_3 +
	                nota_pergunta_4
	            ) / 4,
	            2
	        )
	    ) STORED,
	descricao TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL
    	DEFAULT CURRENT_TIMESTAMP
    	ON UPDATE CURRENT_TIMESTAMP,
	post_visibility BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (usuario_id)
        REFERENCES stage.usuarios(id),
    FOREIGN KEY (lugar_id)
        REFERENCES stage.lugares(id),
	FOREIGN KEY (relacionamento_id)
        REFERENCES stage.relacionamentos(id),       
    CHECK (nota_pergunta_1 BETWEEN 0 AND 10),
    CHECK (nota_pergunta_2 BETWEEN 0 AND 10),
    CHECK (nota_pergunta_3 BETWEEN 0 AND 10),
    CHECK (nota_pergunta_4 BETWEEN 0 AND 10),
    UNIQUE (relacionamento_id, usuario_id, lugar_id)
);

CREATE TABLE stage.fotos_avaliacao_individual (
    id BIGINT NOT NULL AUTO_INCREMENT,
    avaliacao_id BIGINT NOT NULL,
    url VARCHAR(2048) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (avaliacao_id)
        REFERENCES stage.avaliacao_individual(id)
        ON DELETE CASCADE
);

CREATE TABLE stage.casal_page_respostas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    relacionamento_id BIGINT NOT NULL,
    foto_perfil_url VARCHAR(500),
    resposta_1 TEXT,
    resposta_2 TEXT,
    resposta_3 TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY uq_casal_page_usuario (
        relacionamento_id,
        usuario_id
    ),
    FOREIGN KEY (usuario_id)
    REFERENCES stage.usuarios(id),
	FOREIGN KEY (relacionamento_id)
    REFERENCES stage.relacionamentos(id)
);

CREATE TABLE stage.seguidores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    relacionamento_id BIGINT NOT NULL,
    seguidor_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (relacionamento_id, seguidor_id),
    FOREIGN KEY (relacionamento_id)
        REFERENCES stage.relacionamentos(id),
    FOREIGN KEY (seguidor_id)
        REFERENCES stage.usuarios(id)
);
CREATE TABLE IF NOT EXISTS stage.casal_page_respostas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    relacionamento_id BIGINT NOT NULL,
    foto_perfil_url VARCHAR(500),
    resposta_1 TEXT,
    resposta_2 TEXT,
    resposta_3 TEXT,
    UNIQUE KEY uk_casal_page_rel_user (relacionamento_id, usuario_id),
    CONSTRAINT fk_casal_page_user FOREIGN KEY (usuario_id) REFERENCES stage.usuarios (id),
    CONSTRAINT fk_casal_page_rel FOREIGN KEY (relacionamento_id) REFERENCES stage.relacionamentos (id)
);
