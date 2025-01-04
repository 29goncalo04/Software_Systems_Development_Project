package business.SSHorarios.UCs;

public class UC {
    private String nome;
    private String[] preferencias;
    private int semestre;
    private int vagas;


    public UC(String nome, String[] preferencias, Integer semestre, Integer vagas) {
        this.nome = nome;
        this.preferencias = preferencias.clone();
        this.semestre = semestre;
        this.vagas = vagas;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String[] getPreferencias() {
        return preferencias.clone();
    }

    public void setPreferencias(String[] preferencias) {
        this.preferencias = preferencias.clone();
    }

    public int getSemestre(){
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


    public String toString(){
        StringBuilder sb = new StringBuilder(); 
        sb.append(nome);
        return sb.toString();
    }
}