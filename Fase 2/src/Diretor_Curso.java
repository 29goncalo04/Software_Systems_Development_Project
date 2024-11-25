package src;
public class Diretor_Curso extends Utilizador {

    //-----------construtores---------------
    public Diretor_Curso(){
        super();
    }

    public Diretor_Curso(String email, String password) {
        super(email, password); // Chama o construtor da classe abstrata
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
    //--------------------------------------
}