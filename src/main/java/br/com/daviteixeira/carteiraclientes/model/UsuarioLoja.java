package br.com.daviteixeira.carteiraclientes.model;

public class UsuarioLoja {

    private int id;
    private int usuarioId;
    private int lojaId;
    private String cargo;

    private String usuarioNome;
    private String lojaNome;

    public UsuarioLoja() {
    }

    public UsuarioLoja(int id, int usuarioId, int lojaId, String cargo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.lojaId = lojaId;
        this.cargo = cargo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }    

    public int getLojaId() {
        return lojaId;
    }

    public void setLojaId(int lojaId) {
        this.lojaId = lojaId;
    }    

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }    

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }    

    public String getLojaNome() {
        return lojaNome;
    }

    public void setLojaNome(String lojaNome) {
        this.lojaNome = lojaNome;
    }
}