package view;

import java.util.Scanner;
import model.Curso;

public class CursoDetalheView {

    private final Scanner console;

    public CursoDetalheView(Scanner console) {
        this.console = console;
    }

    public String mostrarMenuCurso(Curso curso) {

        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus Cursos > " + curso.getNome());

        System.out.println("\nCÓDIGO........: " + curso.getCodigoCompartilhavel());
        System.out.println("NOME..........: " + curso.getNome());
        System.out.println("DESCRIÇÃO.....: " + curso.getDescricao());
        System.out.println("DATA DE INÍCIO: " + curso.getDataInicioCurso());

        System.out.println();
        System.out.println(getStatusCurso(curso.getEstado()));
        System.out.println();

        System.out.println("(A) Gerenciar inscritos no curso");
        System.out.println("(B) Corrigir dados do curso");
        System.out.println("(C) Encerrar inscrições");
        System.out.println("(D) Concluir curso");
        System.out.println("(E) Cancelar curso");

        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");

        if (!console.hasNextLine()) {
            return "R";
        }

        String opcao = console.nextLine().trim().toUpperCase();

        // valida opções
        if (opcao.matches("[A-E]") || opcao.equals("R")) {
            return opcao;
        }

        System.out.println("Opção inválida.");
        return "";
    }

    // =========================
    // STATUS DO CURSO
    // =========================

    private String getStatusCurso(int estado) {

        switch (estado) {
            case 0:
                return "Este curso está aberto para inscrições!";
            case 1:
                return "Este curso está com inscrições encerradas.";
            case 2:
                return "Este curso já foi concluído.";
            case 3:
                return "Este curso foi cancelado.";
            default:
                return "Status desconhecido.";
        }
    }
}