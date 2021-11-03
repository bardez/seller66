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
import br.com.seller66.model.Cliente;
import br.com.seller66.model.ItemPedido;
import br.com.seller66.model.Pedido;
import br.com.seller66.model.Produto;

public class ListaProdutoAdapter extends BaseAdapter {

    private Context context;
    private List<Produto> produtos;
    private int currentQuantity = 0;
    private EditText editTextQuantidade;
    private TextView titleItemLista;
    private TextView textDescricaoProduto;
    private ImageButton addButton;
    private List<ItemPedido> itensPedido = new ArrayList<>();
    public ListaProdutoAdapter(Context context, List<Produto> produtos){
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Produto getItem(int i) {
        return produtos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return produtos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_produto, viewGroup, false);

        Produto produto = produtos.get(i);

        titleItemLista = view.findViewById(R.id.item_list_cliente_title);
        titleItemLista.setText(String.format("%d - %s", produto.getId(), produto.getDescricao()));

        addButton = view.findViewById(R.id.list_item_button);
        addButton.setOnClickListener(view1 -> callQuantityModal(produto));

        return view;
    }

    private void callQuantityModal(Produto produto) {

        AlertDialog quantityAlert = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        final View addProdutoView = factory.inflate(R.layout.fragment_add_produto_view, null);
        quantityAlert.setView(addProdutoView);
        textDescricaoProduto = addProdutoView.findViewById(R.id.product_description);
        textDescricaoProduto.setText(String.format("%s \nR$%s", produto.getDescricao(), String.format("%.2f",produto.getValue()).replace(".", ",")));
        editTextQuantidade = addProdutoView.findViewById(R.id.edittext_quantidade);
        Stream<ItemPedido> filter = itensPedido.stream().parallel()
                .filter(i -> (i.getProduto().getId() == produto.getId()));
        if(filter.count() > 0){
            ItemPedido it = itensPedido.stream().parallel()
                    .filter(i -> (i.getProduto().getId() == produto.getId())).findAny().get();
            currentQuantity = it.getQuantidade();
        } else {
            currentQuantity = 0;
        }

        editTextQuantidade.setText(String.valueOf(currentQuantity));

        addProdutoView.findViewById(R.id.plus).setOnClickListener(v -> {
            currentQuantity += 1;
            editTextQuantidade.setText(String.valueOf(currentQuantity));
        });
        addProdutoView.findViewById(R.id.minus).setOnClickListener(v -> {
            currentQuantity -= 1;
            editTextQuantidade.setText(String.valueOf(currentQuantity));
        });

        addProdutoView.findViewById(R.id.cancel_button).setOnClickListener(v -> {
            currentQuantity = 0;
            quantityAlert.dismiss();
        });

        addProdutoView.findViewById(R.id.confirm_button).setOnClickListener(v -> {
            ItemPedido _itemPedido = new ItemPedido();
            _itemPedido.setProduto(produto);
            _itemPedido.setQuantidade(currentQuantity);
            itensPedido.add(_itemPedido);
            currentQuantity = 0;
            quantityAlert.dismiss();
        });
        quantityAlert.show();
    }
}
