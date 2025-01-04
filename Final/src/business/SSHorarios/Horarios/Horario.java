package business.SSHorarios.Horarios;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import business.SSHorarios.Turnos.Turno;

// Classe que representa um horário, contendo um conjunto de turnos organizados.
public class Horario {

    private LinkedHashMap<String, Turno> turnos;


    public Horario() {
        this.turnos = new LinkedHashMap<>();
    }

    public Horario(LinkedHashMap<String, Turno> turnos) {
        this.turnos = turnos;
    }

    public Horario(Horario outro) {
        this.turnos = outro.turnos;
    }


    public LinkedHashMap<String, Turno> getTurnos() {
        return new LinkedHashMap<>(turnos);
    }

    public Turno obterTurno(String id_turno) {
        return this.turnos.get(id_turno);
    }


    // Verifica se existem conflitos entre os turnos do horário
    public void verificaConflitos(int nAluno) {
        boolean encontrouConflitos = false;
        List<Map.Entry<String, Turno>> turnosList = new ArrayList<>(turnos.entrySet());
        for (int i = 0; i < turnosList.size(); i++) {
            Map.Entry<String, Turno> turno1 = turnosList.get(i);
            if (turno1.getValue().getVagas() < 0){
                if(!encontrouConflitos){
                    System.out.println("\n\033[31mO horário do aluno " + nAluno + " tem os seguintes conflitos:\033[0m");
                    encontrouConflitos = true;
                }
                System.out.println("\033[31m      - O turno " + turno1.getValue().getChave() + " excedeu a capacidade\033[0m");
            }
            for (int j = i + 1; j < turnosList.size(); j++) {
                Map.Entry<String, Turno> turno2 = turnosList.get(j);
                Turno t1 = turno1.getValue();
                Turno t2 = turno2.getValue();
                if ((t1.getHoraInicial().isBefore(t2.getHoraFinal())) 
                    && (t2.getHoraInicial().isBefore(t1.getHoraFinal())) 
                    && (t1.getDiaSemana() == t2.getDiaSemana())) {
                    if(!encontrouConflitos){
                        System.out.println("\n\033[31mO horário do aluno " + nAluno + " tem os seguintes conflitos:\033[0m");
                        encontrouConflitos = true;
                    }
                    System.out.println("\033[31m      - Os turnos " + t1.getChave() + " e " + t2.getChave() + " estão sobrepostos\033[0m");
                }
            }
        }
    }

    public boolean verificaConflitosTurnos(Turno t1, Turno t2){
        if ((t1.getHoraInicial().isBefore(t2.getHoraFinal())) 
                        && (t2.getHoraInicial().isBefore(t1.getHoraFinal())) 
                        && (t1.getDiaSemana() == t2.getDiaSemana())) return true;
        return false;
    }

