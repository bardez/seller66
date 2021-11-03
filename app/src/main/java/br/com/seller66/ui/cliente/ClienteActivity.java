package br.com.seller66.ui.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import br.com.seller66.R;
import br.com.seller66.adapter.ListaClienteAdapter;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.Rota;
import br.com.seller66.tasks.GetClientsTask;
import br.com.seller66.tasks.GetProductsTask;
import br.com.seller66.ui.produto.ProdutoActivity;

public class ClienteActivity extends AppCompatActivity {

    private ListView lista_clientes;
    private Rota rota;
    private  List<Cliente> clientes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        Intent intent = getIntent();
        rota = (Rota) intent.getSerializableExtra("data");

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(String.format("Clientes - %s", rota.getName()));
        setSupportActionBar(toolbar);

        initilizeView();
    }


    private void initilizeView() {
        lista_clientes = findViewById(R.id.lista_cliente);
        lista_clientes.setAdapter(new ListaClienteAdapter(ClienteActivity.this, clientes));
        lista_clientes.setOnItemClickListener((adapterView, view, i, l) -> {
                Cliente itemClient = (Cliente) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ClienteActivity.this, ProdutoActivity.class);
                intent.putExtra("rota", rota);
                intent.putExtra("cliente", itemClient);
                startActivity(intent);
        });

        new GetClientsTask(this, lista_clientes, String.valueOf(rota.getId())).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetClientsTask(this, lista_clientes, String.valueOf(rota.getId())).execute();
    }
}