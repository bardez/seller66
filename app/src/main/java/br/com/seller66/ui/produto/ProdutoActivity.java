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
import br.com.seller66.adapter.ListaProdutoAdapter;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.ItemPedido;
import br.com.seller66.model.Pedido;
import br.com.seller66.model.Produto;
import br.com.seller66.model.Rota;
import br.com.seller66.ui.cliente.ClienteActivity;
import br.com.seller66.ui.confirmacao.ConfirmacaoActivity;

public class ProdutoActivity extends AppCompatActivity {

    private ListView lista_produtos;
    private List<Produto> produtos = new ArrayList<>();
    private Cliente cliente;
    private Pedido pedido = new Pedido();
    private Rota rota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callConfirmationModal();
            }
        });

        Intent intent = getIntent();
        cliente = (Cliente) intent.getSerializableExtra("cliente");
        rota = (Rota) intent.getSerializableExtra("rota");

        pedido.setCliente_id(cliente.getId());
        pedido.setId(1);
        Date date = new Date();
        pedido.setData_pedido(date);
        pedido.setRota_id(rota.getId());

        Produto p1 = new Produto();
        p1.setDescricao("Esparadrapo");
        p1.setId(1);
        p1.setStatus(1);
        produtos.add(p1);

        Produto p2 = new Produto();
        p2.setDescricao("Faixa");
        p2.setId(2);
        p2.setStatus(1);
        produtos.add(p2);

        Produto p3 = new Produto();
        p3.setDescricao("Agulha");
        p3.setId(3);
        p3.setStatus(1);
        produtos.add(p3);

        Produto p4 = new Produto();
        p4.setDescricao("Seringa");
        p4.setId(4);
        p4.setStatus(1);
        produtos.add(p4);

        Produto p5 = new Produto();
        p5.setDescricao("Algodão");
        p5.setId(5);
        p5.setStatus(1);
        produtos.add(p5);

        Produto p6 = new Produto();
        p6.setDescricao("Pinça");
        p6.setId(6);
        p6.setStatus(0);
        produtos.add(p6);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(String.format("Pedido:  %s", pedido.getId()));
        setSupportActionBar(toolbar);

        initilizeView();
    }


    private void initilizeView() {
        lista_produtos = findViewById(R.id.lista_cliente);
        lista_produtos.setAdapter(new ListaProdutoAdapter(ProdutoActivity.this, produtos));
        lista_produtos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void callConfirmationModal() {

        AlertDialog confirmationAlert = new AlertDialog.Builder(ProdutoActivity.this).create();
        LayoutInflater factory = LayoutInflater.from(ProdutoActivity.this);
        final View confirmationView = factory.inflate(R.layout.fragment_confirma_pedido_view, null);
        confirmationAlert.setView(confirmationView);

        confirmationView.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationAlert.dismiss();
            }
        });

        confirmationView.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationAlert.dismiss();
                Intent intent = new Intent(ProdutoActivity.this, ConfirmacaoActivity.class);
                intent.putExtra("pedido", pedido);
                startActivity(intent);
            }
        });
        confirmationAlert.show();
    }
}