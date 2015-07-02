package Supermarket.Servidor;

import java.io.Serializable;

public class Produto implements Serializable {
    private String nome;
    private double preco;
    private String validade;
    private String fornecedor;
    private int quantidade;

    Produto(String nome, double preco, String validade, String fornecedor) {
        this.nome = nome;
        this.preco = preco;
        this.validade = validade;
        this.fornecedor = fornecedor;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getProduto() {
        return nome + "," + preco + "," + validade + "," + fornecedor + "," + quantidade + "\n";
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getValidade() {
        return validade;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
