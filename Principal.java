import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import controller.CursoController;
import controller.UsuarioController;
import model.Curso;
import model.Usuario;
import view.CursoDetalheView;
import view.CursosView;
import view.DadosView;
import view.InicioView;
import view.LoginView;

public class Principal {

    private static final Scanner CONSOLE = new Scanner(System.in);
    private static final LoginView LOGIN_VIEW = new LoginView(CONSOLE);
    private static final InicioView INICIO_VIEW = new InicioView(CONSOLE);
    private static final DadosView DADOS_VIEW = new DadosView(CONSOLE);
    private static final CursosView CURSOS_VIEW = new CursosView(CONSOLE);
    private static final CursoDetalheView CURSO_DETALHE_VIEW = new CursoDetalheView(CONSOLE);
    private static UsuarioController USUARIO_CONTROLLER;
    private static CursoController CURSO_CONTROLLER;

    public static void main(String[] args){
        try {
            USUARIO_CONTROLLER = new UsuarioController();
            CURSO_CONTROLLER = new CursoController();
            menuInicial();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o sistema.");
            e.printStackTrace();
        } finally {
            if (USUARIO_CONTROLLER != null) {
                try {
                    USUARIO_CONTROLLER.close();
                } catch (Exception e) {
                    System.out.println("Erro ao encerrar recursos do sistema.");
                    e.printStackTrace();
                }
            }
            if (CURSO_CONTROLLER != null) {
                try {
                    CURSO_CONTROLLER.close();
                } catch (Exception e) {
                    System.out.println("Erro ao encerrar recursos de cursos.");
                    e.printStackTrace();
                }
            }
        }
    }

    private static void menuInicial() throws Exception {
        String opcao;
        boolean executando = true;

        while (executando) {
            opcao = LOGIN_VIEW.lerOpcaoMenuInicial();

            switch (opcao) {
                case "A":
                    executando = realizarLogin();
                    break;
                case "B":
                    cadastrarNovoUsuario();
                    break;
                case "C":
                    redefinirSenha();
                    break;
                case "S":
                    LOGIN_VIEW.mostrarMensagem("Encerrando...");
                    executando = false;
                    break;
                default:
                    LOGIN_VIEW.mostrarMensagem("Opcao invalida.");
                    break;
            }
        }
    }

    private static boolean realizarLogin() throws Exception {
        LoginView.CredenciaisLogin credenciais = LOGIN_VIEW.lerCredenciaisLogin();
        String email = credenciais.email;

        if (!emailValido(email)) {
            LOGIN_VIEW.mostrarMensagem("Email invalido.");
            return true;
        }

        boolean loginValido = USUARIO_CONTROLLER.login(email, credenciais.senha);
        if (!loginValido) {
            LOGIN_VIEW.mostrarMensagem("Usuario nao cadastrado ou senha incorreta.");
            return true;
        }

        LOGIN_VIEW.mostrarMensagem("Login realizado com sucesso.");
        return menuInicioAposLogin(email);
    }

    private static boolean menuInicioAposLogin(String email) throws Exception {
        String opcao;

        do {
            opcao = INICIO_VIEW.lerOpcaoMenuInicio();
            switch (opcao) {
                case "A":
                    String emailAtualizado = menuMeusDados(email);
                    if (emailAtualizado == null) {
                        return false;
                    }
                    email = emailAtualizado;
                    break;
                case "B":
                    if (!menuMeusCursos(email)) {
                        return false;
                    }
                    break;
                case "C":
                    INICIO_VIEW.mostrarMensagem("Tela de Minhas inscricoes em desenvolvimento.");
                    break;
                case "S":
                    INICIO_VIEW.mostrarMensagem("Encerrando...");
                    return false;
                default:
                    INICIO_VIEW.mostrarMensagem("Opcao invalida.");
                    break;
            }
        } while (true);
    }

