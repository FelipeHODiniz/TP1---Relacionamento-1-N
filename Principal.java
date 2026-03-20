import java.util.Scanner;
import controller.UsuarioController;
import view.InicioView;
import view.LoginView;
import model.Usuario;

public class Principal {

    private static final Scanner CONSOLE = new Scanner(System.in);
    private static final LoginView LOGIN_VIEW = new LoginView(CONSOLE);
    private static final InicioView INICIO_VIEW = new InicioView(CONSOLE);
    private static UsuarioController USUARIO_CONTROLLER;

    public static void main(String[] args){
        try {
            USUARIO_CONTROLLER = new UsuarioController();
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
        return menuInicioAposLogin();
    }

    private static boolean menuInicioAposLogin() {
        String opcao;

        do {
            opcao = INICIO_VIEW.lerOpcaoMenuInicio();
            switch (opcao) {
                case "A":
                    INICIO_VIEW.mostrarMensagem("Tela de Meus dados em desenvolvimento.");
                    break;
                case "B":
                    INICIO_VIEW.mostrarMensagem("Tela de Meus cursos em desenvolvimento.");
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

    private static boolean emailValido(String email) {
        return email != null && email.contains("@")
            && email.indexOf('@') > 0 && email.indexOf('@') < email.length() - 1;
    }
}

