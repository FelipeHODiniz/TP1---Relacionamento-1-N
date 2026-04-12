# TP1 — Relacionamento 1:N
**Trabalho Prático — Algoritmos e Estruturas de Dados III**

---

## Participantes

- Felipe Henrique Oliveira Diniz
- Matheus de Almeida Moreira

---

## Descrição do Sistema

O sistema, chamado **EntrePares 1.0**, é uma aplicação de linha de comando para gerenciamento de usuários e cursos com persistência em arquivos binários. Foi desenvolvido em Java seguindo o padrão arquitetural **MVC (Model-View-Controller)** e implementa as estruturas de dados de indexação **Tabela Hash Extensível** e **Árvore B+** para garantir acesso eficiente aos registros em disco.

### Funcionalidades Principais

- **Cadastro e autenticação de usuários**: o usuário se registra informando nome, e-mail, senha, pergunta e resposta secreta. A senha e a resposta secreta são armazenadas como hashes MD5.
- **Gerenciamento do perfil**: após o login, o usuário pode alterar seus dados (nome, e-mail, pergunta/resposta secreta) ou deletar a conta.
- **CRUD de cursos**: cada usuário autenticado pode criar, visualizar, editar, encerrar, concluir ou cancelar seus cursos.
- **Relacionamento 1:N**: um usuário possui zero ou mais cursos; o vínculo é mantido por uma Árvore B+ de relacionamento.
- **Código compartilhável**: ao criar um curso, um código único de 10 caracteres (NanoID) é gerado automaticamente.
- **Estado do curso**: cada curso pode estar em um dos quatro estados: *Aberto* (0), *Inscrições encerradas* (1), *Concluído* (2) ou *Cancelado* (3).
- **Proteção na exclusão de conta**: a conta só pode ser deletada se o usuário não possuir cursos ativos (estado 0 ou 1). Cursos inativos (estado 2 ou 3) são removidos automaticamente antes da exclusão.

---

## Fluxo de Navegação

```
Tela Inicial
├── (A) Realizar Login
│     └── Menu Logado
│           ├── (A) Meus Dados
│           │     ├── (A) Alterar dados
│           │     └── (B) Deletar conta
│           └── (B) Meus Cursos
│                 ├── (A) Criar novo curso
│                 └── (N) Detalhe do curso selecionado
│                       ├── (A) Gerenciar inscritos    [não implementado neste TP]
│                       ├── (B) Editar curso
│                       ├── (C) Encerrar inscrições
│                       ├── (D) Concluir curso
│                       └── (E) Cancelar curso
└── (B) Cadastrar novo usuário
```

---

## Telas do Sistema

### Tela inicial — Login, Cadastro e Recuperação de senha

<img width="430" height="322" alt="{06A390BE-ACEC-4D4D-BFFE-F45D92378C68}" src="https://github.com/user-attachments/assets/ad49f99b-de8e-464b-b2fa-018acaaa3c71" />



### Tela de cadastro de usuário

<img width="1296" height="625" alt="{561545EA-76C1-44EC-A74E-D9969DFBD836}" src="https://github.com/user-attachments/assets/f1181064-e6b1-409f-83ff-bba7628b5745" />


### Tela de login

<img width="1112" height="534" alt="{5F6EE82F-F9BD-4ACF-A378-F5CA6B6B43DB}" src="https://github.com/user-attachments/assets/f7514727-fce4-4b36-856b-d429a4ae29b9" />

### Tela redefinir senha

<img width="784" height="644" alt="{FCCFF724-9CB6-4874-AF6E-1F2793F12170}" src="https://github.com/user-attachments/assets/52adf48f-8dd9-44bb-89fd-1a57b7d97656" />

### Menu principal (pós-login)

<img width="1203" height="448" alt="{9CE1DB99-82DB-4DF9-A976-2525AA8CC099}" src="https://github.com/user-attachments/assets/9757093f-75a3-4971-ac1b-c760043b0d29" />


### Meus Dados

<img width="701" height="509" alt="{994AAAB9-0E32-4589-8B87-190EC36ED360}" src="https://github.com/user-attachments/assets/2df9a9dd-12a4-4724-bbfa-9181c865c707" />


### Meus Cursos — lista

