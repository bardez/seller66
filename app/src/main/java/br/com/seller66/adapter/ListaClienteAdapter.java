package br.com.seller66.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.seller66.R;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.Rota;

public class ListaClienteAdapter extends BaseAdapter {

    private Context context;
    private List<Cliente> clientes;

    private TextView titleItemLista;
    private TextView contentItemLista;

    public ListaClienteAdapter(Context context, List<Cliente> clientes){
        this.context = context;
        this.clientes = clientes;
    }

    @Override
    public int getCount() {
        return clientes.size();
    }

    @Override
    public Cliente getItem(int i) {
        return clientes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return clientes.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_cliente, viewGroup, false);

        Cliente cliente = clientes.get(i);

        titleItemLista = view.findViewById(R.id.item_list_cliente_title);
        titleItemLista.setText(cliente.getNome());

        contentItemLista = view.findViewById(R.id.item_list_cliente_content);
        contentItemLista.setText(String.format("%s - %s", cliente.getEndereco(), cliente.getEstado()));
        return view;
    }
}
