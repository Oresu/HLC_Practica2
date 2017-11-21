package com.example.zasha.hlc_practica2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressBar barra;
    private Button start;
    private Button stop;
    private TextView contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.comenzar);
        stop = (Button) findViewById(R.id.parar);
        barra = (ProgressBar) findViewById(R.id.barraProgreso);
        contador = (TextView) findViewById(R.id.contador);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Asine().execute();
                start.setClickable(false);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //new Asine().cancel(true);
                //new Asine().onCancelled();
            }
        });

    }//final onCreate


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    class Asine extends AsyncTask<Boolean, Integer, Boolean> {

        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barra.setMax(100); //Doy el valor maximo a la barra de progreso
            barra.setProgress(0); // y el valor actual

        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {

            for (int i = 0; i < 101; i++) {
                publishProgress(i); //Llamo a la funcion para comunicarme con el hilo principal
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) { // Llama a la funcion si el usuario cancela
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //Actualiza la barra de progreso
            barra.setProgress(values[0]);
            contador.setText(values[0].toString()+"%");
        }

        @Override
        protected void onPostExecute(Boolean boleano) {
            super.onPostExecute(boleano);
            if (boleano) {
                Toast.makeText(getApplicationContext(), "Progreso Terminado", Toast.LENGTH_LONG).show();
                start.setClickable(true);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_LONG).show();
        }

    }//final clase Asine (AsyncTask)

}//final de la clase