<img width="573" height="377" alt="{F5111B8D-B83E-4CCC-9368-F23463543135}" src="https://github.com/user-attachments/assets/da980dca-7e6d-4cf6-b38b-c1627990c816" />


### Meus Cursos — criar novo curso

<img width="671" height="313" alt="{E194C4CC-E062-4B7E-A1A6-31EA8864AF11}" src="https://github.com/user-attachments/assets/d984a919-f4e6-406c-bbfe-943ffa2f14da" />


### Detalhe do curso

<img width="810" height="650" alt="{02A4F57C-EF8A-434C-9612-1890346C8C86}" src="https://github.com/user-attachments/assets/2c5b4105-f9b8-41d1-9ae3-58dfcf280f4b" />


---

## Classes Criadas

### `model/`

| Classe | Descrição |
|--------|-----------|
| `Usuario` | Entidade usuário com campos `id`, `nome`, `email`, `hashSenha`, `PerguntaSecreta`, `RespostaSecreta`. Implementa serialização binária via `toByteArray()` / `fromByteArray()`. |
| `Curso` | Entidade curso com campos `id`, `nome`, `dataInicioCurso`, `descricao`, `codigoCompartilhavel`, `estado` e `usuarioId` (chave estrangeira). O código compartilhável é gerado automaticamente na construção. |
| `NanoID` | Utilitário que gera strings alfanuméricas seguras usando `SecureRandom`. |

### `repository/`

| Classe | Descrição |
|--------|-----------|
| `Registro` | Interface contrato para entidades persistíveis: `setId`, `getId`, `toByteArray`, `fromByteArray`. |
| `RegistroHashExtensivel<T>` | Interface contrato para elementos armazenáveis na Hash Extensível. |
| `RegistroArvoreBMais<T>` | Interface contrato para elementos armazenáveis na Árvore B+. |
| `Arquivo<T>` | Motor de armazenamento binário genérico. Arquivos com cabeçalho de 12 bytes (último ID + cabeça da lista de espaços livres). Cada registro tem lápide (1 byte) + comprimento (2 bytes) + payload. Mantém um `HashExtensivel<ParIDEndereco>` como índice direto ID → endereço em disco. |
| `HashExtensivel<T>` | Implementação completa de **Hashing Extensível** persistida em dois arquivos: diretório (`.d.db`) e baldes (`.c.db`). Suporta split de baldes e duplicação de diretório. |
| `ArvoreBMais<T>` | Implementação completa de **Árvore B+** em disco. Folhas encadeadas para varredura sequencial. Suporta chaves duplicadas distinguidas por um segundo campo (`num2`). |
| `ParIDEndereco` | Par `(int id, long endereço)` — entrada do índice direto de todos os arquivos. |

### `repository/Usuario/`

| Classe | Descrição |
|--------|-----------|
| `ArquivoUsuario` | Estende `Arquivo<Usuario>`. Adiciona índice indireto `HashExtensivel<ParEmailID>` para busca por e-mail. Sobrescreve `create`, `delete` e `update` para manter o índice de e-mail sincronizado. |
| `ParEmailID` | Par `(String email, int id)` — entrada do índice indireto de e-mail. Tamanho fixo de 44 bytes. |

### `repository/Curso/`

| Classe | Descrição |
|--------|-----------|
| `ArquivoCurso` | Estende `Arquivo<Curso>`. Adiciona índice de relacionamento `ArvoreBMais<ParIntInt>` para o vínculo 1:N usuário-curso. Sobrescreve `create` e `delete` para manter a árvore sincronizada. Implementa `listarPorUsuario`, `temCursosAtivos` e `deletarCursosInativos`. |
| `ParIntInt` | Par `(int num1, int num2)` representando `(usuarioId, cursoId)` — entrada da Árvore B+ de relacionamento. Tamanho fixo de 8 bytes. |

### `controller/`

| Classe | Descrição |
|--------|-----------|
| `UsuarioController` | Camada de serviço para usuários. Encapsula o repositório, aplica hashing MD5 na senha e na resposta secreta, e expõe operações de login, cadastro, atualização e exclusão. |
| `CursoController` | Camada de serviço para cursos. Delega ao repositório e expõe operações CRUD mais `temCursosAtivos` e `excluirCursosInativos`. |

