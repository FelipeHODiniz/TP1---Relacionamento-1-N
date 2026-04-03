package view;

import java.util.List;
import java.util.Scanner;
import model.Curso;
import model.Usuario;

public class CursosView {

    private final Scanner console;
    
    public CursosView(Scanner console) {
        this.console = console;
    }

    public String lerOpcaoMenuCursos(Usuario usuario, List<Curso> cursos) {
		System.out.println("\nEntrePares 1.0");
		System.out.println("-----------------");
		System.out.println("Inicio > Meus cursos");
		System.out.println("\nCURSOS");
		
        int index = 1;
        for (Curso curso : cursos) {
            System.out.println("(" + index + ") " + curso.getNome() + " - " + curso.getDataInicioCurso());
            index++;
        }

		System.out.println("\n(A) Novo curso");
		System.out.println("(R) Retornar ao menu anterior");
		System.out.print("\nOpcao: ");

		if (!console.hasNextLine()) {
			return "R";
		}

		return console.nextLine().trim().toUpperCase();
	}
}
