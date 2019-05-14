package remotecontrolledcar;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.io.ObjectOutputStream;

public class Test_Ultrasonic {

    public static String sensor;

    public static void main(String[] args) throws Exception {
        final GpioController gpio = GpioFactory.getInstance(); //pinleri tanımlamasi 
        final GpioPinDigitalOutput out0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);//pin 00 tanimladim
        final GpioPinDigitalOutput out2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);//pin 02 tanimladim
        final GpioPinDigitalOutput out3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);//pin 03 tanimladim
        final GpioPinDigitalOutput out4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);//pin 04 tanimladim

        PiJavaUltrasonic sonic = new PiJavaUltrasonic(
                28,//ECO 11
                29,//TRIG 22
                1000,//REJECTION_START ; long
                23529411 //REJECTION_TIME ; long
        );
        System.out.println("Start");

        while (true) {
            System.out.println("distance " + sonic.getDistance() / 10 + "cm");

            if ((sonic.getDistance() / 10) < 40) {// 30 cmden daha az mesafede sensorun arabayi durdurmasi icin
                out4.low(); // pin 04'u durdurur
                out2.low(); // pin 02'u durdurur
                out3.low(); // pin 03'u durdurur 
                out0.low(); // pin 00'u durdurur

            }
        }
    }

}
