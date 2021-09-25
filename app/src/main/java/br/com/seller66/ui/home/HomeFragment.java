package br.com.seller66.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import br.com.seller66.R;
import br.com.seller66.adapter.ListaRotaAdapter;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.Rota;
import br.com.seller66.ui.cliente.ClienteActivity;

public class HomeFragment extends Fragment {

    private ListView lista_rota;
    private List<Rota> rotas;
    private List<Cliente> clientes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //CHAMADA PRO BANCO DE DADOS
        Cliente _cliente = new Cliente();
        _cliente.setNome("Joseph Climber");
        _cliente.setId(1);
        _cliente.setCidade("Bauru");

        Cliente _cliente2 = new Cliente();
        _cliente2.setNome("Mary thompson");
        _cliente2.setId(2);
        _cliente2.setCidade("Ourinhos");

        Cliente _cliente3 = new Cliente();
        _cliente3.setNome("John Doe");
        _cliente3.setId(3);
        _cliente3.setCidade("Ipaussu");

        clientes = new ArrayList<>();
        clientes.add(_cliente);
        clientes.add(_cliente2);
        clientes.add(_cliente3);

        Rota _rota1 = new Rota();
        _rota1.setId(1);
        _rota1.setName("Rota Califórnia");
        _rota1.setClienteList(clientes);

        Rota _rota2 = new Rota();
        _rota2.setId(2);
        _rota2.setName("Rota 66");
        _rota2.setClienteList(clientes);

        rotas = new ArrayList<>();
        rotas.add(_rota1);
        rotas.add(_rota2);

        initilizeView(root);
        return root;
    }

    private void initilizeView(View view) {
        lista_rota = view.findViewById(R.id.lista_rota);
        lista_rota.setAdapter(new ListaRotaAdapter(getContext(), rotas));
        lista_rota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Rota itemRota = (Rota) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), ClienteActivity.class);
                intent.putExtra("data", itemRota);
                startActivity(intent);
            }
        });
    }
}