package com.example.chatjavasocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    EditText editText;
    String msg = "";
    Socket socket;
    PrintWriter out;
    Button conectar;
    Button enviar;
    String nome;
    BufferedReader in;
    TextView texto;
    ListView listaMensagens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.texto);
        enviar = (Button) findViewById(R.id.enviar);
        texto = (TextView) findViewById(R.id.lista);
        conectar = (Button) findViewById(R.id.conectar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(socket != null){
                        socketClient(editText.getText().toString());
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Você ainda não se conectou no chat!", Toast.LENGTH_LONG);
                        toast.show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!editText.getText().toString().equals("")){
                        conectar();
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Nome está vazio!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Cliente conectado...");
            }
        });

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    socketClient(editText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*if(!editText.getText().toString().isEmpty()) {
                    dados.add(editText.getText().toString());
                    adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.select_dialog_item, dados);
                    listview.setAdapter(adapter);
                    editText.getText().clear();
                }else{
                    Toast toast = Toast.makeText(getBaseContext(),"Campo vazio insira algum texto!...",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });*/
    }

    public void conectar() throws IOException {
        nome = editText.getText().toString();
        socketClient("Conectado...");
        editText.setText("");
        editText.setHint("Digite uma mensagem:");
        receberMensagem();
        conectar.setEnabled(false);
        enviar.setEnabled(true);
    }

    public void receberMensagem(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = " ";
                    Thread.sleep(2000);
                    while (true){
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        msg += in.readLine()+ "\n";
                        String finalMensagem = msg;
                        System.out.println(msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                texto.setText(finalMensagem);
                                editText.getText().clear();
                            }
                        });
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /*public void conectarSocket(String nomeUsuario){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.137.120",1001);
                    out = new PrintWriter(socket.getOutputStream());
                    out.println(nomeUsuario);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    public void socketClient(String texto) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(socket == null){
                        socket = new Socket("192.168.0.114",1001);
                    }
                    if(texto.equalsIgnoreCase("sair")){
                        out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(texto);
                    }
                    out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(nome+": "+texto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}