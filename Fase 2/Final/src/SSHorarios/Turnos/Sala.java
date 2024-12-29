package SSHorarios.Turnos;

public class Sala {
    private String nome;               // Nome da sala
    private int ocupacaoMaxima;        // Ocupação máxima da sala

    // ----------- Construtores ---------------

    // Construtor por defeito que inicializa a sala com valores padrão
    public Sala() {
        this.nome = "";
        this.ocupacaoMaxima = -1;
    }

    // Construtor que inicializa a sala com o nome e a ocupação máxima fornecidos
    public Sala(String nome, int ocupacaoMaxima) {
        this.nome = nome;
        this.ocupacaoMaxima = ocupacaoMaxima;
    }

    // ----------- Getters e Setters ---------------

    // Devolve o nome da sala
    public String getNome() {
        return nome;
    }

    // Define o nome da sala
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Devolve a ocupação máxima da sala
    public int getOcupacaoMaxima() {
        return ocupacaoMaxima;
    }

    // Define a ocupação máxima da sala
    public void setOcupacaoMaxima(int ocupacaoMaxima) {
        this.ocupacaoMaxima = ocupacaoMaxima;
    }

    // ----------- Funções ---------------

    // Devolve uma representação textual da sala
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Sala: 1.01  MAX: 32 alunos
        sb.append("Sala: " + nome + " MAX: " + ocupacaoMaxima + " alunos");
        return sb.toString();
    }
}
