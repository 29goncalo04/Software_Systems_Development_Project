package business.SSHorarios;

import business.SSHorarios.Horarios.*;
import business.SSHorarios.Turnos.*;
import business.SSHorarios.UCs.*;
import business.SSUtilizadores.Utilizadores.Utilizador;
import data.SalaDAO;
import data.TurnoDAO;
import data.UcDAO;
import data.UtilizadorDAO;

import javax.json.*;

import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


public class SSHorariosFacade {

    private UcDAO ucs;
    private TreeMap<Integer, Horario> horarios;  // Mapeamento de id para Horario
    private TurnoDAO turnos;
    private UtilizadorDAO alunos;
    private SalaDAO salas;
    private TreeMap<Integer, Horario> horarios_gerais;

    public SSHorariosFacade() {
        this.ucs = UcDAO.getInstance();
        this.horarios = new TreeMap<>();
        this.turnos = TurnoDAO.getInstance();
        this.alunos = UtilizadorDAO.getInstance();
        this.salas = SalaDAO.getInstance();
        this.horarios_gerais = new TreeMap<>();
    }


    public Horario getHorario(int nAluno){
        return horarios.get(nAluno);
    }

    public void exportarHorario(int id_aluno){
        horarios.get(id_aluno).imprimeHorario(id_aluno);
    }

    public void efetuaTroca(int id_utilizador, String id_turno_antigo, String id_turno_novo){
        Utilizador aluno = alunos.get(id_utilizador);
        aluno.removeTurnoAntigo(id_turno_antigo);
        aluno.addTurnoNovo(id_turno_novo);
        Turno turno_antigo = turnos.get(id_turno_antigo);
        turno_antigo.incrementaVagas();
        Turno turno_novo = turnos.get(id_turno_novo);
        turno_novo.diminuiVagas();
    }

    public void inscreveAluno(int id_utilizador, String id_turno){
        Utilizador aluno = alunos.get(id_utilizador);
        aluno.addTurnoNovo(id_turno);
        Turno turno = turnos.get(id_turno);
        turno.diminuiVagas();
    }

