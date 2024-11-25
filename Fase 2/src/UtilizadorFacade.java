package src;

import Interfacess.IUtilizador;
import java.util.TreeMap;

public class UtilizadorFacade implements IUtilizador{
    private TreeMap<Integer, Utilizador> utilizadores;
    
    public void existeEmail(String email) {
        // 
    }

    @Override
    public boolean passwordValida(String email, String password) {
        return false;
    }

    @Override
    public boolean turnoValidoAluno(int id_utilizador, int id_turno) {
        return true;
    }

    @Override
    public void efetuaTroca(int id_utilizador, int id_turno_antigo, int id_turno_novo) {
        return;
    }

    @Override
    public boolean inscreveAluno(int id_utilizador, int id_turno) {
        return true;
    }

    @Override
    public boolean verificarHorario(int id_utilizador) {
        return false;
    }
}
