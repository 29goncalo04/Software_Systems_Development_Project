package SSUtilizadores.Utilizadores;

import java.util.ArrayList;

import SSHorarios.Horarios.Horario;

public class Utilizador {
    protected String email;     // Protected para permitir acesso nas subclasses
    protected String password;  // Password do utilizador
    private String nome;               // Nome do aluno
    private int numero;                // Número de aluno
    private float media;               // Média académica do aluno
    private String estatuto;           // Estatuto do aluno (ex: trabalhador-estudante)
    private String situacao;           // Situação do aluno (ex: normal, suspenso)
    private int semestre;              // Semestre atual do aluno
    private ArrayList<String> turnos;  // Lista de turnos atribuídos ao aluno
    private ArrayList<String> ucs;     // Lista de unidades curriculares que o aluno frequenta
    private Horario horario;           // Horário do aluno

    //----------- Construtores ---------------

    // Construtor por defeito que inicializa o utilizador com valores padrão
    // public Utilizador() {
    //     this.email = "";
    //     this.password = "";
    // }

    // Construtor que inicializa o utilizador com os valores fornecidos
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


    //-------------- Getters e Setters -----------------

    // Devolve o email do utilizador
    public String getEmail() {
        return email;
    }

    // Define o email do utilizador
    public void setEmail(String email) {
        this.email = email;
    }

    // Devolve a password do utilizador
    public String getPassword() {
        return password;
    }

    // Define a password do utilizador
    public void setPassword(String password) {
        this.password = password;
    }

    // Devolve o nome do aluno
    public String getNome() {
        return nome;
    }

    // Define o nome do aluno
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Devolve o número do aluno
    public int getNumero() {
        return numero;
    }

    // Define o número do aluno
    public void setNumero(int numero) {
        this.numero = numero;
    }

    // Devolve a média académica do aluno
    public float getMedia() {
        return media;
    }

    // Define a média académica do aluno
    public void setMedia(float media) {
        this.media = media;
    }

    // Devolve o estatuto do aluno
    public String getEstatuto() {
        return estatuto;
    }

    // Define o estatuto do aluno
    public void setEstatuto(String estatuto) {
        this.estatuto = estatuto;
    }

    // Devolve a situação do aluno
    public String getSituacao() {
        return situacao;
    }

    // Define a situação do aluno
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    // Devolve o semestre atual do aluno
    public int getSemestre() {
        return semestre;
    }

    // Define o semestre atual do aluno
    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    // Devolve a lista de turnos atribuídos ao aluno
    public ArrayList<String> getTurnos() {
        return new ArrayList<String>(turnos);
    }

    // Define a lista de turnos atribuídos ao aluno
    public void setTurnos(ArrayList<String> turnos) {
        this.turnos = new ArrayList<String>(turnos);
    }

    // Devolve a lista de unidades curriculares (UCs) que o aluno frequenta
    public ArrayList<String> getUcs() {
        return new ArrayList<String>(ucs);
    }

    // Define a lista de unidades curriculares (UCs) que o aluno frequenta
    public void setUcs(ArrayList<String> ucs) {
        this.ucs = new ArrayList<String>(ucs);
    }

    // Devolve o horário do aluno
    public Horario getHorario() {
        return new Horario(horario);
    }

    // Define o horário do aluno
    public void setHorario(Horario horario) {
        this.horario = new Horario(horario);
    }


    //-------------- Funções -----------------

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
