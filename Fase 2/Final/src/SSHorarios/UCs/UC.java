package SSHorarios.UCs;

public class UC {
    private String nome;
    private boolean opcional;
    private String[] preferencias;
    private int semestre;
    private int vagas;

    //----------- Construtores ---------------
    // public UC() {
    //     this.nome = "";
    //     this.opcional = false;
    //     this.preferencias = new String[0];  // Inicializa um array vazio
    // }

    public UC(String nome, String[] preferencias, Integer semestre, Integer vagas) {
        this.nome = nome;
        this.preferencias = preferencias.clone();
        this.semestre = semestre;
        this.vagas = vagas;
    }

    //--------------------------------------
    //-------------- Getters e Setters -----------------

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isOptional() {
        return opcional;
    }

    public void setOptional(boolean opcional) {
        this.opcional = opcional;
    }

    public String[] getPreferencias() {
        return preferencias.clone();
    }

    public void setPreferencias(String[] preferencias) {
        this.preferencias = preferencias.clone();
    }

    public Integer getSemestre(){
        return this.semestre;
    }

    public void setSemestre(Integer semestre){
        this.semestre = semestre;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    //--------------------------------------
    //-------------- Métodos adicionais -----------------
    //public boolean ucValidaVagas(){
    //    int res = getVagas();
    //    if (res == 0) return false;
    //    return true;
    //}

    public String toString(){
        StringBuilder sb = new StringBuilder();
        // DSS 
        sb.append(nome);
        return sb.toString();
    }
}