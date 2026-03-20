package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import model.Usuario;
import repository.Usuario.ArquivoUsuario;

public class UsuarioController {

    private final ArquivoUsuario repository;

    public UsuarioController() throws Exception {
        this.repository = new ArquivoUsuario();
    }

    public void close() throws Exception {
        repository.close();
    }

    public Usuario buscarPorEmail(String email) {
        return this.repository.buscarPorEmail(email);
    }

    public boolean login(String email, String senha){
        Usuario usuario = this.repository.buscarPorEmail(email);
        if (usuario == null) {
            return false;
        }

        if (!usuario.hashSenha.equals(toMd5(senha))) {
            return false;
        }

        return true;
    }

    public int cadastrar(Usuario u){
        int id = -1;
        try{
            u.setHashSenha(toMd5(u.getHashSenha()));
            id = this.repository.create(u);
        }
        catch(Exception e){
            e.printStackTrace();;
        }
        return id;
    }

    public boolean atualizarDados(String emailOriginal, String novoNome, String novoEmail, String novaPergunta, String novaResposta) {
        try {
            Usuario usuario = repository.buscarPorEmail(emailOriginal);
            if (usuario == null) {
                return false;
            }

            // Se estiver alterando o email, verifica se já existe outro usuário com o novo email
            if (!novoEmail.equals(emailOriginal)) {
                Usuario existente = repository.buscarPorEmail(novoEmail);
                if (existente != null && existente.getId() != usuario.getId()) {
                    return false;
                }
            }

            usuario.nome = novoNome;
            usuario.email = novoEmail;
            // mantem o hash da senha
            usuario.PerguntaSecreta = novaPergunta;
            usuario.RespostaSecreta = novaResposta;

            return repository.update(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletarPorEmail(String email) {
        try {
            return repository.delete(email);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String toMd5(final String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash MD5", e);
        }
    }
}
