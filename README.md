# Documentação Técnica e Arquitetura do Sistema CoupleTour

A presente atividade do curso Ciência de Dados e Inteligência Artificial da Universidade Estadual de Londrina, disciplina Laboratório de Programação, ministrada pelo professor Gustavo T. Naozuka tem o objetivo de desenvolver uma aplicação web completa, aplicando os conceitos abordados ao longo do bimestre 3° Bimestre.

Autores:
- Gabriel Antônio Fabian Dors
- Leonardo Madeira Alves Pereira

## 1. Arquitetura Geral

O sistema CoupleTour adota a arquitetura MVC clássica baseada no framework Spring Boot. A aplicação opera de maneira monolítica, realizando renderização de páginas do lado do servidor via Thymeleaf. A infraestrutura de persistência apoia-se em banco de dados relacional MySQL. Arquivos binários são processados na aplicação e armazenados permanentemente no serviço Google Cloud Storage.

## 2. Estrutura do Banco de Dados

O banco de dados utiliza o esquema nomeado "stage". O projeto define tabelas estritas, empregando normalização e integridade referencial. 

Tabelas fundamentais:
* "usuarios": Armazena identificadores únicos extraídos de provedores externos, chaves internas geradas dinamicamente e chaves estrangeiras apontando para pronomes preferenciais.
* "lugares" e "categorias": Cadastro de destinos avaliados e seus respectivos agrupamentos lógicos.
* "relacionamentos": Estabelece conexões bilaterais entre dois perfis de usuário, possuindo um estado booleano de atividade.
* "avaliacao_individual": Mantém opiniões, descrições, notas seccionadas e configurações de visibilidade de maneira unívoca para cada usuário a respeito de um lugar.
* "fotos_avaliacao_individual": Armazena os caminhos lógicos remotos de imagens atreladas a uma avaliação, possuindo exclusão em cascata configurada no banco de dados.
* "casal_page_respostas": Armazena textos estáticos e URLs de imagens do perfil público do casal.
* "seguidores": Tabela pivot que vincula a chave de um usuário observador à chave primária de um relacionamento seguido, possuindo restrição de unicidade para evitar duplicações.

## 3. Autenticação e Segurança

A barreira de segurança funciona inteiramente sem uso das bibliotecas do Spring Security, operando através de implementações manuais de manipulação de cookies e tokens JWT.

O componente central de autenticação é a classe JwtAuthFilter, que implementa a interface Filter nativa do pacote servlet. Todas as requisições HTTP interceptadas são submetidas a inspeções de cabeçalhos e verificação de presença do cookie "jwt_token". Quando o token está presente, o filtro submete a assinatura do token ao JwtService. Caso o artefato criptográfico seja atestado como válido e não expirado, o filtro insere o atributo "usuarioId" diretamente na requisição corrente. 

Rotas estáticas e páginas de inicialização pública estão isentas da interceptação através de lógicas condicionais que verificam URIs conhecidas antes de engatilhar o redirecionamento forçado para a raiz do domínio.

## 4. Camada de Entidades (Modelos)

Esta camada mapeia as tabelas do esquema "stage" para objetos da linguagem Java, utilizando as anotações do ecossistema JPA (Java Persistence API).

As propriedades destas classes representam diretamente as colunas do banco de dados. Os relacionamentos mapeados com a anotação ManyToOne são configurados exclusivamente com a estratégia de busca FetchType.LAZY. Isso garante que instâncias aninhadas como objetos da classe Usuario ou Categoria não sejam recuperados automaticamente ao carregar um registro da classe AvaliacaoIndividual, poupando recursos operacionais do banco de dados. 

A entidade Relacionamento mantém explicitamente o relacionamento entre dois objetos Usuario, denominados na classe como usuarioA e usuarioB, garantindo traçabilidade das conexões interpessoais em todo o código.

## 5. Camada de Transferência (DTOs e Projeções)

Os Data Transfer Objects (DTOs) isolam e protegem as entidades mapeadas do banco de dados do contato direto com a camada de controladores e visualizações web.

A camada adota "Projections" nativas providas pelo ecossistema Spring Data JPA. Interfaces arquitetadas como FeedProjection e CasalSeguidoProjection definem apenas métodos "getter". Ao executar as junções pesadas nos repositórios, a interface do Hibernate converte os dados literais selecionados diretamente em instâncias concretas destas interfaces. Isso elimina a necessidade de inicializar as entidades base e injetá-las em construtores de DTOs manualmente.

Classes de manipulação complexa como CasalPageViewDTO funcionam como agregadores pesados. O CasalPageViewDTO transporta simultaneamente dados individuais do usuário A, do usuário B, dados de acompanhamento de seguidores e coleções de projeções utilizadas para renderizar blocos dinâmicos da interface web.

## 6. Camada de Acesso a Dados (Repositórios)

Os Repositórios definem interfaces que abstraem a comunicação direta com o banco de dados via JpaRepository.

