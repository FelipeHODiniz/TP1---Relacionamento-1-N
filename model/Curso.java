

public class Curso {

    public int id;
    public String nome;
    public String dataInicioCurso;
    public String descricao;
    public int estado;

    //Construtores
    public Curso() {
        this(-1, "", "", "", 0);
    }
    public Curso(int i, String n, String d, String des, int e) {
        this.id = i;
        this.nome = n;
        this.dataInicioCurso = d;
        this.descricao = des;
        this.estado = e;
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
    public void setEstado(int estado) {
        this.estado = estado;
    }


}