package br.com.seller66.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.seller66.adapter.ListaProdutoAdapter;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.ItemPedido;
import br.com.seller66.model.Produto;
import br.com.seller66.ui.produto.ProdutoActivity;

public class GetItemPedidoTask extends AsyncTask<String, String, String> {

    BaseAdapter pContext;
    ItemPedido itemPedido = new ItemPedido();

    public GetItemPedidoTask(BaseAdapter context, String pedido_id)
    {
        pContext = context;
        AsyncTask.execute(() -> {
            try{
                URL endpoint = new URL("http://api.dev3s.com.br:8083/invoice-items/"+pedido_id);
                HttpURLConnection conexao = (HttpURLConnection) endpoint.openConnection();
                conexao.setRequestProperty("User-Agent","seller66");
                if(conexao.getResponseCode() == 200)
                {
                    conexao.getResponseMessage();
                    InputStream resposta = conexao.getInputStream();
                    InputStreamReader respostaReader = new InputStreamReader(resposta, "UTF-8");
                    JsonReader jsonReader = new JsonReader(respostaReader);

                    jsonReader.beginObject();
                    while(jsonReader.hasNext())
                    {
                        String status = jsonReader.nextName();
                        if(status.equals("data")) {
                            jsonReader.beginArray();
                            Produto p = new Produto();
                            //{"id":1,"product_id":1,"invoice_id":13,"quantity":"5.00","created_at":"2021-11-04T01:39:43.000Z","updated_at":"2021-11-04T01:39:43.000Z","deleted_at":null,"name":"Agulha Hipodérmica 20 x 0,55 mm Caixa 100 Unidades","active":0,"value":"19.90"}
                            while(jsonReader.hasNext())
                            {
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()) {
                                    String prop = jsonReader.nextName();
                                    if (prop.equals("id")) {
                                        Log.i("SAIDA", "Id:" + jsonReader.nextInt());
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("invoice_id")) {
                                        itemPedido.setInvoiceId(jsonReader.nextInt());
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("product_id")) {
                                        p.setId(jsonReader.nextInt());
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("quantity")) {
                                        itemPedido.setQuantidade(Float.parseFloat(jsonReader.nextString()));
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("name")) {
                                        p.setDescricao(jsonReader.nextString());
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("active")) {
                                        p.setStatus(jsonReader.nextInt());
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("value")) {
                                        p.setValue(Float.parseFloat(jsonReader.nextString()));
                                    }
                                }
                                jsonReader.endObject();
                                itemPedido.setProduto(p);
                            }
                            jsonReader.endArray();
                        }
                        else
                        {
                            jsonReader.skipValue();
                        }
                    }
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
    protected void onPostExecute(String result) {
        ((ListaProdutoAdapter)pContext).setCurrentItemPedido(itemPedido);
    }
}
