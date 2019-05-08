package p2pserver;
// in testing i got great accuracy

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


public class Test_Ultrasonic {

    public static void main(String[] args) throws Exception {
        final GpioController gpio = GpioFactory.getInstance(); //pinleri tanımlamasi 
        final GpioPinDigitalOutput out0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        final GpioPinDigitalOutput out2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        final GpioPinDigitalOutput out3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
        final GpioPinDigitalOutput out4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
//
        PiJavaUltrasonic sonic = new PiJavaUltrasonic(
                28,//ECO 11
                29,//TRIG 22
                1000,//REJECTION_START ; long
                23529411 //REJECTION_TIME ; long
        );
        System.out.println("Start");
        
        while (true) {
            System.out.println("distance " + sonic.getDistance() / 10 + "cm");
            Thread.sleep(1000); //1s
            
            if ((sonic.getDistance() / 10) < 30) {
                out4.low();
                out2.low();
                out3.low();
                out0.low();
            }
        }
    }

}
