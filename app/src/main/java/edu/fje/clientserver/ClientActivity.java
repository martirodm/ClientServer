package edu.fje.clientserver;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        textView = findViewById(R.id.textViewClientStatus);

        // Iniciar el cliente en un hilo aparte para evitar bloquear el hilo principal
        new Thread(new Runnable() {
            @Override
            public void run() {
                startClient();
            }
        }).start();
    }

    private void startClient() {
        String serverAddress = "192.168.1.189"; // Cambiar por la dirección IP del servidor
        int port = 12345; // Puerto para la conexión

        try {
            Socket socket = new Socket(serverAddress, port);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Conectado al servidor.");
                }
            });

            String mensaje = "Hola, servidor!";
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write((mensaje + "\n").getBytes());

            // Recepción de la respuesta del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final String serverResponse = in.readLine();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Respuesta del servidor: " + serverResponse);
                }
            });


            // Cerrar conexión
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Error: " + e.getMessage());
                }
            });
        }
    }
}
