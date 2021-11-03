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

import br.com.seller66.adapter.ListaRotaAdapter;
import br.com.seller66.model.Rota;

public class GetRoutesTask extends AsyncTask<String, String, String> {

    Activity pContext;
    ListView pList;
    ArrayList<Rota> routes;

    public GetRoutesTask(Activity context, ListView list, String user)
    {
        pContext = context;
        pList = list;
        AsyncTask.execute(() -> {
            try{
                URL endpoint = new URL("http://api.dev3s.com.br:8083/routes/"+user);
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
                            routes = new ArrayList<>();
                            while(jsonReader.hasNext())
                            {
                                jsonReader.beginObject();
                                Rota _rota = new Rota();
                                while(jsonReader.hasNext()) {
                                    String prop = jsonReader.nextName();
                                    if (prop.equals("id")) {
                                        int routeId = jsonReader.nextInt();
                                        _rota.setId(routeId);
                                    }
                                    prop = jsonReader.nextName();
                                    if (prop.equals("name")) {
                                        String routeName = jsonReader.nextString();
                                        _rota.setName(routeName);
                                    }
                                }
                                jsonReader.endObject();
                                routes.add(_rota);
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
        pList.setAdapter(new ListaRotaAdapter(pContext, routes));
    }
}
