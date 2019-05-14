package game;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Message implements java.io.Serializable {

    
   public static GpioController gpio = GpioFactory.getInstance();
 public   static GpioPinDigitalOutput out0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
   public static GpioPinDigitalOutput out2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
   public static GpioPinDigitalOutput out3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
    public static GpioPinDigitalOutput out4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
    //mesaj tipleri enum 
    public static enum Message_Type {
        None, Start, Ileri, Geri, Sag, Sol, Dur, RivalConnected,Selected, Bitis,
    }
    //mesajın tipi
    public Message_Type type;
    //mesajın içeriği obje tipinde ki istenilen tip içerik yüklenebilsin
    public Object content;

    public Message(Message_Type t) {
        this.type = t;
    }

}
