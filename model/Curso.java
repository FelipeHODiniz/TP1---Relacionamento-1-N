package model;

public class Curso {

    public int id;
    public String nome;
    public String dataInicioCurso;
    public String descricao;
    public String codigoCompartilhavel;
    public int estado;
    public int usuarioId;

    //Construtores
    public Curso() {
        this(-1, "", "", "", 0, 0);
    }
    public Curso(int i, String n, String d, String des, int e, int u) {
        this.id = i;
        this.nome = n;
        this.dataInicioCurso = d;
        this.descricao = des;
        this.codigoCompartilhavel = NanoID.gerarCodigo(10);
        this.estado = e;
        this.usuarioId = u;
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getDataInicioCurso() {
        return dataInicioCurso;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getCodigoCompartilhavel() {
        return codigoCompartilhavel;
    }
    public int getEstado() {
        return estado;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDataInicioCurso(String dataInicioCurso) {
        this.dataInicioCurso = dataInicioCurso;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    /* 
    O codigo é gerado pelo sistema, então não é necessário um setter para ele
    public void setCodigoCompartilhavel(String codigoCompartilhavel) {
        this.codigoCompartilhavel = codigoCompartilhavel;
    }*/
    public void setEstado(int estado) {
        this.estado = estado;
    }   
}