package SSHorarios.Turnos;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Turno {
    private String nome;              // Nome do turno
    private int vagas;                // Número de vagas disponíveis
    private String tipo;              // Tipo do turno (T, TP, PL)
    private DayOfWeek dia_semana;     // Dia da semana em que o turno ocorre
    private LocalTime horaInicial;    // Hora de início do turno
    private LocalTime horaFinal;      // Hora de fim do turno
    private String sala;              // Sala onde o turno ocorre
    private String uc;                // Unidade Curricular associada ao turno

    // ----------- Construtores ---------------

    // Construtor por defeito que inicializa o turno com valores padrão
    public Turno() {
        this.nome = "";
        this.vagas = -1;
        this.tipo = "";
        this.dia_semana = DayOfWeek.MONDAY;
        this.horaInicial = LocalTime.of(8, 0);
        this.horaFinal = LocalTime.of(10, 0);
        this.sala = "";
        this.uc = "";
    }

    // Construtor que inicializa o turno com os valores fornecidos
    public Turno(String nome, int vagas, String tipo, DayOfWeek dia_semana, LocalTime horaInicial, LocalTime horaFinal, String sala, String uc) {
        this.nome = nome;
        this.vagas = vagas;
        this.tipo = tipo;
        this.dia_semana = dia_semana;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.sala = sala;
        this.uc = uc;
    }

    // ----------- Getters e Setters ---------------

    // Devolve uma chave única que identifica o turno através da combinação UC e nome
    public String getChave() {
        return uc + "_" + nome;
    }

    // Devolve o nome do turno
    public String getNome() {
        return nome;
    }

    // Define o nome do turno
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Devolve o número de vagas disponíveis no turno
    public int getVagas() {
        return vagas;
    }

    // Define o número de vagas disponíveis no turno
    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    // Devolve o tipo do turno
    public String getTipo() {
        return tipo;
    }

    // Define o tipo do turno
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Devolve o dia da semana em que o turno ocorre
    public DayOfWeek getDiaSemana() {
        return dia_semana;
    }

    // Define o dia da semana em que o turno ocorre
    public void setDiaSemana(DayOfWeek dia_semana) {
        this.dia_semana = dia_semana;
    }

    // Devolve a hora de início do turno
    public LocalTime getHoraInicial() {
        return horaInicial;
    }

    // Define a hora de início do turno
    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    // Devolve a hora de fim do turno
    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    // Define a hora de fim do turno
    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    // Devolve a sala onde o turno ocorre
    public String getSala() {
        return sala;
    }

    // Define a sala onde o turno ocorre
    public void setSala(String sala) {
        this.sala = sala;
    }

    // Devolve a Unidade Curricular associada ao turno
    public String getUC() {
        return uc;
    }

    // ----------- Funções ---------------

    // Verifica se ainda existem vagas disponíveis no turno
    public boolean ocupacaoTurnoValida() {
        vagas = getVagas();
        return vagas > 0;
    }

    // Diminui o número de vagas disponíveis no turno
    public void diminuiVagas() {
        this.vagas--;
    }

    // Aumenta o número de vagas disponíveis no turno
    public void incrementaVagas() {
        this.vagas++;
    }

    // Devolve uma representação textual do turno
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // DSS    TP3  Hora de início: 9:00h  Hora de fim: 11:00h    0.08
        sb.append(uc + "    " + nome + " Hora de início: " + horaInicial + "    Hora de fim: " + horaFinal + "    " + sala);
        return sb.toString();
    }
}