### `view/`

| Classe | Descrição |
|--------|-----------|
| `LoginView` | Tela inicial: menu de login e cadastro. Contém inner class `DadosNovoUsuario` e `CredenciaisLogin`. |
| `InicioView` | Menu pós-login: Meus Dados / Meus Cursos / Sair. |
| `DadosView` | Tela de perfil do usuário: exibe dados e permite atualização ou exclusão. Contém inner class `DadosAtualizados`. |
| `CursosView` | Lista de cursos do usuário e formulário de criação. Contém inner class `DadosNovoCurso`. |
| `CursoDetalheView` | Exibe detalhes de um curso e opções de gerenciamento. Contém inner class `DadosAtualizados`. |

### Ponto de entrada

| Classe | Descrição |
|--------|-----------|
| `Principal` | Classe `main`. Instancia todos os controllers e views e gerencia o fluxo de navegação. |

---

## Estrutura de Arquivos de Dados

```
dados/
├── usuarios/
│   ├── usuarios.db          # Registros binários de usuários
│   ├── usuarios.d.db        # Diretório do HashExtensivel (índice direto ID→addr)
│   ├── usuarios.c.db        # Baldes do HashExtensivel (índice direto)
│   ├── indiceEMAIL.d.db     # Diretório do HashExtensivel (índice indireto email→ID)
│   └── indiceEMAIL.c.db     # Baldes do HashExtensivel (índice indireto)
└── cursos/
    ├── cursos.db             # Registros binários de cursos
    ├── cursos.d.db           # Diretório do HashExtensivel (índice direto ID→addr)
    ├── cursos.c.db           # Baldes do HashExtensivel (índice direto)
    └── indiceUsuarioCurso.db # Árvore B+ do relacionamento 1:N (usuarioId, cursoId)
```

---

## Checklist

**Há um CRUD de usuários (que estende a classe `ArquivoIndexado`, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?**

Sim. A classe `ArquivoUsuario` estende `Arquivo<Usuario>` (equivalente à `ArquivoIndexado` do enunciado). Possui índice direto via `HashExtensivel<ParIDEndereco>` (ID → endereço) herdado da superclasse, e índice indireto via `HashExtensivel<ParEmailID>` (e-mail → ID) adicionado na subclasse. Todas as operações de criar, ler, atualizar e deletar usuários foram implementadas e testadas manualmente.

**Há um CRUD de cursos (que estende a classe `ArquivoIndexado`, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?**

Sim. A classe `ArquivoCurso` estende `Arquivo<Curso>`. Possui índice direto via `HashExtensivel<ParIDEndereco>` (herdado) e índice indireto via `ArvoreBMais<ParIntInt>` para o relacionamento 1:N. Todas as operações CRUD de cursos funcionam corretamente.

**Os cursos estão vinculados aos usuários usando o `idUsuario` como chave estrangeira?**

Sim. O campo `usuarioId` em `Curso.java` armazena o ID do usuário dono do curso. Esse valor é definido na criação do curso e usado como chave primária de busca na Árvore B+ do relacionamento.

**Há uma árvore B+ que registre o relacionamento 1:N entre usuários e cursos?**

Sim. A classe `ArquivoCurso` mantém uma instância de `ArvoreBMais<ParIntInt>` persistida no arquivo `indiceUsuarioCurso.db`. Os pares `(usuarioId, cursoId)` são inseridos na criação e removidos na exclusão de cursos. A listagem de cursos por usuário percorre a árvore buscando todos os pares com o `usuarioId` informado.

**Há um CRUD de usuários (que estende a classe `ArquivoIndexado`, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade)?**

Sim. 

**O trabalho compila corretamente?**

Sim. O projeto compila sem erros com `javac`. O ponto de entrada é `Principal.java` e todas as dependências estão no mesmo diretório/subdiretórios do projeto.

**O trabalho está completo e funcionando sem erros de execução?**

Sim. O sistema executa o fluxo completo: cadastro de usuário, login, criação de cursos, listagem, edição e exclusão, sem lançar exceções não tratadas durante o uso normal.

**O trabalho é original e não a cópia de um trabalho de outro grupo?**
Sim. O código foi inteiramente desenvolvido pelos integrantes do grupo listados neste relatório.

