
import java.util.Scanner;
import model.Usuario;
import repository.Usuario.*;

public class debugUsuarios {
    
    ArquivoUsuario arqUsuarios;
    private static Scanner console = new Scanner(System.in);

    public debugUsuarios() throws Exception {
        arqUsuarios = new ArquivoUsuario();
    }

    public static void main(String[] args) {
        try {
            debugUsuarios d = new debugUsuarios();
            d.menu();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o debug de usuarios.");
            e.printStackTrace();
        }
    }

    public void menu() {

        int opcao;
        do {

            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Usuarios");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarUsuario();
                    break;
                case 2:
                    incluirUsuario();
                    break;
                case 3:
                    alterarUsuario();
                    break;
                case 4:
                    excluirUsuario();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }


    public void buscarUsuario() {
        System.out.println("\nBusca de usuario");
        String email;
        do {
            System.out.print("\nEMAIL: ");
            email = console.nextLine();

            if(email.isEmpty())
                return; 

            if (!emailValido(email)) {
                System.out.println("Email invalido.");
                email = "";
            }

        } while (email.isEmpty());

        try {
            Usuario usuario = arqUsuarios.read(email);
            if (usuario != null) {
                mostraUsuario(usuario);
            } else {
                System.out.println("Usuario nao encontrado.");
            }
        } catch(Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel buscar o usuario.");
            e.printStackTrace();
        }
    }   


    public void incluirUsuario() {
        System.out.println("\nInclusao de usuario");
        String nome = "";
        String email = "";
        String hashSenha = "";
        String perguntaSecreta = "";
        String respostaSecreta = "";

        do {
            System.out.print("\nNome (min. de 4 letras ou vazio para cancelar): ");
            nome = console.nextLine();
            if(nome.length()==0)
                return;
            if(nome.length()<4) {
                System.err.println("O nome do usuario deve ter no minimo 4 caracteres.");
            }
        } while(nome.length()<4);

        do {
            System.out.print("Email (ou vazio para cancelar): ");
            email = console.nextLine();
            if(email.length()==0) {
                return;
            }
            if(!emailValido(email)) {
                System.err.println("Email invalido.");
            }
        } while(!emailValido(email));

        System.out.print("Hash da senha: ");
        hashSenha = console.nextLine();
        System.out.print("Pergunta secreta: ");
        perguntaSecreta = console.nextLine();
        System.out.print("Resposta secreta: ");
        respostaSecreta = console.nextLine();

        System.out.print("\nConfirma a inclusao do usuario? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if(resp=='S' || resp=='s') {
            try {
                Usuario u = new Usuario(nome, email, hashSenha, perguntaSecreta, respostaSecreta);
                int id = arqUsuarios.create(u);
                System.out.println("Usuario incluido com sucesso. ID: " + id);
            } catch(Exception e) {
                System.out.println("Erro do sistema. Nao foi possivel incluir o usuario.");
                e.printStackTrace();
            }
        }
    }

    public void alterarUsuario() {
        System.out.println("\nAlteracao de usuario");
        String email;

        do {
            System.out.print("\nEMAIL do usuario a alterar: ");
            email = console.nextLine();

            if(email.isEmpty())
                return; 

            if (!emailValido(email)) {
                System.out.println("Email invalido.");
                email = "";
            }
        } while (email.isEmpty());


        try {
            Usuario usuario = arqUsuarios.read(email);
            if (usuario != null) {
                System.out.println("Usuario encontrado:");
                mostraUsuario(usuario);

                System.out.print("\nNovo nome (deixe em branco para manter o anterior): ");
                String novoNome = console.nextLine();
                if (!novoNome.isEmpty()) {
                    usuario.nome = novoNome;
                }

                System.out.print("Novo email (deixe em branco para manter o anterior): ");
                String novoEmail = console.nextLine();
                if (!novoEmail.isEmpty()) {
                    if (!emailValido(novoEmail)) {
                        System.out.println("Email invalido. Alteracao de email ignorada.");
                    } else {
                        usuario.email = novoEmail;
                    }
                }

                System.out.print("Novo hash de senha (deixe em branco para manter o anterior): ");
                String novoHash = console.nextLine();
                if (!novoHash.isEmpty()) {
                    usuario.hashSenha = novoHash;
                }

                System.out.print("Nova pergunta secreta (deixe em branco para manter a anterior): ");
                String novaPergunta = console.nextLine();
                if (!novaPergunta.isEmpty()) {
                    usuario.PerguntaSecreta = novaPergunta;
                }

                System.out.print("Nova resposta secreta (deixe em branco para manter a anterior): ");
                String novaResposta = console.nextLine();
                if (!novaResposta.isEmpty()) {
                    usuario.RespostaSecreta = novaResposta;
                }

                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    boolean alterado = arqUsuarios.update(usuario);
                    if (alterado) {
                        System.out.println("Usuario alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o usuario.");
                    }
                } else {
                    System.out.println("Alteracoes canceladas.");
                }
            } else {
                System.out.println("Usuario nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel alterar o usuario.");
            e.printStackTrace();
        }
        
    }


    public void excluirUsuario() {
        System.out.println("\nExclusao de usuario");
        String email;

        do {
            System.out.print("\nEMAIL: ");
            email = console.nextLine();

            if(email.isEmpty())
                return; 

            if (!emailValido(email)) {
                System.out.println("Email invalido.");
                email = "";
            }
        } while (email.isEmpty());

        try {
            Usuario usuario = arqUsuarios.read(email);
            if (usuario != null) {
                System.out.println("Usuario encontrado:");
                mostraUsuario(usuario);

                System.out.print("\nConfirma a exclusao do usuario? (S/N) ");
                char resp = console.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {
                    boolean excluido = arqUsuarios.delete(email);
                    if (excluido) {
                        System.out.println("Usuario excluido com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o usuario.");
                    }
                } else {
                    System.out.println("Exclusao cancelada.");
                }
            } else {
                System.out.println("Usuario nao encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel excluir o usuario.");
            e.printStackTrace();
        }
    }


    private boolean emailValido(String email) {
        return email != null && email.contains("@") && email.indexOf('@') > 0 && email.indexOf('@') < email.length() - 1;
    }


    public void mostraUsuario(Usuario usuario) {
        if (usuario != null) {
            System.out.println("\nDetalhes do Usuario:");
            System.out.println("----------------------");
            System.out.printf("ID........: %d%n", usuario.id);
            System.out.printf("Nome......: %s%n", usuario.nome);
            System.out.printf("Email.....: %s%n", usuario.email);
            System.out.printf("Hash......: %s%n", usuario.hashSenha);
            System.out.printf("Pergunta..: %s%n", usuario.PerguntaSecreta);
            System.out.printf("Resposta..: %s%n", usuario.RespostaSecreta);
            System.out.println("----------------------");
        }
    }
}
