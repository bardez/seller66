package br.com.seller66.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.seller66.adapter.ListaRotaAdapter;
import br.com.seller66.model.ItemPedido;
import br.com.seller66.model.Pedido;
import br.com.seller66.model.Produto;
import br.com.seller66.model.Rota;
import br.com.seller66.ui.produto.ProdutoActivity;

public class PostItemPedidoTask extends AsyncTask<String, String, String> {

    Activity pContext;
    String pedido_id;

    public PostItemPedidoTask(Activity context, String id, ItemPedido itemPedido)
    {
        pContext = context;
        pedido_id = id;
        AsyncTask.execute(() -> {
            try{
                URL endpoint = new URL("http://api.dev3s.com.br:8083/invoice-items/"+pedido_id);
                HttpURLConnection conexao = (HttpURLConnection) endpoint.openConnection();
                conexao.setRequestProperty("User-Agent","seller66");
                conexao.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conexao.setRequestProperty("Accept", "application/json");
                conexao.setDoOutput(true);
                conexao.setDoInput(true);
                conexao.setRequestMethod("PUT");
                String jsonInputString = "{\"product_id\": "+ itemPedido.getProduto().getId()+", \"quantity\": "+itemPedido.getQuantidade()+"}";
                try(OutputStream os = conexao.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("UTF-8");
                    os.write(input);
                }
                int responseCode = conexao.getResponseCode();
                if( responseCode == 200)
                {
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(conexao.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        System.out.println(response.toString());
                    }
                } else {
                    throw new Exception(String.format("Erro ao criar dados - código: %d", responseCode));
                }
            }
            catch (Exception e)
            {
                Log.i("SAIDA", "erro ao consumir api:" + e.getMessage());
            }
        });
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
