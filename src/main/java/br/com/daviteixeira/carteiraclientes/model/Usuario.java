package br.com.daviteixeira.carteiraclientes.model;

public class Usuario {

    private int id;
    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private String role;
    private boolean ativo;

    public Usuario() {
    }

    public Usuario(int id, String nome, String cpf, String login, String senha, String role, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.role = role;
        this.ativo = ativo;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isUsuario() {
        return "USUARIO".equalsIgnoreCase(role);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }    

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }    

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }    

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }    

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }    

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}