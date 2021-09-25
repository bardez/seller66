package br.com.seller66.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.seller66.R;
import br.com.seller66.model.Rota;

public class ListaRotaAdapter extends BaseAdapter {

    private Context context;
    private List<Rota> rotas;

    private TextView textoItemLista;

    public ListaRotaAdapter(Context context, List<Rota> rotas){
        this.context = context;
        this.rotas = rotas;
    }

    @Override
    public int getCount() {
        return rotas.size();
    }

    @Override
    public Rota getItem(int i) {
        return rotas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return rotas.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_rota, null);

        Rota rotaItem = rotas.get(i);

        textoItemLista = (TextView) view.findViewById(R.id.item_list_rota_texto);
        textoItemLista.setText(rotaItem.getName());
        return view;
    }
}