    public void criaSalas(){
        try (FileReader reader = new FileReader("./jsons/salas.json");
            JsonReader jsonReader = Json.createReader(reader)) {

            // Lê o ficheiro JSON como um array
            JsonArray jsonArray = jsonReader.readArray();

            // Itera sobre os objetos no array
            for (JsonValue value : jsonArray) {
                JsonObject jsonObject = value.asJsonObject();

                // Extrai os valores do JSON
                String nome = jsonObject.getString("nome");
                int ocupacaoMaxima = jsonObject.getInt("ocupacaoMaxima");

                // Cria um objeto Sala
                Sala sala = new Sala(nome, ocupacaoMaxima);
                this.salas.put(nome, sala);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void criaUCs(){
        try (FileReader reader = new FileReader("./jsons/ucs.json");
            JsonReader jsonReader = Json.createReader(reader)) {

            // Lê o ficheiro JSON como um array
            JsonArray jsonArray = jsonReader.readArray();

            // Itera sobre os objetos no array
            for (JsonValue value : jsonArray) {
                JsonObject jsonObject = value.asJsonObject();

                // Extrai os valores do JSON
                String nome = jsonObject.getString("nome");
                JsonArray preferenciasArray = jsonObject.getJsonArray("preferencias");
                String[] preferencias = new String[preferenciasArray.size()];
                for (int i = 0; i < preferenciasArray.size(); i++) {
                    preferencias[i] = preferenciasArray.getString(i);
                }
                Integer semestre = jsonObject.getInt("semestre");
                Integer vagas = jsonObject.getInt("vagas");
                UC uc = new UC(nome, preferencias, semestre, vagas);
                this.ucs.put(nome, uc);
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void criaTurnos(){
        try (FileReader reader = new FileReader("./jsons/turnos.json");
            JsonReader jsonReader = Json.createReader(reader)) {

            // Lê o ficheiro JSON como um array
            JsonArray jsonArray = jsonReader.readArray();

            // Itera sobre os objetos no array
            for (JsonValue value : jsonArray) {
                JsonObject jsonObject = value.asJsonObject();

                // Extrai os valores do JSON
                String nome = jsonObject.getString("nome");
                String tipo = jsonObject.getString("tipo");
                String dia_semanaString = jsonObject.getString("dia_semana");
                DayOfWeek dia_semana = DayOfWeek.valueOf(dia_semanaString);
                String horaInicialString = jsonObject.getString("horaInicial");
                LocalTime horaInicial = LocalTime.parse(horaInicialString);
                String horaFinalString = jsonObject.getString("horaFinal");
                LocalTime horaFinal = LocalTime.parse(horaFinalString);
                String sala = jsonObject.getString("sala");
                String uc = jsonObject.getString("uc");
                
                Integer vagas;
                if (tipo.equals("T")) {
                    Sala salaObj = salas.get(sala);
                    if (salaObj != null) {
                        vagas = salaObj.getOcupacaoMaxima();
                    } else {
                        throw new RuntimeException("Sala não encontrada: " + sala);
                    }
                } else {
                    vagas = ucs.get(uc).getVagas();
                }
                Turno turno = new Turno(nome, vagas, tipo, dia_semana, horaInicial, horaFinal, sala, uc);
                String key = turno.getChave(); // Geração da chave única
                turnos.put(key, turno); // Guardar no DAO
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void criaHorariosGerais(){
        for (int semestre = 1; semestre<=6; semestre++){
            List<Turno> turnosList = new ArrayList<Turno>();
            for (Turno turno : turnos.values()){
                String nomeUC = turno.getUC();
                UC uc = ucs.get(nomeUC);
                int semestreUC = uc.getSemestre();
                if (semestreUC == semestre){
                    turnosList.add(turno);
                }
            }
            turnosList.sort(Comparator.comparing(Turno::getDiaSemana).thenComparing(Turno::getHoraInicial));
            LinkedHashMap<String, Turno> turnosMap = turnosList.stream()
                .collect(Collectors.toMap(turno -> turno.getChave(), turno->turno, (a, b) -> a, LinkedHashMap::new));
            Horario horario_geral = new Horario(turnosMap);
            horarios_gerais.put(semestre, horario_geral);
        }
    }
    
    public String imprimeHorarioGeral(int semestre){
        StringBuilder sb = new StringBuilder();
        sb.append(horarios_gerais.get(semestre));
        return sb.toString();
    }

    public void geraHorarios(){
        int situacaoAluno = 0;
        while (situacaoAluno<=1){
            for(Utilizador aluno: alunos.values()){
                if((situacaoAluno == 0 && aluno.getSituacao().equals("nao repetente")) || (situacaoAluno == 1 && aluno.getSituacao().equals("repetente"))){
                    TreeSet<Integer> semestres = new TreeSet<Integer>(Comparator.reverseOrder());
                    TreeMap<Integer, ArrayList<String>> ucsAluno = new TreeMap<Integer, ArrayList<String>>(Comparator.reverseOrder());
                    ArrayList<String> ucsPorSemestre = new ArrayList<String>();
                    for(String uc_nome : aluno.getUcs()){
                        UC uc = ucs.get(uc_nome);
                        semestres.add(uc.getSemestre());
                        ucsPorSemestre.add(uc.getNome());
                        ucsAluno.put(uc.getSemestre(), ucsPorSemestre);
                    }
                    TreeMap<Integer, Horario> horariosGerais = new TreeMap<Integer, Horario>();
                    for(Integer semestre : semestres){
                        horariosGerais.put(semestre, horarios_gerais.get(semestre));
                    }   
                    aluno.setHorario(aluno.getHorario().geraHorario(ucsAluno, horariosGerais));
                    for(Turno turno : aluno.getHorario().getTurnos().values()){
                        inscreveAluno(aluno.getNumero(), turno.getChave());
                    }
                    horarios.put(aluno.getNumero(), aluno.getHorario());
                    aluno.getHorario().verificaConflitos(aluno.getNumero());
                }
            }
            situacaoAluno++;
        }
    }

    public void atualizaHorario(int nAluno, String id_turno_antigo, String id_turno_novo){
        Turno turno_novo = turnos.get(id_turno_novo);
        horarios.put(nAluno, horarios.get(nAluno).atualizaHorario(id_turno_antigo, turno_novo));
    }

    public void verificaConflitos(){
        for(Map.Entry<Integer, Horario> horarioAluno : horarios.entrySet()){
            horarioAluno.getValue().verificaConflitos(horarioAluno.getKey());
        }
    }
}