package repository.Curso;

import java.io.*;
import repository.RegistroArvoreBMais;

public class ParNomeId implements RegistroArvoreBMais<ParNomeId> {

    private String nome;
    private int id;

    private final short TAMANHO = 104; // 100 chars + 4 bytes int

    public ParNomeId() {
        this("", -1);
    }

    public ParNomeId(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    @Override
    public ParNomeId clone() {
        return new ParNomeId(this.nome, this.id);
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public int compareTo(ParNomeId o) {
        return this.nome.compareTo(o.nome);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(ba);

        String nomeFixed = String.format("%-100s", nome);
        out.writeUTF(nomeFixed);
        out.writeInt(id);

        return ba.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(in);

        this.nome = dis.readUTF().trim();
        this.id = dis.readInt();
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }
}