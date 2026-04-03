package view;

import java.util.Scanner;
import model.Curso;

public class CursoDetalheView {

    private final Scanner console;

    public static class DadosAtualizados {
        public String nome;
        public String dataInicioCurso;
        public String descricao;
    }

    public CursoDetalheView(Scanner console) {
        this.console = console;
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

    public boolean confirmarAcao(String acao) {
        System.out.print(acao + "? Confirmar (S/N): ");
        if (!console.hasNextLine()) return false;
        return console.nextLine().trim().equalsIgnoreCase("S");
    }

    public DadosAtualizados lerDadosAtualizados(Curso curso) {
        DadosAtualizados dados = new DadosAtualizados();

        System.out.println("\n> Corrigir dados do curso (Enter para manter o valor atual)");

        System.out.print("Nome [" + curso.getNome() + "]: ");
        String nome = console.nextLine().trim();
        dados.nome = nome.isEmpty() ? curso.getNome() : nome;

        System.out.print("Data de início [" + curso.getDataInicioCurso() + "]: ");
        String data = console.nextLine().trim();
        dados.dataInicioCurso = data.isEmpty() ? curso.getDataInicioCurso() : data;

        System.out.print("Descrição [" + curso.getDescricao() + "]: ");
        String desc = console.nextLine().trim();
        dados.descricao = desc.isEmpty() ? curso.getDescricao() : desc;

        return dados;
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