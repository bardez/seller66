package br.com.seller66.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import br.com.seller66.R;
import br.com.seller66.adapter.ListaRotaAdapter;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.Rota;
import br.com.seller66.tasks.GetClientsTask;
import br.com.seller66.tasks.GetRoutesTask;
import br.com.seller66.ui.cliente.ClienteActivity;

public class HomeFragment extends Fragment {

    private ListView lista_rota;
    private List<Rota> rotas;
    private List<Cliente> clientes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rotas = new ArrayList<>();
        initilizeView(root);
        return root;
    }

    private void initilizeView(View view) {
        lista_rota = view.findViewById(R.id.lista_rota);
        lista_rota.setAdapter(new ListaRotaAdapter(getContext(), rotas));
        lista_rota.setOnItemClickListener((adapterView, view1, i, l) -> {
            Rota itemRota = (Rota) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(getContext(), ClienteActivity.class);
            intent.putExtra("data", itemRota);
            startActivity(intent);
        });

        new GetRoutesTask(getActivity(), lista_rota, "1").execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetRoutesTask(getActivity(), lista_rota, "1").execute();
    }
}