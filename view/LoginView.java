package view;

import java.util.Scanner;

public class LoginView {

    private final Scanner console;

    public LoginView(Scanner console) {
        this.console = console;
    }

    public String lerOpcaoMenuInicial() {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("\n(A) Login");
        System.out.println("(B) Novo usuario");
        System.out.println("\n(S) Sair");
        System.out.print("\nOpcao: ");

        if (!console.hasNextLine()) {
            return "S";
        }

        return console.nextLine().trim().toUpperCase();
    }

    public CredenciaisLogin lerCredenciaisLogin() {
        System.out.println("\nLogin");
        System.out.print("Email: ");
        String email = console.nextLine().trim();
        System.out.print("Senha: ");
        String senha = console.nextLine();
        return new CredenciaisLogin(email, senha);
    }

    public DadosNovoUsuario lerNovoUsuario() {
        System.out.println("\nNovo usuario");

        System.out.print("Nome: ");
        String nome = console.nextLine().trim();

        System.out.print("Email: ");
        String email = console.nextLine().trim();

        System.out.print("Senha: ");
        String senha = console.nextLine();

        System.out.print("Pergunta secreta: ");
        String pergunta = console.nextLine();

        System.out.print("Resposta secreta: ");
        String resposta = console.nextLine();

        return new DadosNovoUsuario(nome, email, senha, pergunta, resposta);
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public static class CredenciaisLogin {
        public final String email;
        public final String senha;

        public CredenciaisLogin(String email, String senha) {
            this.email = email;
            this.senha = senha;
        }
    }

    public static class DadosNovoUsuario {
        public final String nome;
        public final String email;
        public final String senha;
        public final String pergunta;
        public final String resposta;

        public DadosNovoUsuario(String nome, String email, String senha, String pergunta, String resposta) {
            this.nome = nome;
            this.email = email;
            this.senha = senha;
            this.pergunta = pergunta;
            this.resposta = resposta;
        }
    }
}
