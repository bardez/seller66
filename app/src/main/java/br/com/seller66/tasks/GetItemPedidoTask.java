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
    ItemPedido itemPedido;
    ItemPedido itemPedidoSelecionado;
    List<ItemPedido> itens = new ArrayList<>();

    public GetItemPedidoTask(BaseAdapter context, String pedido_id, String produto_id)
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
                            while(jsonReader.hasNext())
                            {
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()) {
                                    itemPedido = new ItemPedido();
                                    Produto p = new Produto();
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
                                    itemPedido.setProduto(p);
                                    if(!produto_id.isEmpty() && produto_id.equals(String.valueOf(itemPedido
                                    .getProduto().getId()))){
//                                        itemPedidoSelecionado = itemPedido;
                                        ((ListaProdutoAdapter)pContext).setCurrentItemPedido(itemPedido);
                                    }
                                }
                                jsonReader.endObject();
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
        if(itemPedidoSelecionado != null)
            ((ListaProdutoAdapter)pContext).setCurrentItemPedido(itemPedido);
    }
}
