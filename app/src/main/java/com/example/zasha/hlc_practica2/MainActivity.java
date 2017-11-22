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
    private Asine asine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.comenzar);
        stop = (Button) findViewById(R.id.parar);
        barra = (ProgressBar) findViewById(R.id.barraProgreso);
        contador = (TextView) findViewById(R.id.contador);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                asine = (Asine) new Asine().execute(); //Ejecuta la tarea asincrona
                start.setClickable(false); //Desactivo el boton de iniciar
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (asine != null) //Si no esta parada, cancelamos la tarea asincrona
                    asine.cancel(true);
            }
        });

    }//final onCreate

    //@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class Asine extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barra.setMax(100); //Doy el valor maximo a la barra de progreso
            barra.setProgress(0); // y el valor actual
        }

        @Override
        protected Void doInBackground(Void... params) {

            for (int i = 0; i < 101; i++) {
                publishProgress(i); //Llamo a la funcion para comunicarme con el hilo principal
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /* Para que al parar el progreso no se quede el boton de inciar bloqueado
                 lo que le digo es que si el usuario cancela, me vuelva a activar el boton para
                 poder iniciar de nuevo el contador*/
                if (isCancelled()) {
                    start.setClickable(true);
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /*Recojo los datos del hilo en segundo plano y los muestro en la interfaz principal
            * de usuario*/
            barra.setProgress(values[0]); //Actualiza la barra de progreso
            contador.setText(values[0].toString() + "%"); //Actualiza el TextView del contador
        }

        @Override
        protected void onPostExecute(Void voider) {
            super.onPostExecute(voider);
            /*Si el proceso no se ha cancelado se muestra un mensaje y activa de nuevo el boton*/
            Toast.makeText(getApplicationContext(), "Progreso Terminado", Toast.LENGTH_LONG).show();
            start.setClickable(true); // Vuelvo a activar el boton de iniciar

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_LONG).show();
        }

    }//final clase Asine (AsyncTask)

}//final de la clase




