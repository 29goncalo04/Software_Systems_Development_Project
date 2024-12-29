package SSUtilizadores;

import Interfacess.IUtilizador;
import SSUtilizadores.Utilizadores.Utilizador;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class UtilizadorFacade implements IUtilizador {

    private TreeMap<Integer, Utilizador> alunos;  // Armazena os alunos com o número como chave

    //----------- Construtores ---------------

    // Construtor por defeito que inicializa a TreeMap de alunos
    public UtilizadorFacade() {
        this.alunos = new TreeMap<>();
    }


    //-------------- Getters e Setters -----------------

    // Define a lista de utilizadores (alunos) na TreeMap
    public void setUtilizadores(TreeMap<Integer, Utilizador> utilizadores) {
        this.alunos = new TreeMap<>(utilizadores);
    }

    // Devolve uma cópia da TreeMap de alunos
    public TreeMap<Integer, Utilizador> getAlunos() {
        return new TreeMap<>(alunos);
    }


    //-------------- Funções -----------------

    // Verifica se o email inserido já está associado a um utilizador
    public boolean existeEmail(String email) {
        for (Utilizador utilizador : alunos.values()) {
            if (utilizador.compareEmail(email)) return true;
        }
        return false;
    }

    // Verifica se o email e a password fornecidos são válidos
    public boolean passwordValida(String email, String password) {
        for (Utilizador utilizador : alunos.values()) {
            if (utilizador.passwordValida(email, password)) return true;
        }
        return false;
    }

    // Verifica se o turno especificado é válido para o aluno com o ID fornecido
    //public boolean turnoValidoAluno(int id_utilizador, int id_turno) {
    //    Utilizador utilizador = alunos.get(id_utilizador);
    //    if (utilizador instanceof Aluno) {
    //        Aluno aluno = (Aluno) utilizador;
    //        return aluno.turnoValidoAluno(id_turno);
    //    }
    //    return false;
    //}

    // Verifica se o horário do aluno com o ID fornecido é válido
    //public boolean verificarHorario(int id_utilizador) {
    //    Utilizador utilizador = alunos.get(id_utilizador);
    //    if (utilizador instanceof Aluno) {
    //        Aluno aluno = (Aluno) utilizador;
    //        return aluno.verificarHorario();
    //    }
    //    return false;
    //}

    // Lê o ficheiro JSON de alunos e cria objetos Aluno
    public void criaAlunos() {
        List<Utilizador> alunos = new ArrayList<>();
        try (FileReader reader = new FileReader("./data/alunos.json");
             JsonReader jsonReader = Json.createReader(reader)) {

            // Lê o ficheiro JSON como um array
            JsonArray jsonArray = jsonReader.readArray();

            // Itera sobre os objetos no array
            for (JsonValue value : jsonArray) {
                JsonObject jsonObject = value.asJsonObject();

                // Extrai os valores do JSON
                String email = jsonObject.getString("email");
                String password = jsonObject.getString("password");
                String nome = jsonObject.getString("nome");
                int numero = jsonObject.getInt("numero");
                float media = jsonObject.getInt("media");
                String estatuto = jsonObject.getString("estatuto");
                String situacao = jsonObject.getString("situacao");
                int semestre = jsonObject.getInt("semestre");
                JsonArray ucsArray = jsonObject.getJsonArray("ucs");

                // Converte a lista de UCs de JSON para ArrayList<String>
                ArrayList<String> ucs = new ArrayList<>();
                for (int i = 0; i < ucsArray.size(); i++) {
                    ucs.add(ucsArray.getString(i));
                }

                // Cria um objeto Aluno
                Utilizador aluno = new Utilizador(email, password, nome, numero, media, estatuto, situacao, semestre, ucs);
                alunos.add(aluno);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Atualiza a TreeMap com os alunos criados
        setUtilizadores(alunos.stream()
            .collect(Collectors.toMap(aluno -> aluno.getNumero(), aluno -> aluno, (a, b) -> a, TreeMap::new)));
    }

    // Devolve um objeto Aluno correspondente ao email fornecido
    public Utilizador devolveAluno(String email) {
        for (Utilizador aluno : alunos.values()) {
            if (aluno.compareEmail(email)) return aluno;
        }
        return null;
    }
}