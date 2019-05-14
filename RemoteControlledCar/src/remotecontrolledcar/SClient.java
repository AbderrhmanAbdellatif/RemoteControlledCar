package remotecontrolledcar;

import game.Message;
import static game.Message.Message_Type.Selected;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class SClient {

    int id;
    public String name = "NoName";
    Socket soket;
    ObjectOutputStream sOutput;
    ObjectInputStream sInput;
    //clientten gelenleri dinleme threadi
    Listen listenThread;
    //cilent eşleştirme thredi
    PairingThread pairThread;
    //rakip client
    SClient rival;
    //eşleşme durumu
    public boolean paired = false;


    int ileriGeri;

    public SClient(Socket gelenSoket, int id) {
        this.soket = gelenSoket;
        this.id = id;
        try {
            this.sOutput = new ObjectOutputStream(this.soket.getOutputStream());
            this.sInput = new ObjectInputStream(this.soket.getInputStream());
            
         
            
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //thread nesneleri
        this.listenThread = new Listen(this);
        this.pairThread = new PairingThread(this);

    }

    //client mesaj gönderme
    public void Send(Message message) {
        try {
            this.sOutput.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //client dinleme threadi
    //her clientin ayrı bir dinleme thredi var
    class Listen extends Thread {

        SClient TheClient;

        //thread nesne alması için yapıcı metod
        Listen(SClient TheClient) {
            this.TheClient = TheClient;
        }

        public void run() {
            //client bağlı olduğu sürece dönsün
            while (TheClient.soket.isConnected()) {
                try {
                    //mesajı bekleyen kod satırı

                 String received = TheClient.sInput.readObject().toString();
                    //mesaj gelirse bu satıra geçer
                    //mesaj tipine göre işlemlere ayır

                    switch (received) {

                        case "Start":
                         //   TheClient.name = received.content.toString();
                            // isim verisini gönderdikten sonra eşleştirme işlemine başla
                            TheClient.pairThread.start();
                            break;
                        case "Ileri":
                            Server.Display("ileri");

                            Message.out0.high();
                            Message.out4.high();
                            Message.out2.low();
                            Message.out3.low();

                            ileriGeri = 0;

                            break;
                            
                        case "Geri":
                            Server.Display("Geri");
                            Message.out0.low();
                            Message.out4.low();
                            Message.out2.high();
                            Message.out3.high();
                            ileriGeri = 1;

                            break;
                            
                        case "Sag":
                            Server.Display("Sag");

                            Message.out4.low();
                            Message.out2.low();
                            Message.out3.high();
                            Message.out0.high();
                            Thread.sleep(500);
                            Message.out3.low();
                            Message.out0.low();
                            if (ileriGeri == 0) {

                                Message.out0.high();
                                Message.out4.high();
                                Message.out2.low();
                                Message.out3.low();

                            } else if (ileriGeri == 1) {
                                Message.out0.low();
                                Message.out4.low();
                                Message.out2.high();
                                Message.out3.high();

                            }
                            break;
                        case "Sol":
                            Server.Display("Sol");
                            Message.out0.low();
                            Message.out2.high();
                            Message.out3.low();
                            Message.out4.high();
                            Thread.sleep(500);
                            Message.out4.low();
                            Message.out2.low();

                            if (ileriGeri == 0) {

                                Message.out0.high();
                                Message.out4.high();
                                Message.out2.low();
                                Message.out3.low();

                            } else if (ileriGeri == 1) {
                                Message.out0.low();
                                Message.out4.low();
                                Message.out2.high();
                                Message.out3.high();

                            }

                            break;

                        case "Dur":
                            Server.Display("Dur");
                            Message.out4.low();
                            Message.out2.low();
                            Message.out3.low();
                            Message.out0.low();
                            ileriGeri = 2;
                            break;

                       

                    }

                } catch (IOException ex) {
                    Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
                    //client bağlantısı koparsa listeden sil
                    Server.Clients.remove(TheClient);

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
                    //client bağlantısı koparsa listeden sil
                    Server.Clients.remove(TheClient);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    //eşleştirme threadi
    //her clientin ayrı bir eşleştirme thredi var
    class PairingThread extends Thread {

        SClient TheClient;

        PairingThread(SClient TheClient) {
            this.TheClient = TheClient;
        }

        public void run() {
            //client bağlı ve eşleşmemiş olduğu durumda dön
            while (TheClient.soket.isConnected() && TheClient.paired == false) {
                try {
                    //lock mekanizması
                    //sadece bir client içeri grebilir
                    //diğerleri release olana kadar bekler
                    Server.pairTwo.acquire(1);

                    //client eğer eşleşmemişse gir
                    if (!TheClient.paired) {
                        SClient crival = null;
                        //eşleşme sağlanana kadar dön
                        while (crival == null && TheClient.soket.isConnected()) {
                            //liste içerisinde eş arıyor
                            for (SClient clnt : Server.Clients) {
                                if (TheClient != clnt && clnt.rival == null) {
                                    //eşleşme sağlandı ve gerekli işaretlemeler yapıldı
                                    crival = clnt;
                                    crival.paired = true;
                                    crival.rival = TheClient;
                                    TheClient.rival = crival;
                                    TheClient.paired = true;
                                    break;
                                }
                            }
                            //sürekli dönmesin 1 saniyede bir dönsün
                            //thredi uyutuyoruz
                            sleep(1000);
                        }
                        //eşleşme oldu
                        //her iki tarafada eşleşme mesajı gönder 
                        //oyunu başlat
//                        Message msg1 = new Message(Message.Message_Type.RivalConnected);
//                        msg1.content = TheClient.name;
//                        Server.Send(TheClient.rival, msg1);
//
//                        Message msg2 = new Message(Message.Message_Type.RivalConnected);
//                        msg2.content = TheClient.rival.name;
//                        Server.Send(TheClient, msg2);
                    }
                    //lock mekanizmasını servest bırak
                    //bırakılmazsa deadlock olur.
                    Server.pairTwo.release(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PairingThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
