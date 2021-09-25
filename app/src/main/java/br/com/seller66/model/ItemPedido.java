package br.com.seller66.model;

import java.io.Serializable;

public class ItemPedido implements Serializable {
    private Produto produto;
    private float quantidade;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }
}
