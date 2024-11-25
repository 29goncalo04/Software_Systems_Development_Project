package src;
public class Aluno extends Utilizador {
    private String nome;
    private int numero;
    private float media;
    private String estatuto;
    private String situacao;
    private int nMatricula;

    //-----------construtores---------------
    public Aluno(){
        super();
        this.nome = "";
        this.numero = -1;
        this.media = -1;
        this.estatuto = "";
        this.situacao = "";
        this.nMatricula = -1;
    }

    public Aluno(String email, String password, String nome, int numero, float media, String estatuto, String situacao, int nMatricula) {
        super(email, password); // Chama o construtor da classe abstrata
        this.nome = nome;
        this.numero = numero;
        this.media = media;
        this.estatuto = estatuto;
        this.situacao = situacao;
        this.nMatricula = nMatricula;
    }
    //--------------------------------------
    //--------------funções-----------------
    public String getEmail(){
        return super.getEmail();
    }
    
    public void setEmail(String email){
        super.setEmail(email);
    }

    public String getPassword(){
        return super.getPassword();
    }

    public void setPassword(String password){
        super.setPassword(password);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public String getEstatuto() {
        return estatuto;
    }

    public void setEstatuto(String estatuto) {
        this.estatuto = estatuto;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public int getNMatricula() {
        return nMatricula;
    }

    public void setNMatricula(int nMatricula) {
        this.nMatricula = nMatricula;
    }
    //--------------------------------------
}