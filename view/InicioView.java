package view;

import java.util.Scanner;

public class InicioView {

    private final Scanner console;

    public InicioView(Scanner console) {
        this.console = console;
    }

    public String lerOpcaoMenuInicio() {
        System.out.println("\nEntrePares 1.0");
        System.out.println("-----------------");
        System.out.println("> Inicio");
        System.out.println("\n(A) Meus dados");
        System.out.println("(B) Meus cursos");
        System.out.println("(C) Minhas inscricoes");
        System.out.println("\n(S) Sair");
        System.out.print("\nOpcao: ");

        if (!console.hasNextLine()) {
            return "S";
        }

        return console.nextLine().trim().toUpperCase();
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
