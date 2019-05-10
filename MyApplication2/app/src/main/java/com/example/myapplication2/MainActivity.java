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
import java.io.IOException;
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
    Button kapat;

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
    public Thread tmr_slider;



}
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
        kapat =(Button) findViewById(R.id.kapat);
        MyTask mt = new MyTask();
        mt.execute();
    }


        MainActivity() {



            tmr_slider = new Thread(() -> {
                //soket bağlıysa dönsün
                while (Client.socket.isConnected()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Nothing here
                    }
                }


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
                        text.setText("Connected to server");
                    }catch (ExceptionInInitializerError ex){
                        text.setText("Unable to connect to server");
                    }

                }
            });

            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    message = "Ileri";
                    //Client.Start(ip, port,message);

                    Message msg = new Message(Message.Message_Type.Text);
                    msg.content = message;
                    Client.Send(msg);
                    /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    //message = messageBox.getText().toString();
                    Client.Start(ip, port,message);
                    alertDialogBuilder.setTitle(message);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();*/

                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    //text.setText();
                    message = "Geri";
                    Message msg = new Message(Message.Message_Type.Text);
                    msg.content = message;
                    Client.Send(msg);
                }
            });

            sag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    //text.setText("Geri gidiyor");
                    message = "Sag";
                   /// Client.Start(ip, port,message);
                    Message msg = new Message(Message.Message_Type.Text);
                    msg.content = message;
                    Client.Send(msg);
                }
            });

            sol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    //text.setText("Geri gidiyor");
                    message = "Sol";
                   // Client.Start(ip, port,message);
                    Message msg = new Message(Message.Message_Type.Text);
                    msg.content = message;
                    Client.Send(msg);
                }
            });

            dur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    //text.setText("Geri gidiyor");
                    message = "Dur";
                  //  Client.Start(ip, port,message);
                    Message msg = new Message(Message.Message_Type.Text);
                    msg.content = message;
                    Client.Send(msg);
                }
            });




            return null;
        }

    }
}