    // Devolve uma representação textual do horário, com os turnos organizados por dia da semana
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Segunda-feira\n");
        for (Turno turno : turnos.values()) {
            if (turno.getDiaSemana().name().equals("MONDAY")) {
                sb.append("     ").append(turno).append("\n");
            }
        }
        sb.append("\nTerca-feira\n");
        for (Turno turno : turnos.values()) {
            if (turno.getDiaSemana().name().equals("TUESDAY")) {
                sb.append("     ").append(turno).append("\n");
            }
        }
        sb.append("\nQuarta-feira\n");
        for (Turno turno : turnos.values()) {
            if (turno.getDiaSemana().name().equals("WEDNESDAY")) {
                sb.append("     ").append(turno).append("\n");
            }
        }
        sb.append("\nQuinta-feira\n");
        for (Turno turno : turnos.values()) {
            if (turno.getDiaSemana().name().equals("THURSDAY")) {
                sb.append("     ").append(turno).append("\n");
            }
        }
        sb.append("\nSexta-feira\n");
        for (Turno turno : turnos.values()) {
            if (turno.getDiaSemana().name().equals("FRIDAY")) {
                sb.append("     ").append(turno).append("\n");
            }
        }
        return sb.toString();
    }

    // Imprime o horário no formato textual
    public void imprimeHorario(int id_aluno) {
        String nomeFicheiro = "Aluno_" + id_aluno + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFicheiro))) {
            // Escreve o conteúdo do horário no ficheiro
            writer.write(toString());
        } catch (IOException e) {
            System.err.println("Erro ao exportar o horário");
        }
    }

    // Gera um horário personalizado para o aluno com base nas UC's que ele frequenta e nos turnos disponíveis no horário geral
    public Horario geraHorario(TreeMap<Integer, ArrayList<String>> ucsAluno, TreeMap<Integer, Horario> horariosGerais) {
        ArrayList<Turno> turno_para_horarioArray = new ArrayList<>();

        int adicionado_T = 0;
        int adicionado_TP_PL = 0;

        for(Integer semestre : ucsAluno.keySet()){
            for (String uc : ucsAluno.get(semestre)) {
                adicionado_T = 0;
                adicionado_TP_PL = 0;
                Horario horario_geral = horariosGerais.get(semestre);
                int temConflitoT = 0;
                int temConflitoTP_PL = 0;
                for (Turno turno : horario_geral.getTurnos().values()) {
                    temConflitoT = 0;
                    temConflitoTP_PL = 0;
                    if (turno.getUC().equals(uc)) {
                        if (adicionado_T == 0 && turno.getTipo().equals("T") && turno.ocupacaoTurnoValida()) {
                            for (Turno t : turno_para_horarioArray){
                                if (verificaConflitosTurnos(t, turno)) temConflitoT = 1;
                            }
                            if (temConflitoT == 0){
                                turno_para_horarioArray.add(turno);
                                adicionado_T = 1;
                            }
                        }
                        if (adicionado_TP_PL == 0 && (turno.getTipo().equals("TP") || turno.getTipo().equals("PL")) 
                        && turno.ocupacaoTurnoValida()) {
                            for (Turno t : turno_para_horarioArray){
                                if (verificaConflitosTurnos(t, turno)) temConflitoTP_PL = 1;
                            }
                            if (temConflitoTP_PL == 0){
                                turno_para_horarioArray.add(turno);
                                adicionado_TP_PL = 1;
                            }   
                        }
                    }
                        if (adicionado_T == 1 && adicionado_TP_PL == 1) break;
                    }
                    if (adicionado_T == 0){
                        for(Turno turno : horario_geral.getTurnos().values()){
                            if (turno.getUC().equals(uc) && turno.getTipo().equals("T")){
                                turno_para_horarioArray.add(turno);
                                break;
                            }
                        }
                    }
                    if (adicionado_TP_PL == 0){
                        for(Turno turno : horario_geral.getTurnos().values()){
                            if (turno.getUC().equals(uc) && (turno.getTipo().equals("TP") || turno.getTipo().equals("PL"))){
                                turno_para_horarioArray.add(turno);
                                break;
                            }
                        }
                }
            }
        }

        // Ordena os turnos por dia da semana e hora de início
        turno_para_horarioArray.sort(Comparator.comparing(Turno::getDiaSemana)
                                               .thenComparing(Turno::getHoraInicial));

        // Converte a lista ordenada de turnos em LinkedHashMap
        LinkedHashMap<String, Turno> novo_horario = turno_para_horarioArray.stream()
            .collect(Collectors.toMap(Turno::getChave, turno -> turno, (a, b) -> a, LinkedHashMap::new));

        return new Horario(novo_horario);
    }

    public Horario atualizaHorario(String id_turno_antigo, Turno turno_novo){
        this.turnos.remove(id_turno_antigo);
        ArrayList<Turno> turnos_para_horarioArray = new ArrayList<Turno>();
        for(Turno turno : turnos.values()){
            turnos_para_horarioArray.add(turno);
        }
        turnos_para_horarioArray.add(turno_novo);
        // Ordena os turnos por dia da semana e hora de início
        turnos_para_horarioArray.sort(Comparator.comparing(Turno::getDiaSemana)
                                               .thenComparing(Turno::getHoraInicial));
        LinkedHashMap<String, Turno> horario_atualizado = turnos_para_horarioArray.stream()
            .collect(Collectors.toMap(Turno::getChave, turno -> turno, (a, b) -> a, LinkedHashMap::new));
        return new Horario(horario_atualizado);
    }
}