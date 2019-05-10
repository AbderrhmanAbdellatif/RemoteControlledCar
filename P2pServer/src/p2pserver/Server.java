package p2pserver;

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

    //server soketinin eklenmesi
    // Serverin dileyeceği port
    public static int port = 0;
    public static int ileriGeri = 2;

    public static ServerSocket serverSocket;
    public static Socket clientSocket;

    public static void Start(int serverPort) {

        //Kullanilacak 4 pinin tanimlanmasi
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput out0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        final GpioPinDigitalOutput out2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        final GpioPinDigitalOutput out3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
        final GpioPinDigitalOutput out4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);

        try {
            Server.port = serverPort;

            Server.serverSocket = new ServerSocket(Server.port); 
            
            Server.Display("Client Bekleniyor...");

            while (true) {

                clientSocket = Server.serverSocket.accept();

                Server.Display("Client Geldi...");

                ObjectOutputStream sOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream sInput = new ObjectInputStream(clientSocket.getInputStream());

                sOutput.writeObject("Server:Merhabalar");

                String message = sInput.readObject().toString(); 
                //Server.Display();
                if (message != null && message.equals("İleri")) {
                    Server.Display("ileri");

                    out0.high();
                    out4.high();
                    out2.low();
                    out3.low();

                    ileriGeri = 0;

                } else if (message != null && message.equals("Geri")) {
                    Server.Display("Geri");
                    out0.low();
                    out4.low();
                    out2.high();
                    out3.high();
                    ileriGeri = 1;
                } else if (message != null && message.equals("Sag")) {
                    Server.Display("Sag");

                    out4.low();
                    out2.low();
                    out3.high();
                    out0.high();
                    Thread.sleep(500);
                    out3.low();
                    out0.low();
                    if (ileriGeri == 0) {

                        out0.high();
                        out4.high();
                        out2.low();
                        out3.low();

                    } else if (ileriGeri == 1) {
                        out0.low();
                        out4.low();
                        out2.high();
                        out3.high();

                    }
                } else if (message != null && message.equals("Sol")) {
                    Server.Display("Sol");
                    out0.low();
                    out2.high();
                    out3.low();
                    out4.high();
                    Thread.sleep(500);
                    out4.low();
                    out2.low();

                    if (ileriGeri == 0) {

                        out0.high();
                        out4.high();
                        out2.low();
                        out3.low();

                    } else if (ileriGeri == 1) {
                        out0.low();
                        out4.low();
                        out2.high();
                        out3.high();

                    }

                } else if (message != null && message.equals("Dur")) {
                    out4.low();
                    out2.low();
                    out3.low();
                    out0.low();
                    ileriGeri = 2;

                } else if (message != null && message.equals("kapat")) {
                    System.exit(0);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void Display(String msg) {
        System.out.println(msg);
    }

}
