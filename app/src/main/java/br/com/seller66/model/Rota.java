package br.com.seller66.model;

import java.io.Serializable;
import java.util.List;

public class Rota implements Serializable {
    private int id;
    private String name;
    private List<Cliente> clienteList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }
}
