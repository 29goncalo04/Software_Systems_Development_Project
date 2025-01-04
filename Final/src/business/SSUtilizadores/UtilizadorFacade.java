package business.SSUtilizadores;

import business.Interfacess.IUtilizador;
import business.SSUtilizadores.Utilizadores.Utilizador;
import data.UtilizadorDAO;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class UtilizadorFacade implements IUtilizador {

    private UtilizadorDAO alunos;

    public UtilizadorFacade() {
        this.alunos = UtilizadorDAO.getInstance();
    }


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


    public void criaAlunos() {
        //List<Utilizador> alunos = new ArrayList<>();
        try (FileReader reader = new FileReader("./jsons/alunos.json");
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
                this.alunos.put(numero, aluno);
                //alunos.add(aluno);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
 
        // Atualiza o TreeMap com os alunos criados
        //setUtilizadores(alunos.stream()
        //    .collect(Collectors.toMap(aluno -> aluno.getNumero(), aluno -> aluno, (a, b) -> a, TreeMap::new)));
    }

    // Devolve um objeto Aluno correspondente ao email fornecido
    public Utilizador devolveAluno(String email) {
        for (Utilizador aluno : alunos.values()) {
            if (aluno.compareEmail(email)) return aluno;
        }
        return null;
    }
}