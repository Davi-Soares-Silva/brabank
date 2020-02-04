package br.com.cursoandroid.brabank.model.entity;

public class Categoria {
    public Integer codigo;
    public String tipo, nome;

    public Categoria(Integer codigo, String tipo, String nome) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.nome = nome;
    }

    public Categoria() {

    }

    @Override
    public String toString() {
        return this.nome;
    }
}
