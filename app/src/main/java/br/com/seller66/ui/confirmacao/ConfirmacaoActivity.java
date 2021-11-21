package br.com.seller66.ui.confirmacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import br.com.seller66.MainActivity;
import br.com.seller66.R;
import br.com.seller66.model.Cliente;
import br.com.seller66.model.ItemPedido;
import br.com.seller66.model.Pedido;
import br.com.seller66.model.Rota;
import br.com.seller66.ui.cliente.ClienteActivity;

public class ConfirmacaoActivity extends AppCompatActivity {
    Rota rota;
    Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);

        Intent intent = getIntent();
        rota = (Rota) intent.getSerializableExtra("rota");
        pedido = (Pedido) intent.getSerializableExtra("pedido");

        initializeView();
    }

    private void initializeView() {
        Button btn_nova_rota = findViewById(R.id.btn_nova_rota);
        btn_nova_rota.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ClienteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("data", rota);
            startActivity(intent);
        });

        Button btn_whatsapp = findViewById(R.id.btn_whatsapp);
        btn_whatsapp.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.putExtra(Intent.EXTRA_TEXT, getPedidoString());
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, "Compartilhar pedido");
            startActivity(shareIntent);
        });
    }

    private String getPedidoString(){
        String pedidoString = "";
        pedidoString = "Pedido: "+pedido.getId()+"\n"+
        "Itens:\n";
        for(int l=0; l<pedido.getItemList().size(); l++){
            pedidoString += (pedido.getItemList().get(l).getQuantidade()+" x " +pedido.getItemList().get(l).getProduto().getDescricao());
            pedidoString += "\n";
        }

        return pedidoString;
    }
}
