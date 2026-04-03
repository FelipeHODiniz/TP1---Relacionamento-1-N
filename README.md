# TP1 — Relacionamento 1:N
**Trabalho Prático — Algoritmos e Estruturas de Dados III**

---

## Participantes

- Felipe Henrique Oliveira Diniz
- Matheus de Almeida Moreira

---

## Descrição do Sistema

O sistema, chamado **EntrePares 1.0**, é uma aplicação de gerenciamento de usuários e cursos com persistência em arquivos binários. Ele foi desenvolvido em Java seguindo o padrão arquitetural **MVC (Model-View-Controller)** e implementa estruturas de dados de indexação **Tabela Hash Extensível** e **Árvore B+** para garantir acesso eficiente aos registros armazenados em disco.

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

