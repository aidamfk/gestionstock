package gestionstock.model;

public class Utilisateur {

    private int idUser;
    private String login;
    private String motDePasse;
    private String role;

    public Utilisateur() {
    }

    public Utilisateur(int idUser, String login, String motDePasse, String role) {
        this.idUser = idUser;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
