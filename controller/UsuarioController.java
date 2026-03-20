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
