package br.com.seller66.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.seller66.adapter.ListaClienteAdapter;
import br.com.seller66.adapter.ListaProdutoAdapter;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.Produto;

public class GetProductsTask extends AsyncTask<String, String, String> {

    Activity pContext;
    ListView pList;
    List<Produto> prods = new ArrayList<>();

    public GetProductsTask(Activity context, ListView list)
    {
        pContext = context;
        pList = list;
        AsyncTask.execute(() -> {
            try{
                URL endpoint = new URL("http://api.dev3s.com.br:8083/products");
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
                            jsonReader.beginObject();
                            while(jsonReader.hasNext())
                            {
                                String key = jsonReader.nextName();
                                if(key.equals("rows")) {
                                    jsonReader.beginArray();
                                    while (jsonReader.hasNext()){
                                        jsonReader.beginObject();
                                        Produto _prod = new Produto();
                                        while(jsonReader.hasNext()) {
                                            String prop = jsonReader.nextName();
                                            if (prop.equals("id")) {
                                                int prodId = jsonReader.nextInt();
                                                _prod.setId(prodId);
                                            }
                                            prop = jsonReader.nextName();
                                            if (prop.equals("name")) {
                                                String prodDesc = jsonReader.nextString();
                                                _prod.setDescricao(prodDesc);
                                            }
                                            prop = jsonReader.nextName();
                                            if (prop.equals("active")) {
                                                int prodStatus = jsonReader.nextInt();
                                                _prod.setStatus(prodStatus);
                                            }
                                            prop = jsonReader.nextName();
                                            if (prop.equals("value")) {
                                                String prodValue = jsonReader.nextString();
                                                _prod.setValue(Float.parseFloat(prodValue));
                                            }
                                        }
                                        jsonReader.endObject();
                                        prods.add(_prod);
                                    }
                                    jsonReader.endArray();

                                } else {
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
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
        pList.setAdapter(new ListaProdutoAdapter(pContext, prods));
        ((ListaProdutoAdapter)pList.getAdapter()).notifyDataSetChanged();
    }
}
