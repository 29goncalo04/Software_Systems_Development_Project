package Interfacess;

import SSUtilizadores.Utilizadores.Utilizador;

public interface IUtilizador {
    boolean existeEmail(String email);
    boolean passwordValida(String email, String password);
    //boolean turnoValidoAluno(int id_utilizador, int id_turno);
    //boolean verificarHorario(int id_utilizador);
    Utilizador devolveAluno(String email);
}