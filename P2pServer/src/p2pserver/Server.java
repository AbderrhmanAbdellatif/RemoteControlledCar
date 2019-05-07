package p2pserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    //server soketi eklemeliyiz
    public static ServerSocket serverSocket;
    // Serverın dileyeceği port
    public static int port = 0;
  /*  GpioPinDigitalOutput out0 =null ;
    GpioPinDigitalOutput out2 =null ;
    GpioPinDigitalOutput out3 =null ;
    GpioPinDigitalOutput out4 = null ;*/

    public static void Start(int openport) {
        final GpioController gpio = GpioFactory.getInstance(); //pinleri tanımlama 
        final GpioPinDigitalOutput out0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        final GpioPinDigitalOutput out2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        final GpioPinDigitalOutput out3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
        final GpioPinDigitalOutput out4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
//
//    final GpioPinDigitalOutput out0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19, PinState.LOW);
//    final GpioPinDigitalOutput out2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, PinState.LOW);
//    final GpioPinDigitalOutput out3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW);
//    final GpioPinDigitalOutput out4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_20, PinState.LOW);

        try {
            Server.port = openport;
            // serversoket nesnesi
            Server.serverSocket = new ServerSocket(Server.port);
            Server.Display("Client Bekleniyor...");

            while (true) {

                Socket clientSocket = Server.serverSocket.accept();

                Server.Display("Client Geldi...");

                ObjectOutputStream sOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream sInput = new ObjectInputStream(clientSocket.getInputStream());

                sOutput.writeObject("Server:Hosgeldin");

                String message = sInput.readObject().toString();
                //Server.Display();

                if (message != null && message.equals("İleri")) {
                    Server.Display("ileri");

                    out0.high();
                    out4.high();

                    out2.low();
                    out3.low();

                } else if (message != null && message.equals("Geri")) {
                    Server.Display("Geri");
                    out0.low();
                    out4.low();
                    out2.high();
                    out3.high();
                } else if (message != null && message.equals("Sag")) {
                    Server.Display("Sag");
                    out0.low();
                    out2.low();
                    out3.low();
                    out4.high();
                    Thread.sleep(500);
                    out4.low();

                } else if (message != null && message.equals("Sol")) {
                    Server.Display("Sol");
                    out4.low();
                    out2.low();
                    out3.low();
                    out0.high();
                    Thread.sleep(500);
                    out0.low();
                } else if (message != null && message.equals("360")) {
                    out4.low();
                    out2.low();
                    out3.low();
                    out0.high();
                    Thread.sleep(1750);
                    out0.low();
                } else if (message != null && message.equals("Dur")) {
                    out4.low();
                    out2.low();
                    out3.low();
                    out0.low();

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Testserver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Testserver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            Logger.getLogger(Testserver.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void Display(String msg) {

        System.out.println(msg);

    }

}
