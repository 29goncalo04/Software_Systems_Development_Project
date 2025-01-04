package ui;

import java.util.Scanner;

import business.SSHorarios.SSHorariosFacade;
import business.SSUtilizadores.UtilizadorFacade;
import business.SSUtilizadores.Utilizadores.Utilizador;

public class Main {
    public static void main(String[] args){
        UtilizadorFacade utilizadoresFacade = new UtilizadorFacade();
        Scanner scanner = new Scanner(System.in);
        utilizadoresFacade.criaAlunos();
        SSHorariosFacade horariosFacade = new SSHorariosFacade();

        while(true){
            System.out.print("\nInsira o seu email: ");
            String email = scanner.nextLine();
            if (email.equals("admin")){ // É o diretor de curso
                int terminar_sessao = 0;
                while(terminar_sessao == 0){
                    System.out.println("\n  1 - Importar salas");
                    System.out.println("  2 - Importar UCs");
                    System.out.println("  3 - Importar turnos");
                    System.out.println("  4 - Criar horários gerais");
                    System.out.println("  5 - Ver horários gerais");
                    System.out.println("  6 - Gerar horários pessoais");
                    System.out.println("  7 - Ver horário específico");
                    System.out.println("  8 - Gerir conflitos");
                    System.out.println("  9 - Verificar conflitos");
                    System.out.println("  10 - Terminar sessão");
                    System.out.println("  11 - Sair do programa");
                    System.out.print("Digite o número da opção: ");
                    int escolha = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();
                    switch (escolha) {
                        case 1:
                            horariosFacade.criaSalas();
                            System.out.println("As salas foram importadas\n");
                            break;
                        case 2:
                            horariosFacade.criaUCs(); 
                            System.out.println("As UCs foram importadas\n");
                            break;
                        case 3:
                            horariosFacade.criaTurnos(); 
                            System.out.println("Os turnos foram importados\n");
                            break;
                        case 4:
                            horariosFacade.criaHorariosGerais();
                            System.out.println("Os horários gerais foram criados\n");
                            break;
                        case 5:
                            System.out.println("\u001B[1m════════════════════════════════════════════════");
                            System.out.println("\u001B[1m        Escolha o semestre do horário           ");
                            System.out.println("\u001B[1m    que deseja ver (1 | 2 | 3 | 4 | 5 | 6):     ");
                            System.out.println("\u001B[1m════════════════════════════════════════════════\u001B[0m");
                            int semestre = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println(horariosFacade.imprimeHorarioGeral(semestre));
                            break;
                        case 6:
                            horariosFacade.geraHorarios();
                            System.out.println(("\nHorários pessoais gerados\n"));
                            break;
                        case 7:
                            System.out.print("Insira o número do aluno que deseja: ");
                            int nAluno = scanner.nextInt();
                            //System.out.println(utilizadoresFacade.getAluno(nAluno).getHorario());
                            System.out.println(horariosFacade.getHorario(nAluno));
                            scanner.nextLine();
                            break;
                        case 8:
                            System.out.print("Insira o número do aluno que deseja: ");
                            int nrAluno = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Insira o turno que deseja trocar: ");
                            String turno_antigo = scanner.nextLine();
                            System.out.print("Insira a respetiva UC: ");
                            String uc = scanner.nextLine();
                            System.out.print("Insira o novo turno em que pretende inscrever o aluno: ");
                            String turno_novo = scanner.nextLine();
                            horariosFacade.efetuaTroca(nrAluno, (uc + "_" + turno_antigo), (uc + "_" + turno_novo));
                            horariosFacade.atualizaHorario(nrAluno, (uc + "_" + turno_antigo), (uc + "_" + turno_novo));
                            System.out.print("\nTroca efetuada\n");
                            break;
                        case 9:
                            horariosFacade.verificaConflitos();
                            break;
                        case 10:
                            terminar_sessao = 1;
                            System.err.println("A terminar sessão...");
                            break;
                        case 11: //sair
                            System.out.println("A sair do programa...");
                            scanner.close();
                            return;
                        default:
                            break;
                    }
                }
            }
            else{ // É um aluno
                int terminar_sessao = 0;
                if(!utilizadoresFacade.existeEmail(email)) System.out.println("Esse email não existe");
                else{
                    System.out.print("Insira a sua password: ");
                    String password = scanner.nextLine();
                    if (utilizadoresFacade.passwordValida(email, password)){
                        Utilizador aluno = utilizadoresFacade.devolveAluno(email);
                        boolean hasEstatuto = aluno.hasEstatuto();
                        System.out.println("\n****************************************");
                        System.out.println("               Bem-vindo                  ");
                        System.out.println("****************************************\n");
                        while(terminar_sessao == 0){
                            if(hasEstatuto){  // Pode trocar turnos
                                System.out.println("Selecione a opção desejada:");
                                System.out.println("  1 - Trocar turno");
                                System.out.println("  2 - Visualizar horário pessoal");
                                System.out.println("  3 - Visualizar horário geral");
                                System.out.println("  4 - Exportar horário pessoal");
                                System.out.println("  5 - Terminar sessão");
                                System.out.println("  6 - Sair da aplicação");
                                System.out.print("\nEscreva o número da opção: ");
                                int opcao = scanner.nextInt();
                                scanner.nextLine();
                                switch(opcao){
                                    case 1:
                                        System.out.print("Insira o turno que deseja trocar: ");
                                        String turno_antigo = scanner.nextLine();
                                        System.out.print("Insira a respetiva UC: ");
                                        String uc = scanner.nextLine();
                                        System.out.print("Insira o novo turno em que se pretende inscrever: ");
                                        String turno_novo = scanner.nextLine();
                                        horariosFacade.efetuaTroca(aluno.getNumero(), (uc + "_" + turno_antigo), (uc + "_" + turno_novo));
                                        horariosFacade.atualizaHorario(aluno.getNumero(), (uc + "_" + turno_antigo), (uc + "_" + turno_novo));
                                        break;
                                    case 2:
                                        int nAluno = aluno.getNumero();
                                        System.out.println(horariosFacade.getHorario(nAluno));
                                        break;
                                    case 3:
                                        System.out.println(horariosFacade.imprimeHorarioGeral(aluno.getSemestre()));
                                        break;
                                    case 4:
                                        horariosFacade.exportarHorario(aluno.getNumero());
                                        break;
                                    case 5:
                                        terminar_sessao = 1;
                                        System.out.println("A terminar sessão...");
                                        break;
                                    case 6:
                                        System.out.println("A sair do programa...");
                                        scanner.close();
                                        return;
                                    default:
                                        break;
                                }
                            }
                            else{
                                System.out.println("Selecione a opção que deseja:");
                                System.out.println("  1 - Visualizar horário pessoal");
                                System.out.println("  2 - Visualizar horário geral");
                                System.out.println("  3 - Exportar horário pessoal");
                                System.out.println("  4 - Terminar sessão");
                                System.out.println("  5 - Sair da aplicação");
                                System.out.print("Digite o número da opção: ");

                                int opcao = scanner.nextInt();
                                scanner.nextLine();
                                switch(opcao){
                                    case 1:
                                        int nAluno = aluno.getNumero();
                                        System.out.println(horariosFacade.getHorario(nAluno));
                                        break;
                                    case 2:
                                        System.out.println(horariosFacade.imprimeHorarioGeral(aluno.getSemestre()));
                                        break;
                                    case 3:
                                        horariosFacade.exportarHorario(aluno.getNumero());
                                        break;
                                    case 4:
                                        terminar_sessao = 1;
                                        System.out.println("A terminar sessão...");
                                        break;
                                    case 5:
                                        System.out.println("A sair do programa...");
                                        scanner.close();
                                        return;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    else{
                        System.out.println("Password incorreta");
                    }
                }
            }
        }
    }   
}