Ao invés de dependência integral em métodos gerados automaticamente pelo Spring Data JPA, este projeto utiliza extensivamente anotações do tipo "@Query" declaradas como Native Queries. 

O arquivo FeedRepository.java é o componente crítico do acesso a dados. Ele ignora mapeamentos Hibernate em prol de consultas SQL brutas contendo cláusulas LEFT JOIN encadeadas, agregadores GROUP_CONCAT e manipulações cronológicas via instruções nativas do banco MySQL (INTERVAL). Esta abordagem garante a recuperação completa do Feed e fusão das notas isoladas do casal em um único disparo consolidado contra o sistema gerenciador de banco de dados.

## 7. Camada de Negócio (Serviços)

Onde ocorre a validação condicional e integração de fluxo, protegida pela anotação Transactional.

A classe CasalPageService é responsável por consolidar metadados dispersos no banco de dados. Ela realiza verificações constantes para descobrir qual usuário solicitou a página, compara contra os dois integrantes do relacionamento e constrói respostas seguras baseando-se no nível de acesso.

A classe PostagemService orquestra gravações de dados interdependentes. O fluxo inicia associando entidades secundárias e procede para o armazenamento de texto. Simultaneamente, este serviço itera sobre arquivos de imagem providos em requisições MultipartFile, delegando a subida dos binários para a classe GcpStorageService e aguardando resoluções externas antes de comitar URLs de forma persistente.

A classe VinculacaoService gerencia as máquinas de estado de relacionamentos. Produz chaves criptografadas de convite, valida expirações parciais e muda definitivamente o estado booleano de vinculação ativa que dita a visibilidade global do casal nos feeds principais.

## 8. Camada de Rotas e Apresentação (Controladores)

Os Controladores injetam dependências estritas das classes de Serviço. Nenhuma classe final da categoria Controller detém referências diretas para interfaces da categoria Repository, assegurando o princípio MVC clássico e mitigando fugas lógicas.

Anotações de mapeamento explícitas diferenciam requisições GET, POST, PUT e DELETE. Todos os Controladores compartilham da assinatura nativa de métodos contendo parâmetros HttpServletRequest para recuperar chaves injetadas e RedirectAttributes para gerar atributos voláteis de vida curta empregados em mensagens de alertas web interativos.

A arquitetura do Spring foi reconfigurada via arquivo de propriedades para aceitar o HiddenHttpMethodFilter. Isso permite o processamento de atualizações e remoções via protocolo HTTP restrito, oriundos de formulários HTML elementares que injetam entradas escondidas simulando a intenção de transição de estado.

## 9. Visão e Frontend (Thymeleaf)

A interface baseia-se em templates da linguagem Thymeleaf instalados localmente sob a pasta de recursos estáticos.

Estruturas lógicas como o atributo "th:if" avaliam dados complexos transitados pelo Model, ocultando e exibindo botões cruciais dependendo das flags de permissão calculadas dinamicamente nos DTOs. Funções da biblioteca "strings" integrada ao Thymeleaf cortam porções e realizam formatações de data e hora em memória durante o processo de geração da string de resposta HTML, reduzindo a carga do banco de dados na conversão fuso horária do formato universal UTC.

Formulários são desenhados em HTML5 padrão submetendo pacotes URL encoded ou multipartes dependendo do transporte fotográfico. Elementos estilizados empregam diretamente bibliotecas externas contendo sistemas de grid pré compilados e manipuladores visuais embutidos localmente no escopo do documento via propriedades customizadas no CSS.

## 10. Fluxos de Domínio Essenciais

Processo de Vincular e Desvincular:
O aplicativo atrela o início de conexões utilizando o algoritmo JWT assinado pelo serviço local, transmutando a chave do remetente em um pacote assinado. O destinatário engatilha a quebra do pacote através da rota associada. A verificação procede pela checagem de parcerias existentes. Em caso afirmativo a relação é atualizada para ativa. O processo de desvincular engatilha o sinal inverso no relacionamento, inabilitando instantaneamente as projeções conjuntas via consultas filtradas condicionalmente em toda a extensão do Repositório central de avaliações.

Processo de Avaliação de Múltiplos Arquivos:
A submissão flui desde formulários estáticos, sendo mapeada automaticamente em vetores de objetos MultipartFile pelo manipulador do controlador. Fotos marcadas explicitamente para exclusão pela interface são enviadas paralelamente através de propriedades contendo arrays de strings lógicas de deleção. O Serviço analisa os subconjuntos de entrada, efetua comandos locais de deleção em banco e simultaneamente executa requisições remotas (via SDK próprio ativado por credenciais ambientais providas via base64 decodificada em tempo real) para provisionar os novos arquivos na nuvem.

Processo de Visibilidade e Segurança:
Os posts possuem atributos nulos validados por coalescências manuais em SpEL nas páginas HTML. Um atributo omisso representa validação irrestrita. Atributos registrados como falsos nas postagens isolam completamente as ocorrências da exibição em consultas públicas, retendo-as contudo nas consultas exclusivas limitadas ao espaço reservado de navegação do relacionamento atrelado ao usuário.
