package Supermarket.Servidor;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String nome;
    private String endereco;
    private int telefone;
    private String email;
    private String ID;
    private int senha;
    private boolean connected = false;

    public Usuario(String nome, String endereco, int telefone, String email, String ID) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.ID = ID;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public void setConnected(boolean b) {
        this.connected = b;
    }

    public String getUsuario() {
        return nome + "," + endereco + "," + telefone  + "," + email  + "," + ID  + "," + senha + "\n";
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getID() {
        return ID;
    }

    public int getSenha() {
        return senha;
    }

    public boolean getConnected() {
        return connected;
    }
}
