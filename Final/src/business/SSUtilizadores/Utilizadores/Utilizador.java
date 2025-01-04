package business.SSUtilizadores.Utilizadores;

import java.util.ArrayList;

import business.SSHorarios.Horarios.Horario;

public class Utilizador {
    protected String email;            // Email do utilizador
    protected String password;         // Password do utilizador
    private String nome;               // Nome do aluno
    private int numero;                // Número de aluno
    private float media;               // Média do aluno
    private String estatuto;           // Estatuto do aluno (ex: trabalhador-estudante)
    private String situacao;           // Situação do aluno (ex: normal, suspenso)
    private int semestre;              // Semestre atual do aluno
    private ArrayList<String> turnos;  // Lista de turnos atribuídos ao aluno
    private ArrayList<String> ucs;     // Lista de unidades curriculares que o aluno frequenta
    private Horario horario;           // Horário do aluno


    public Utilizador(String email, String password, String nome, int numero, float media, String estatuto, String situacao, int semestre, ArrayList<String> ucs) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.numero = numero;
        this.media = media;
        this.estatuto = estatuto;
        this.situacao = situacao;
        this.semestre = semestre;
        this.turnos = new ArrayList<String>();
        this.ucs = ucs;
        this.horario = new Horario();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public String getEstatuto() {
        return estatuto;
    }

    public void setEstatuto(String estatuto) {
        this.estatuto = estatuto;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public ArrayList<String> getTurnos() {
        return new ArrayList<String>(turnos);
    }

    public void setTurnos(ArrayList<String> turnos) {
        this.turnos = new ArrayList<String>(turnos);
    }

    public ArrayList<String> getUcs() {
        return new ArrayList<String>(ucs);
    }

    public void setUcs(ArrayList<String> ucs) {
        this.ucs = new ArrayList<String>(ucs);
    }

    public Horario getHorario() {
        return new Horario(horario);
    }

    public void setHorario(Horario horario) {
        this.horario = new Horario(horario);
    }



    // Compara o email fornecido com o email do utilizador
    public boolean compareEmail(String email) {
        boolean res = getEmail().equals(email);
        return res;
    }

    // Verifica se o email e a password fornecidos correspondem ao utilizador
    public boolean passwordValida(String email, String password) {
        String emailUtilizador = getEmail();
        boolean a = emailUtilizador.equals(email);
        if (a) {
            String passwordUtilizador = getPassword();
            return passwordUtilizador.equals(password);
        }
        return false;
    }

    // Remove um turno antigo da lista de turnos do aluno
    public void removeTurnoAntigo(String id_turno_antigo) {
        this.turnos.remove(id_turno_antigo);
    }

    // Adiciona um novo turno à lista de turnos do aluno
    public void addTurnoNovo(String id_turno_novo) {
        this.turnos.add(id_turno_novo);
    }

    // Verifica se o aluno possui um estatuto especial
    public boolean hasEstatuto() {
        if (this.estatuto.equals("normal")) return false;
        return true;
    }
}
