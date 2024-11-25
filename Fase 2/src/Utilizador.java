package src;
public abstract class Utilizador {
    protected String email;    // Protected para permitir acesso nas subclasses
    protected String password;


    //-----------construtores---------------
    public Utilizador (){
        this.email = "";
        this.password = "";
    }

    public Utilizador(String email, String password) {
        this.email = email;
        this.password = password;
    }
    //--------------------------------------
    //--------------funções-----------------
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //------------------------------------
}