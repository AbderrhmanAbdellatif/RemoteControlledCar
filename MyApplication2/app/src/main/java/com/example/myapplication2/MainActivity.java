package com.example.myapplication2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.burak_000.client.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button forward;
    Button back;
    Button sag;
    Button sol;
    Button dur;
    Button conn;
    EditText ipT;
    EditText portT;
   public static  EditText sansor;

    public static TextView text;

    private Socket s;
    private ServerSocket ss;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static PrintWriter pr;

    String message="";
    static String ip="";
    static int port;
    final Context context = this;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        forward = (Button) findViewById(R.id.ileri);
        back = (Button) findViewById(R.id.geri);
        sag = (Button) findViewById(R.id.sag);
        sol = (Button) findViewById(R.id.sol);
        dur = (Button) findViewById(R.id.dur);
        conn = (Button) findViewById(R.id.conn);
        text = (TextView)findViewById(R.id.textView);
        ipT = (EditText) findViewById(R.id.ipText);
        portT = (EditText) findViewById(R.id.portText);
        sansor =(EditText) findViewById(R.id.sansor);
        MyTask mt = new MyTask();
        mt.execute();

    }

    class MyTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params){

            conn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    message = "b";
                    ip=ipT.getText().toString();
                    port=Integer.parseInt(portT.getText().toString());
                    try{
                        Client.Start(ip, port,message);
                        text.setText("Connected to server");
                    }catch (ExceptionInInitializerError ex){
                        text.setText("Unable to connect to server");
                    }

                }
            });

            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    message = "İleri";
                    Client.Start(ip, port,message);
                   }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    message = "Geri";
                    Client.Start(ip, port,message);
                }
            });

            sag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    message = "Sag";
                    Client.Start(ip, port,message);
                }
            });

            sol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    message = "Sol";
                    Client.Start(ip, port,message);
                }
            });

            dur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    message = "Dur";
                    Client.Start(ip, port,message);
                }
            });





            return null;
        }

    }
}
