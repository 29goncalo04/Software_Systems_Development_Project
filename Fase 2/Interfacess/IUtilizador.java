package Interfacess;

public interface IUtilizador {
    void existeEmail(String email);
    boolean passwordValida(String email, String password);
    boolean turnoValidoAluno(int id_utilizador, int id_turno);
    void efetuaTroca(int id_utilizador, int id_turno_antigo, int id_turno_novo);
    boolean inscreveAluno(int id_utilizador, int id_turno);
    boolean verificarHorario(int id_utilizador);
}