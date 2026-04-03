package repository.Curso;

import java.io.*;
import java.util.*;
import model.Curso;
import repository.ArvoreBMais;

public class ArquivoCurso {

    private RandomAccessFile arquivo;
    private final String nomeArquivo = "cursos.db";

    private ArvoreBMais<ParNomeId> indiceNome;

    public ArquivoCurso() throws Exception {
        arquivo = new RandomAccessFile(nomeArquivo, "rw");

        if (arquivo.length() == 0) {
            arquivo.writeInt(0); // último ID
        }

        indiceNome = new ArvoreBMais<>(
            ParNomeId.class.getConstructor(),
            5,
            "indiceNomeCurso.db"
        );
    }

    public void close() throws Exception {
        arquivo.close();
    }

    // CREATE
    public int create(Curso c) throws Exception {

        arquivo.seek(0);
        int ultimoId = arquivo.readInt();
        ultimoId++;

        c.setId(ultimoId);

        arquivo.seek(0);
        arquivo.writeInt(ultimoId);

        arquivo.seek(arquivo.length());

        byte[] ba = toByteArray(c);

        arquivo.writeBoolean(false);
        arquivo.writeInt(ba.length);
        arquivo.write(ba);

        // índice
        indiceNome.create(new ParNomeId(c.getNome(), c.getId()));

        return c.getId();
    }

    // READ
    public Curso read(int id) throws Exception {

        arquivo.seek(4);

        while (arquivo.getFilePointer() < arquivo.length()) {

            boolean lapide = arquivo.readBoolean();
            int tamanho = arquivo.readInt();

            byte[] ba = new byte[tamanho];
            arquivo.read(ba);

            if (!lapide) {
                Curso c = fromByteArray(ba);
                if (c.getId() == id)
                    return c;
            }
        }

        return null;
    }

    // LISTAR ORDENADO POR NOME (POR USUÁRIO)
    public List<Curso> listarPorUsuario(int usuarioId) throws Exception {

        List<Curso> listaFinal = new ArrayList<>();

        ArrayList<ParNomeId> lista = indiceNome.read(null);

        for (ParNomeId p : lista) {
            Curso c = read(p.getId());

            if (c != null && c.usuarioId == usuarioId) {
                listaFinal.add(c);
            }
        }

        return listaFinal;
    }

    // UPDATE
    public boolean update(Curso novo) throws Exception {

        arquivo.seek(4);

        while (arquivo.getFilePointer() < arquivo.length()) {

            long pos = arquivo.getFilePointer();

            boolean lapide = arquivo.readBoolean();
            int tamanho = arquivo.readInt();

            byte[] ba = new byte[tamanho];
            arquivo.read(ba);

            if (!lapide) {
                Curso antigo = fromByteArray(ba);

                if (antigo.getId() == novo.getId()) {

                    byte[] novoBa = toByteArray(novo);

                    // atualiza índice se nome mudou
                    if (!antigo.getNome().equals(novo.getNome())) {
                        indiceNome.delete(new ParNomeId(antigo.getNome(), antigo.getId()));
                        indiceNome.create(new ParNomeId(novo.getNome(), novo.getId()));
                    }

                    if (novoBa.length <= tamanho) {
                        arquivo.seek(pos + 5);
                        arquivo.write(novoBa);
                    } else {
                        arquivo.seek(pos);
                        arquivo.writeBoolean(true);

                        arquivo.seek(arquivo.length());
                        arquivo.writeBoolean(false);
                        arquivo.writeInt(novoBa.length);
                        arquivo.write(novoBa);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    // DELETE
    public boolean delete(int id) throws Exception {

        arquivo.seek(4);

        while (arquivo.getFilePointer() < arquivo.length()) {

            long pos = arquivo.getFilePointer();

            boolean lapide = arquivo.readBoolean();
            int tamanho = arquivo.readInt();

            byte[] ba = new byte[tamanho];
            arquivo.read(ba);

            if (!lapide) {
                Curso c = fromByteArray(ba);

                if (c.getId() == id) {

                    arquivo.seek(pos);
                    arquivo.writeBoolean(true);

                    indiceNome.delete(new ParNomeId(c.getNome(), c.getId()));

                    return true;
                }
            }
        }

        return false;
    }

    // ================= SERIALIZAÇÃO =================

    private byte[] toByteArray(Curso c) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(c.getId());
        dos.writeUTF(c.getNome());
        dos.writeUTF(c.getDataInicioCurso());
        dos.writeUTF(c.getDescricao());
        dos.writeUTF(c.getCodigoCompartilhavel());
        dos.writeInt(c.getEstado());
        dos.writeInt(c.usuarioId);

        return baos.toByteArray();
    }

    private Curso fromByteArray(byte[] ba) throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        Curso c = new Curso();

        c.setId(dis.readInt());
        c.setNome(dis.readUTF());
        c.setDataInicioCurso(dis.readUTF());
        c.setDescricao(dis.readUTF());
        c.codigoCompartilhavel = dis.readUTF();
        c.setEstado(dis.readInt());
        c.usuarioId = dis.readInt();

        return c;
    }
}