package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jssc.SerialPort;
import jssc.SerialPortException;

public class WriteToPort{
    public static SerialPort serial;
    public static String str_outcome;

    public static void Event_W(TextField txt, SerialPort serial, String mode){
             str_outcome = txt.getText();

                try {
                    serial.openPort();

                    serial.setParams(SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

                    serial.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                            SerialPort.FLOWCONTROL_RTSCTS_OUT);
                    System.out.println("port " + serial.getPortName()+" is opened.");
                    serial.writeString(str_outcome);
                    System.out.println("port " +serial.getPortName()+" is opened.");
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                    Exeptions_list.except.remove(0, 1);
                    Exeptions_list.except.add(ex.toString().substring(15));
                }
            System.out.println(serial.getPortName()+" is ready to write");
            }
}