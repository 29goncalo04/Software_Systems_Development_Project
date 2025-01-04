package business.SSHorarios.Turnos;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Turno {
    private String nome;              // Nome do turno
    private int vagas;                // Número de vagas disponíveis
    private String tipo;              // Tipo do turno (T, TP ou PL)
    private DayOfWeek dia_semana;     // Dia da semana do turno
    private LocalTime horaInicial;    // Hora de início do turno
    private LocalTime horaFinal;      // Hora de fim do turno
    private String sala;              // Sala onde o turno ocorre
    private String uc;                // Unidade Curricular associada ao turno


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


    public String getChave() {
        return uc + "_" + nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public DayOfWeek getDiaSemana() {
        return dia_semana;
    }

    public void setDiaSemana(DayOfWeek dia_semana) {
        this.dia_semana = dia_semana;
    }

    public LocalTime getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getUC() {
        return uc;
    }


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
        sb.append(uc + "    " + nome + " Hora de inicio: " + horaInicial + "    Hora de fim: " + horaFinal + "    " + sala);
        return sb.toString();
    }
}
