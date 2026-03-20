package view;

import java.util.Scanner;

import model.Usuario;

public class DadosView {

	private final Scanner console;

	public DadosView(Scanner console) {
		this.console = console;
	}

	public String lerOpcaoMenuDados(Usuario usuario) {
		System.out.println("\nEntrePares 1.0");
		System.out.println("-----------------");
		System.out.println("Inicio > Meus dados");
		System.out.println("\nNome........: " + usuario.getNome());
		System.out.println("Email.......: " + usuario.getEmail());
		System.out.println("Pergunta....: " + usuario.getPerguntaSecreta());
		System.out.println("Resposta....: " + usuario.getRespostaSecreta());

		System.out.println("\n(A) Alterar dados");
		System.out.println("(B) Deletar conta");
		System.out.println("\n(S) Voltar");
		System.out.print("\nOpcao: ");

		if (!console.hasNextLine()) {
			return "S";
		}

		return console.nextLine().trim().toUpperCase();
	}

	public DadosAtualizados lerDadosAtualizados(Usuario atual) {
		System.out.println("\nAlterar dados (deixe em branco para manter o valor atual)");

		System.out.print("Novo nome (" + atual.getNome() + "): ");
		String nome = console.nextLine().trim();
		if (nome.isEmpty()) {
			nome = atual.getNome();
		}

		System.out.print("Novo email (" + atual.getEmail() + "): ");
		String email = console.nextLine().trim();
		if (email.isEmpty()) {
			email = atual.getEmail();
		}

		System.out.print("Nova pergunta secreta (" + atual.getPerguntaSecreta() + "): ");
		String pergunta = console.nextLine();
		if (pergunta.isEmpty()) {
			pergunta = atual.getPerguntaSecreta();
		}

		System.out.print("Nova resposta secreta (" + atual.getRespostaSecreta() + "): ");
		String resposta = console.nextLine();
		if (resposta.isEmpty()) {
			resposta = atual.getRespostaSecreta();
		}

		return new DadosAtualizados(nome, email, pergunta, resposta);
	}

	public boolean confirmarExclusao() {
		System.out.print("\nTem certeza que deseja deletar sua conta? (S/N): ");
		if (!console.hasNextLine()) {
			return false;
		}
		String opcao = console.nextLine().trim().toUpperCase();
		return "S".equals(opcao);
	}

	public void mostrarMensagem(String mensagem) {
		System.out.println(mensagem);
	}

	public static class DadosAtualizados {
		public final String nome;
		public final String email;
		public final String pergunta;
		public final String resposta;

		public DadosAtualizados(String nome, String email, String pergunta, String resposta) {
			this.nome = nome;
			this.email = email;
			this.pergunta = pergunta;
			this.resposta = resposta;
		}
	}
}
