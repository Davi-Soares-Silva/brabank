package br.com.cursoandroid.brabank.model.entity;

import java.util.Date;

public class Lancamento {
    public Integer codigo;
    public String tipo, descricao;
    public double valor;
    public Categoria categoria;
    public Date data;

    public Lancamento(Integer codigo, String tipo, String descricao, double valor, Categoria categoria, Date data) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
    }

    public Lancamento() {
    }
}




