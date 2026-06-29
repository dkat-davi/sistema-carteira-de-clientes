package br.com.daviteixeira.carteiraclientes.model;

import java.time.LocalDateTime;

public class Atendimento {

    private int id;
    private int clienteId;
    private int usuarioId;
    private LocalDateTime dataAtendimento;
    private String descricao;

    private String clienteNome;
    private String usuarioNome;
    private String lojaNome;

    public Atendimento() {
    }

    public Atendimento(int id, int clienteId, int usuarioId, LocalDateTime dataAtendimento, String descricao) {
        this.id = id;
        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
        this.dataAtendimento = dataAtendimento;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }    

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }    

    public LocalDateTime getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(LocalDateTime dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }    

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }    

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
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
