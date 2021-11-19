package br.com.seller66.ui.produto;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import br.com.seller66.R;
import br.com.seller66.adapter.ListaClienteAdapter;
import br.com.seller66.adapter.ListaItemPedidoAdapter;
import br.com.seller66.adapter.ListaProdutoAdapter;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.ItemPedido;
import br.com.seller66.model.Pedido;
import br.com.seller66.model.Produto;
import br.com.seller66.model.Rota;
import br.com.seller66.tasks.GetItemPedidoTask;
import br.com.seller66.tasks.GetItensPedidoTask;
import br.com.seller66.tasks.GetProductsTask;
import br.com.seller66.tasks.PostItemPedidoTask;
import br.com.seller66.tasks.PostPedidoTask;
import br.com.seller66.tasks.PutPedidoTask;
import br.com.seller66.ui.cliente.ClienteActivity;
import br.com.seller66.ui.confirmacao.ConfirmacaoActivity;
import br.com.seller66.utils.IAsyncResponse;

public class ProdutoActivity extends AppCompatActivity {

    private ListView lista_produtos;
    private ListView lista_item_pedidos;
    private List<Produto> produtos = new ArrayList<>();
    private List<ItemPedido> itensPedido = new ArrayList<>();
    private Cliente cliente;
    private Pedido pedido = new Pedido();
    private Rota rota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            new GetItensPedidoTask(this, String.valueOf(pedido.getId()), "").execute();
        });

        Intent intent = getIntent();
        cliente = (Cliente) intent.getSerializableExtra("cliente");
        rota = (Rota) intent.getSerializableExtra("rota");

        pedido.setCliente_id(cliente.getId());
        Date date = new Date();
        pedido.setData_pedido(date);
        pedido.setRota_id(rota.getId());
        pedido.setItemList(itensPedido);
        pedido.setStatus("A");

        new PostPedidoTask(this, pedido).execute();

        initilizeView();
    }

    public void setToolbar(String pedidoId){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(String.format("Pedido:  %s", pedidoId));
        setSupportActionBar(toolbar);
    }

    public void putItemPedido(ItemPedido  it){
        new PostItemPedidoTask(this, String.valueOf(pedido.getId()),it).execute();
    }

    public void setItensPedido(List<ItemPedido> itens){
        pedido.setItemList(itens);
        callConfirmationModal();
    }

    private void initilizeView() {
        lista_produtos = findViewById(R.id.lista_produto);
        lista_produtos.setAdapter(new ListaProdutoAdapter(ProdutoActivity.this, produtos));
        lista_produtos.setOnItemClickListener((adapterView, view, i, l) -> {

        });

        new GetProductsTask(this, lista_produtos).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetProductsTask(this, lista_produtos).execute();
    }

    private void callConfirmationModal() {

        AlertDialog confirmationAlert = new AlertDialog.Builder(ProdutoActivity.this).create();
        LayoutInflater factory = LayoutInflater.from(ProdutoActivity.this);
        final View confirmationView = factory.inflate(R.layout.fragment_confirma_pedido_view, null);
        confirmationAlert.setView(confirmationView);

        List<String> itensPedido = new ArrayList<>();
        pedido.getItemList().forEach(i ->
                itensPedido.add(i.getQuantidade()+" x " +i.getProduto().getDescricao()));

        lista_item_pedidos = confirmationView.findViewById(R.id.lista_item_pedido);
        lista_item_pedidos.setAdapter(new ListaItemPedidoAdapter(ProdutoActivity.this, itensPedido));
        ((ListaItemPedidoAdapter)lista_item_pedidos.getAdapter()).notifyDataSetChanged();

        confirmationView.findViewById(R.id.cancel_button).setOnClickListener(v -> confirmationAlert.dismiss());

        confirmationView.findViewById(R.id.confirm_button).setOnClickListener(v -> {
            new PutPedidoTask(this, pedido, rota).execute();
            confirmationAlert.dismiss();
            Intent intent = new Intent(ProdutoActivity.this, ConfirmacaoActivity.class);
            intent.putExtra("pedido", pedido);
            startActivity(intent);
        });
        confirmationAlert.show();
    }

    public String getPedidoId() {
        return String.valueOf(pedido.getId());
    }

    public void setItemsPedido(List<ItemPedido> itens) {
        pedido.setItemList(itens);
    }
}