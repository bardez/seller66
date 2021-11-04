package br.com.seller66.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Pedido implements Serializable {
    private int id;
    private List<ItemPedido> itemList;
    private Date data_pedido;
    private float total;
    private int rota_id;
    private int cliente_id;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ItemPedido> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemPedido> produtoList) {
        this.itemList = produtoList;
    }

    public Date getData_pedido() {
        return data_pedido;
    }

    public void setData_pedido(Date data_pedido) {
        this.data_pedido = data_pedido;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getRota_id() {
        return rota_id;
    }

    public void setRota_id(int rota_id) {
        this.rota_id = rota_id;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
