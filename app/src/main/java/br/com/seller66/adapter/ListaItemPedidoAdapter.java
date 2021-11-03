package br.com.seller66.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import br.com.seller66.R;
import br.com.seller66.model.ItemPedido;
import br.com.seller66.model.Produto;

public class ListaItemPedidoAdapter extends BaseAdapter {

    private Context context;
    private List<String> produtos;
    private TextView desc;

    public ListaItemPedidoAdapter(Context context, List<String> produtos){
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public String getItem(int i) {
        return produtos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_pedido, viewGroup, false);

        String produto = produtos.get(i);

        desc = view.findViewById(R.id.item_list_item_prod_desc);
        desc.setText( produto );
        return view;
    }
}
