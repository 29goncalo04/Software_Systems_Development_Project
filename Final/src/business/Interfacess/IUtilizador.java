package business.Interfacess;

import business.SSUtilizadores.Utilizadores.Utilizador;

public interface IUtilizador {
    boolean existeEmail(String email);
    boolean passwordValida(String email, String password);
    Utilizador devolveAluno(String email);
}