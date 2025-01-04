package business.Interfacess;

import business.SSUtilizadores.Utilizadores.Utilizador;

public interface IHorarios {
    boolean ocupacaoTurnoValida(String id_turno);
    void exportarHorario(int id_utilizador);
    void efetuaTroca(int id_utilizador, String id_turno_antigo, String id_turno_novo);
    void inscreveAluno(int id_utilizador, String id_turno);
    void criaHorariosGerais();
    String imprimeHorarioGeral(int semestre);
    void geraHorarios();
    void atualizaHorario(Utilizador aluno, String id_turno_antigo, String id_turno_novo);
    void verificaConflitos();
}