    private static boolean menuMeusCursos(String email) throws Exception {
        Usuario usuario = USUARIO_CONTROLLER.buscarPorEmail(email);
        if (usuario == null) {
            INICIO_VIEW.mostrarMensagem("Usuario nao encontrado.");
            return true;
        }

        boolean emCursos = true;
        while (emCursos) {
            List<Curso> cursos = CURSO_CONTROLLER.listarPorUsuario(usuario.getId());
            if (cursos == null) cursos = new ArrayList<>();

            String opcao = CURSOS_VIEW.lerOpcaoMenuCursos(usuario, cursos);

            if (opcao.equals("R")) {
                emCursos = false;
            } else if (opcao.equals("A")) {
                CursosView.DadosNovoCurso dados = CURSOS_VIEW.lerDadosNovoCurso();
                Curso novo = new Curso(-1, dados.nome, dados.dataInicioCurso, dados.descricao, 0, usuario.getId());
                int id = CURSO_CONTROLLER.cadastrar(novo);
                CURSOS_VIEW.mostrarMensagem(id >= 0
                    ? "Curso criado com sucesso. ID: " + id
                    : "Falha ao criar curso.");
            } else if (opcao.matches("\\d+")) {
                int idx = Integer.parseInt(opcao) - 1;
                menuDetalheCurso(cursos.get(idx));
            }
        }

        return true;
    }

    private static void menuDetalheCurso(Curso curso) throws Exception {
        boolean emDetalhe = true;
        while (emDetalhe) {
            String opcao = CURSO_DETALHE_VIEW.mostrarMenuCurso(curso);
            switch (opcao) {
                case "A":
                    CURSO_DETALHE_VIEW.mostrarMensagem("Gerenciar inscritos em desenvolvimento.");
                    break;
                case "B":
                    CursoDetalheView.DadosAtualizados novos = CURSO_DETALHE_VIEW.lerDadosAtualizados(curso);
                    curso.setNome(novos.nome);
                    curso.setDataInicioCurso(novos.dataInicioCurso);
                    curso.setDescricao(novos.descricao);
                    boolean ok = CURSO_CONTROLLER.atualizar(curso);
                    CURSO_DETALHE_VIEW.mostrarMensagem(ok
                        ? "Curso atualizado com sucesso."
                        : "Falha ao atualizar curso.");
                    break;
                case "C":
                    if (CURSO_DETALHE_VIEW.confirmarAcao("Encerrar inscricoes")) {
                        curso.setEstado(1);
                        CURSO_CONTROLLER.atualizar(curso);
                        CURSO_DETALHE_VIEW.mostrarMensagem("Inscricoes encerradas.");
                    }
                    break;
                case "D":
                    if (CURSO_DETALHE_VIEW.confirmarAcao("Concluir curso")) {
                        curso.setEstado(2);
                        CURSO_CONTROLLER.atualizar(curso);
                        CURSO_DETALHE_VIEW.mostrarMensagem("Curso concluido.");
                    }
                    break;
                case "E":
                    if (CURSO_DETALHE_VIEW.confirmarAcao("Cancelar curso")) {
                        // Sem inscrições: exclui o curso. Com inscrições (TP2): apenas cancela.
                        boolean excluido = CURSO_CONTROLLER.excluir(curso.getId());
                        if (excluido) {
                            CURSO_DETALHE_VIEW.mostrarMensagem("Curso cancelado e excluido.");
                            emDetalhe = false;
                        } else {
                            curso.setEstado(3);
                            CURSO_CONTROLLER.atualizar(curso);
                            CURSO_DETALHE_VIEW.mostrarMensagem("Curso cancelado.");
                        }
                    }
                    break;
                case "R":
                    emDetalhe = false;
                    break;
                default:
                    break;
            }
        }
    }

