package model;
import java.io.IOException;

import repository.Registro;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class Usuario implements Registro {

    public int id;
    public String nome;
    public String email;
    public String hashSenha;
    public String PerguntaSecreta;
    public String RespostaSecreta;

    public Usuario() {
        this(-1, "", "", "", "", "");
    }

    public Usuario(int i, String n, String e, String h, String p, String r) {
        this.id = i;
        this.nome = n;
        this.email = e;
        this.hashSenha = h;
        this.PerguntaSecreta = p;
        this.RespostaSecreta = r;
    }
    public Usuario(String n, String e, String h, String p, String r) {
        this(-1, n, e, h, p, r);
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setHashSenha(final String hashSenha){
        this.hashSenha = hashSenha;
    }

    public String getHashSenha() {
        return hashSenha;
    }

    public String getPerguntaSecreta() {
        return PerguntaSecreta;
    }

    public String getRespostaSecreta() {
        return RespostaSecreta;
    }

    public String toString() {
        return "\nID........: " + this.id +
               "\nNome......: " + this.nome +
               "\nEmail: " + this.email +
               "\nHash Senha: " + this.hashSenha +
               "\nPergunta Secreta: " + this.PerguntaSecreta +
               "\nResposta Secreta: " + this.RespostaSecreta;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeUTF(this.hashSenha);
        dos.writeUTF(this.PerguntaSecreta);
        dos.writeUTF(this.RespostaSecreta);
        return baos.toByteArray();
    }


    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.hashSenha = dis.readUTF();
        this.PerguntaSecreta = dis.readUTF();
        this.RespostaSecreta = dis.readUTF();
    }
}