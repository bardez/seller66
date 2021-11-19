package br.com.seller66.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.seller66.model.Pedido;
import br.com.seller66.ui.produto.ProdutoActivity;

public class PutPedidoTask extends AsyncTask<String, String, String> {

    Activity pContext;
    Pedido pedido;

    public PutPedidoTask(Activity context, Pedido _pedido )
    {
        pContext = context;
        pedido = _pedido;
        AsyncTask.execute(() -> {
            try{
                URL endpoint = new URL("http://api.dev3s.com.br:8083/invoices/"+pedido.getId());
                HttpURLConnection conexao = (HttpURLConnection) endpoint.openConnection();
                conexao.setRequestProperty("User-Agent","seller66");
                conexao.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conexao.setRequestProperty("Accept", "application/json");
                conexao.setDoOutput(true);
                conexao.setDoInput(true);
                conexao.setRequestMethod("PUT");
                String jsonInputString = "{\"status\": \"F\"}";
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
                        try {
                            JSONObject jObj = new JSONObject(response.toString());
                            JSONObject value = jObj.getJSONObject("data");
                            pedido.setId(value.getInt("id"));
                        } catch (JSONException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((ProdutoActivity)pContext).setToolbar(String.valueOf(pedido.getId()));
    }
}
