package view;

import java.util.List;
import java.util.Scanner;
import model.Curso;
import model.Usuario;

public class CursosView {

    private final Scanner console;

    public static class DadosNovoCurso {
        public String nome;
        public String dataInicioCurso;
        public String descricao;
    }

    public CursosView(Scanner console) {
        this.console = console;
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

    public String lerOpcaoMenuCursos(Usuario usuario, List<Curso> cursos) {

        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus cursos");

        System.out.println("\nCURSOS");

        int index = 1;
        for (Curso curso : cursos) {
            System.out.println("(" + index + ") " + curso.getNome() + " - " + curso.getDataInicioCurso());
            index++;
        }

        System.out.println("\n(A) Novo curso");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        if (!console.hasNextLine()) {
            return "R";
        }

        String opcao = console.nextLine().trim().toUpperCase();

        if (opcao.matches("\\d+")) {
            int num = Integer.parseInt(opcao);
            if (num >= 1 && num <= cursos.size()) {
                return opcao;
            } else {
                System.out.println("Opção inválida.");
                return "";
            }
        }

        if (opcao.equals("A") || opcao.equals("R")) {
            return opcao;
        }

        System.out.println("Opção inválida.");
        return "";
    }

    public DadosNovoCurso lerDadosNovoCurso() {
        DadosNovoCurso dados = new DadosNovoCurso();

        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus cursos > Novo curso");

        System.out.print("\nNome do curso: ");
        dados.nome = console.nextLine().trim();

        System.out.print("Data de início (dd/mm/aaaa): ");
        dados.dataInicioCurso = console.nextLine().trim();

        System.out.print("Descrição: ");
        dados.descricao = console.nextLine().trim();

        return dados;
    }
}