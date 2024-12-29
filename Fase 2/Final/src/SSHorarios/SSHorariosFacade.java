package SSHorarios;

import SSHorarios.Horarios.*;
import SSHorarios.Turnos.*;
import SSHorarios.UCs.*;
import SSUtilizadores.Utilizadores.Utilizador;

import javax.json.*;

import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


public class SSHorariosFacade {

    private TreeMap<String, UC> ucs;         // Mapeamento de id para UC
    private TreeMap<Integer, Horario> horarios;  // Mapeamento de id para Horario
    private LinkedHashMap<String, Turno> turnos;   // Mapeamento de id para Turno
    private TreeMap<Integer, Utilizador> alunos;  // Mapeamento de id para Aluno
    private TreeMap<String, Sala> salas;  // Mapeamento de id para Salas
    private TreeMap<Integer, Horario> horarios_gerais;

    //----------- Construtor ---------------
    public SSHorariosFacade(TreeMap<Integer, Utilizador> alunos) {
        this.ucs = new TreeMap<>();
        this.horarios = new TreeMap<>();
        this.turnos = new LinkedHashMap<>();
        this.alunos = new TreeMap<Integer, Utilizador>(alunos);
        this.salas = new TreeMap<>();
        this.horarios_gerais = new TreeMap<>();
    }

    //--------------------------------------
    // ----------- Getters e Setters ---------------
    public void setSalas(TreeMap<String, Sala> salas){
        this.salas = new TreeMap<String, Sala> (salas);
    }

    public TreeMap<String, Sala> getSalas(){
        return new TreeMap<String, Sala>(this.salas);
    }

    public void setUCs(TreeMap<String, UC> ucs){
        this.ucs = new TreeMap<String, UC>(ucs);
    }

    public TreeMap<String, UC> getUcs(){
        return new TreeMap<String, UC>(this.ucs);
    }

    public void setTurnos(LinkedHashMap<String, Turno> turnos){
        this.turnos = new LinkedHashMap<String, Turno>(turnos);
    }

    public LinkedHashMap<String, Turno> getTurnos(){
        return new LinkedHashMap<String, Turno>(this.turnos);
    }

    // ----------- Funções ---------------

    public boolean ocupacaoTurnoValida(String id_turno){
        Turno turno = turnos.get(id_turno);
        return turno.ocupacaoTurnoValida();
    }

    //public boolean conflitosVerificacao(int id_horario){
    //    Horario horario = horarios.get(id_horario);
    //    return horario.verificaConflitos();
    //}

    //public boolean ucValidaVagas(int id_uc){
    //    UC uc = ucs.get(id_uc);
    //    return (uc.ucValidaVagas());
    //}

    public void exportarHorario(int id_aluno){
        Utilizador aluno = alunos.get(id_aluno);
        aluno.getHorario().imprimeHorario(id_aluno);
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
        List<Sala> salas = new ArrayList<Sala>();
        try (FileReader reader = new FileReader("./data/salas.json");
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
                salas.add(sala);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSalas(salas.stream()
            .collect(Collectors.toMap(sala -> sala.getNome(), sala->sala, (a, b) -> a, TreeMap::new)));
    }

    public void criaUCs(){
        List<UC> ucs = new ArrayList<UC>();
        try (FileReader reader = new FileReader("./data/ucs.json");
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
                ucs.add(uc);
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUCs(ucs.stream()
            .collect(Collectors.toMap(uc -> uc.getNome(), uc->uc, (a, b) -> a, TreeMap::new)));
    }

    public void criaTurnos(){
        List<Turno> turnos = new ArrayList<Turno>();
        try (FileReader reader = new FileReader("./data/turnos.json");
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
                if (tipo.equals("T")) vagas = salas.get(sala).getOcupacaoMaxima();
                else vagas = ucs.get(uc).getVagas();
                // Depois adicionar a condição se houver preferencias dos sores
                Turno turno = new Turno(nome, vagas, tipo, dia_semana, horaInicial, horaFinal, sala, uc);
                turnos.add(turno);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        turnos.sort(Comparator.comparing(Turno::getDiaSemana).thenComparing(Turno::getHoraInicial));
        setTurnos(turnos.stream()
            .collect(Collectors.toMap(turno -> turno.getChave(), turno->turno, (a, b) -> a, LinkedHashMap::new)));
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
                if((situacaoAluno == 0 && aluno.getSituacao().equals("não repetente")) || (situacaoAluno == 1 && aluno.getSituacao().equals("repetente"))){
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

    public void atualizaHorario(Utilizador aluno, String id_turno_antigo, String id_turno_novo){
        Turno turno_novo = turnos.get(id_turno_novo);
        aluno.setHorario(aluno.getHorario().atualizaHorario(id_turno_antigo, turno_novo));
        horarios.put(aluno.getNumero(), aluno.getHorario());
    }

    public void verificaConflitos(){
        for(Map.Entry<Integer, Horario> horarioAluno : horarios.entrySet()){
            horarioAluno.getValue().verificaConflitos(horarioAluno.getKey());
        }
    }
}