    private static String menuMeusDados(String email) throws Exception {
        Usuario usuario = USUARIO_CONTROLLER.buscarPorEmail(email);
        if (usuario == null) {
            DADOS_VIEW.mostrarMensagem("Usuario nao encontrado.");
            return email;
        }

        boolean emDados = true;
        while (emDados) {
            String opcao = DADOS_VIEW.lerOpcaoMenuDados(usuario);
            switch (opcao) {
                case "A":
                    DadosView.DadosAtualizados novos = DADOS_VIEW.lerDadosAtualizados(usuario);
                    boolean perguntaMudou = !novos.pergunta.equals(usuario.PerguntaSecreta);
                    boolean temNovaResposta = novos.resposta != null && !novos.resposta.isEmpty();

                    if (novos.nome.length() < 4) {
                        DADOS_VIEW.mostrarMensagem("Erro: O nome deve ter no minimo 4 caracteres.");
                    } else if (!emailValido(novos.email)) {
                        DADOS_VIEW.mostrarMensagem("Erro: Email invalido.");
                    } else if (perguntaMudou && !temNovaResposta) {
                        DADOS_VIEW.mostrarMensagem("Erro: Ao alterar a pergunta, voce deve obrigatoriamente fornecer uma nova resposta.");
                    } else {
                        boolean ok = USUARIO_CONTROLLER.atualizarDados(email, novos.nome, novos.email, novos.pergunta, novos.resposta);
                        if (ok) {
                            DADOS_VIEW.mostrarMensagem("Dados atualizados com sucesso.");
                            email = novos.email;
                            usuario.nome = novos.nome;
                            usuario.email = novos.email;
                            usuario.PerguntaSecreta = novos.pergunta;
                            if (temNovaResposta) {
                                usuario.RespostaSecreta = USUARIO_CONTROLLER.toMd5(novos.resposta.toLowerCase());
                            }
                        } else {
                            DADOS_VIEW.mostrarMensagem("Falha ao atualizar dados.");
                        }
                    }

                    break;
                case "B":
                    if (DADOS_VIEW.confirmarExclusao()) {
                        if (CURSO_CONTROLLER.temCursosAtivos(usuario.getId())) {
                            DADOS_VIEW.mostrarMensagem("Nao e possivel excluir usuario com cursos ativos.");
                        } else {
                            CURSO_CONTROLLER.excluirCursosInativos(usuario.getId());
                            boolean apagou = USUARIO_CONTROLLER.deletarPorEmail(email);
                            if (apagou) {
                                DADOS_VIEW.mostrarMensagem("Conta deletada com sucesso.");
                                return null;
                            } else {
                                DADOS_VIEW.mostrarMensagem("Falha ao deletar conta.");
                            }
                        }
                    }
                    break;
                case "S":
                    emDados = false;
                    break;
                default:
                    DADOS_VIEW.mostrarMensagem("Opcao invalida.");
                    break;
            }
        }

        return email;
    }

    private static void cadastrarNovoUsuario() throws Exception {
        LoginView.DadosNovoUsuario dados = LOGIN_VIEW.lerNovoUsuario();

        if (dados.nome.length() < 4) {
            LOGIN_VIEW.mostrarMensagem("O nome deve ter no minimo 4 caracteres.");
            return;
        }

        if (!emailValido(dados.email)) {
            LOGIN_VIEW.mostrarMensagem("Email invalido.");
            return;
        }

        if (dados.senha.isEmpty()) {
            LOGIN_VIEW.mostrarMensagem("Senha nao pode ser vazia.");
            return;
        }

        if (dados.pergunta.trim().isEmpty() || dados.resposta.trim().isEmpty()) {
            LOGIN_VIEW.mostrarMensagem("Pergunta e resposta secreta sao obrigatorias.");
            return;
        }

        Usuario novoUsuario = new Usuario(
            dados.nome,
            dados.email,
            dados.senha,
            dados.pergunta,
            dados.resposta
        );

        int id = USUARIO_CONTROLLER.cadastrar(novoUsuario);
        if (id < 0) {
            LOGIN_VIEW.mostrarMensagem("Ja existe usuario cadastrado com esse email.");
            return;
        }

        LOGIN_VIEW.mostrarMensagem("Usuario cadastrado com sucesso. ID: " + id);
    }

    private static void redefinirSenha() throws Exception {
        String email = LOGIN_VIEW.lerEmail();

        if (!emailValido(email)) {
            LOGIN_VIEW.mostrarMensagem("Email invalido.");
            return;
        }

        Usuario usuario = USUARIO_CONTROLLER.buscarPorEmail(email);
        if (usuario == null) {
            LOGIN_VIEW.mostrarMensagem("Usuario nao encontrado.");
            return;
        }

        String resposta = LOGIN_VIEW.lerRespostaSecreta(usuario.PerguntaSecreta);
        String hashResposta = USUARIO_CONTROLLER.toMd5(resposta.toLowerCase().trim());

        if (hashResposta.equals(usuario.getRespostaSecreta())) {
            String novaSenha = LOGIN_VIEW.lerNovaSenha();
            if (novaSenha.isEmpty()) {
                LOGIN_VIEW.mostrarMensagem("Senha nao pode ser vazia.");
                return;
            }

            boolean ok = USUARIO_CONTROLLER.redefinirSenha(email, novaSenha);
            if (ok) {
                LOGIN_VIEW.mostrarMensagem("Senha redefinida com sucesso.");
            } else {
                LOGIN_VIEW.mostrarMensagem("Falha ao redefinir a senha.");
            }
        } else {
            LOGIN_VIEW.mostrarMensagem("Resposta incorreta.");
        }
    }

    private static boolean emailValido(String email) {
        return email != null && email.contains("@")
            && email.indexOf('@') > 0 && email.indexOf('@') < email.length() - 1;
    }
}

