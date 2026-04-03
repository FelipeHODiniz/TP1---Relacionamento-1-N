package controller;

import java.util.List;
import model.Curso;
import repository.Curso.ArquivoCurso;

public class CursoController {

    private ArquivoCurso repository;

    public CursoController() throws Exception {
        repository = new ArquivoCurso();
    }

    public void close() throws Exception {
        repository.close();
    }

    // CREATE
    public int cadastrar(Curso c) {
        try {
            return repository.create(c);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar curso");
            return -1;
        }
    }

    // READ (por ID)
    public Curso buscar(int id) {
        try {
            return repository.read(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar curso");
            return null;
        }
    }

    // READ (todos)
    public List<Curso> listar() {
        try {
            return repository.readAll();
        } catch (Exception e) {
            System.out.println("Erro ao listar cursos");
            return null;
        }
    }

    // UPDATE
    public boolean atualizar(Curso c) {
        try {
            return repository.update(c);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar curso");
            return false;
        }
    }

    // DELETE
    public boolean excluir(int id) {
        try {
            return repository.delete(id);
        } catch (Exception e) {
            System.out.println("Erro ao excluir curso");
            return false;
        }
    }
}