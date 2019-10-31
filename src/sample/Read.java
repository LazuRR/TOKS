package sample;

import jssc.SerialPort;  /*Импорт классов библиотеки jssc*/
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;


public class Read  {  /*Класс чтения из порта*/
    private static SerialPort serialPort; /*Создаем объект типа SerialPort*/
    private static boolean flag = false;

    public static boolean open (String name,boolean f) {
        serialPort = new SerialPort(name);//создаем порт
        try {
            serialPort.openPort();//пытаемся открыть порт
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            serialPort.addEventListener(new IncomingMsg(), SerialPort.MASK_RXCHAR);
            flag = true;//говорим что открыли порт
            System.out.println("port "+name+" opened");
            Exeptions_list.except.remove(0,1);
            Exeptions_list.except.add("port "+name+" opened");
        }
        catch (SerialPortException e) {//обрабатываем возможные ошибки
            if (e.getExceptionType().equals("Port busy")) {
                Exeptions_list.except.remove(0, 1);
                Exeptions_list.except.add("Port "+name+" is busy");
            } else {
                Exeptions_list.except.remove(0, 1);
                Exeptions_list.except.add("Port "+name+" is opened");
            }
            return false;
        }
        return true;
    }

    public static SerialPort getSerialPort() {
        return serialPort;
    }

    public boolean send(String stringToSend) throws SerialPortException, InterruptedException {
        if (!flag) {
            return false;
        }
        IncomingMsg portReceiverTransmitter = new IncomingMsg();
        portReceiverTransmitter.SynchronisedSend(stringToSend);
        return true;
    }

    public static class IncomingMsg implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {

            boolean isRTS_CTS = CTSchoise();
            boolean isDTR_DSR = DSRchoise();
            boolean isNO_SYNC = NOsync();

            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String incomingSTR = new String();
                    String buf = new String();
                    System.out.println(incomingSTR);
                    if (isRTS_CTS) {

                        if (serialPort.isDSR() || (!serialPort.isCTS() && !serialPort.isDSR())) {
                            serialPort.setDTR(false);
                            System.out.println("Synchronisation doesn't match");
                            Exeptions_list.except.remove(0, 1);
                            Exeptions_list.except.add("Synchronisation doesn't match");
                            readToBuffer(buf);
                            buf = "";
                            return;
                        }

                        while (serialPort.isCTS()) {
                            serialPort.setDTR(false);
                            byte[] byteRead = serialPort.readBytes(1);
                            String data = new String(byteRead);
                            if(data.equals("$")||data.equals("^")){
                                return;
                            }
                            incomingSTR += data;
                            Output_area.call_label(incomingSTR);
                        }

                    }
                    if (isDTR_DSR) {

                        if (serialPort.isCTS() || (!serialPort.isCTS() && !serialPort.isDSR())) {
                            serialPort.setRTS(false);
                            System.out.println("Synchronisation doesn't match");
                            Exeptions_list.except.remove(0, 1);
                            Exeptions_list.except.add("Synchronisation doesn't match");
                            readToBuffer(buf);
                            buf = "";
                            return;
                        }

                        while (serialPort.isDSR()) {
                            serialPort.setRTS(false);
                            byte[] byteRead = serialPort.readBytes(1);
                            String data = new String(byteRead);
                            if(data.equals("$")||data.equals("^")){
                             return;
                            }
                            incomingSTR += data;
                            Output_area.call_label(incomingSTR);
                        }

                    }

                    if (isNO_SYNC) {

                        if (serialPort.isDSR() || serialPort.isCTS()) {
                            System.out.println("Synchronisation doesn't match");
                            Exeptions_list.except.remove(0, 1);
                            Exeptions_list.except.add("Synchronisation doesn't match");
                            readToBuffer(buf);
                            buf = "";
                            return;
                        }


                        serialPort.setRTS(false);
                        serialPort.setDTR(false);
                        String data = serialPort.readString();
                        if(data.substring(0,1).equals("^"))data=data.substring(1);
                        if(data.substring(0,2).equals("$^"))data=data.substring(2);
                        System.out.println(data);
                        incomingSTR += data;
                        Output_area.call_label(incomingSTR);
                    }
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }

        public void SynchronisedSend(String textToSend) throws SerialPortException, InterruptedException {

            boolean cts = CTSchoise();
            boolean dsr = DSRchoise();
            boolean nosync = NOsync();
            serialPort.setDTR(false);
            serialPort.setRTS(false);


            textToSend = "^"+" "+textToSend;
            byte[] byteArrayToSend = textToSend.getBytes();

            int stringLength = textToSend.length();
            int messageLength = textToSend.length();


            if (cts) {
                while (messageLength > 0) {
                    serialPort.setDTR(false);
                    serialPort.setRTS(true);
                    sendByte(byteArrayToSend[stringLength - messageLength]);
                    messageLength--;
                    Thread.sleep(10);
                }
            }

            if (dsr) {
                while (messageLength > 0) {
                    serialPort.setRTS(false);
                    serialPort.setDTR(true);
                    sendByte(byteArrayToSend[stringLength - messageLength]);
                    messageLength--;
                    Thread.sleep(10);
                }
            }

            if (nosync) {
                serialPort.setDTR(false);
                serialPort.setRTS(false);
                serialPort.writeString("$"+textToSend);
                Thread.sleep(10);

            } else if (!cts && !dsr && !nosync) {
                Exeptions_list.except.remove(0, 1);
                Exeptions_list.except.add("You have to choose one of the methods");
            }
        }

        public void sendByte(byte byteToWrite) {
            try {
                serialPort.writeByte(byteToWrite);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean NOsync() {
            return Set_mode.NO_SYNCHRONISATION.isSelected();
        }

        public boolean CTSchoise() {
            return Set_mode.RTS_CTS.isSelected();
        }

        public boolean DSRchoise() {
            return Set_mode.DTR_DSR.isSelected();
        }
    }

    public static void readToBuffer(String buf) throws SerialPortException {
        byte[] byteRead = serialPort.readBytes(1);
        String data = new String(byteRead);
        buf += data;
    }
    public static void closePort(){
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}