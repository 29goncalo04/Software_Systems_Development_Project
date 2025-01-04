package business.SSHorarios.Turnos;

public class Sala {
    private String nome;              // Nome do turno
    private int ocupacaoMaxima;       // Oucpação máxima do turno


    public Sala() {
        this.nome = "";
        this.ocupacaoMaxima = -1;
    }

    public Sala(String nome, int ocupacaoMaxima) {
        this.nome = nome;
        this.ocupacaoMaxima = ocupacaoMaxima;
    }


    public String getNome() {
        return nome;
    }

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


    // Devolve uma representação textual da sala
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sala: " + nome + " MAX: " + ocupacaoMaxima + " alunos");
        return sb.toString();
    }
}
