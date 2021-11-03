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

import br.com.seller66.adapter.ListaClienteAdapter;
import br.com.seller66.model.Cliente;

public class GetClientsTask extends AsyncTask<String, String, String> {

    Activity pContext;
    ListView pList;
    ArrayList<Cliente> clients;

    public GetClientsTask(Activity context, ListView list, String routeId)
    {
        pContext = context;
        pList = list;
        AsyncTask.execute(() -> {
            try{
                URL endpoint = new URL("http://api.dev3s.com.br:8083/clients/"+routeId);
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
                            clients = new ArrayList<>();
                            while(jsonReader.hasNext())
                            {
                                jsonReader.beginObject();
                                Cliente _cliente = new Cliente();
                                while(jsonReader.hasNext()) {
                                    String prop = jsonReader.nextName();
                                    if (prop.equals("id")) {
                                        int clientId = jsonReader.nextInt();
                                        _cliente.setId(clientId);
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("name")) {
                                        String clientName = jsonReader.nextString();
                                        _cliente.setNome(clientName);
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("phone")) {
                                        String clientTel = jsonReader.nextString();
                                        _cliente.setTelefone(clientTel);
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("address")) {
                                        String clientEndereco = jsonReader.nextString();
                                        _cliente.setEndereco(clientEndereco);
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("state")) {
                                        String clientEstado = jsonReader.nextString();
                                        _cliente.setEstado(clientEstado);
                                    }
                                }
                                jsonReader.endObject();
                                clients.add(_cliente);
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
        pList.setAdapter(new ListaClienteAdapter(pContext, clients));
        ((ListaClienteAdapter)pList.getAdapter()).notifyDataSetChanged();
    }
}
