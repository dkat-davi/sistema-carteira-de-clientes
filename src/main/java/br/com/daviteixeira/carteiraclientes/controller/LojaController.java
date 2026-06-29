package br.com.daviteixeira.carteiraclientes.controller;

import br.com.daviteixeira.carteiraclientes.dao.LojaDAO;
import br.com.daviteixeira.carteiraclientes.model.Loja;

import java.util.List;

public class LojaController {

    private final LojaDAO lojaDAO;

    public LojaController() {
        this.lojaDAO = new LojaDAO();
    }

    public void salvar(Loja loja) {
        validar(loja);

        loja.setNome(loja.getNome().trim());
        loja.setTelefone(normalizarTexto(loja.getTelefone()));
        loja.setEndereco(normalizarTexto(loja.getEndereco()));
        loja.setCidade(normalizarTexto(loja.getCidade()));

        if (loja.getEstado() != null) {
            loja.setEstado(loja.getEstado().trim().toUpperCase());
        }

        if (loja.getId() == 0) {
            lojaDAO.salvar(loja);
        } else {
            lojaDAO.atualizar(loja);
        }
    }

    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecione uma loja para excluir.");
        }

        lojaDAO.excluir(id);
    }

    public Loja buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da loja inválido.");
        }

        return lojaDAO.buscarPorId(id);
    }

    public List<Loja> listarTodas() {
        return lojaDAO.listarTodas();
    }

    public List<Loja> pesquisarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return listarTodas();
        }

        return lojaDAO.pesquisarPorNome(nome.trim());
    }

    private void validar(Loja loja) {
        if (loja == null) {
            throw new IllegalArgumentException("Dados da loja não informados.");
        }

        if (loja.getNome() == null || loja.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome da loja.");
        }

        if (loja.getEstado() != null && !loja.getEstado().trim().isEmpty()) {
            if (loja.getEstado().trim().length() != 2) {
                throw new IllegalArgumentException("O estado deve conter apenas a sigla com 2 letras. Exemplo: MG.");
            }
        }
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }

        return texto.trim();
    }
}