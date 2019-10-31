package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.control.ChoiceBox;
import javafx.stage.WindowEvent;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.xml.soap.Text;


public class Main extends Application {
    String in_port;
    SerialPort serial;
    public Scene windowUI(){
//LIST OF COM'S
        String[] portNames = SerialPortList.getPortNames();
        Read comPort = new Read();
        if (portNames.length == 0) {
            Alert.NoPorts();
            System.out.println("There are no serial-ports :( You" +
                    " can use an emulator, such ad VSPE, to create a virtual serial port.");
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//ERRORS OUTPUT

        Label lbl_error = new Label("Debug:");
        ListView<String> exceptionsListView = new ListView<String>(Exeptions_list.except);
        lbl_error.setLayoutY(480);
        lbl_error.setLayoutX(30);
        exceptionsListView.setLayoutX(30);
        exceptionsListView.setLayoutY(510);
        exceptionsListView.setPrefSize(365,50);
//SET CHOISEBOX ELEMENTS

        ObservableList<String> values = FXCollections.observableArrayList();
        for (int i = 0; i < portNames.length; i++){
            values.add(portNames[i]);
            System.out.println(portNames[i]);
        }

        ChoiceBox<String> combo1 = new ChoiceBox<>();
        combo1.setPrefWidth(100);
        ConnectedComboBox<String> connectedComboBox = new ConnectedComboBox<>(values);
        connectedComboBox.addComboBox(combo1);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//INPUT INTERFACE

        TextArea input_line = new TextArea("");
        input_line.setPrefSize(365,60);
        input_line.setLayoutX(30);
        input_line.setLayoutY(75);



//HBOX SETTINGS

        Label lbl1 = new Label("COM name:");
        HBox hbox = new HBox(lbl1,combo1);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        hbox.setLayoutX(30);
        hbox.setLayoutY(10);

//OUTPUT INTERFACE


        Output_area.readingWindow.setLayoutX(30);
        Output_area.readingWindow.setLayoutY(170);
        Output_area.readingWindow.setPrefSize(365,230);
        Output_area.readingWindow.setEditable(false);



//CLS-RTS DTR-RTR ITEMS

        ToggleGroup radio = new ToggleGroup();
        Set_mode.add(radio,30,420);

//READ-WRITE FUNCTIONS

        combo1.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                System.out.println(newValue+" - new choise");
                if(Read.getSerialPort() != null)Read.closePort();
                Read.open(newValue,true);


            }
        });

        input_line.setOnKeyPressed(new EventHandler<KeyEvent>() {//отслеживаем нажатие на Enter в поле ввода
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        if (comPort.send(input_line.getText())) {
                         // input_line.setText("");
                        } else {
                            Exeptions_list.except.remove(0,1);
                            Exeptions_list.except.add("Port isn't opened");
                        }
                    } catch (SerialPortException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//SCENE SETTINGS

        Group root = new Group();
        root.getChildren().addAll(input_line,Set_mode.hBox,hbox,exceptionsListView,lbl_error,Output_area.readingWindow);
        Scene scene = new Scene(root,420,600);
        Stop[] stops = new Stop[] { new Stop(0,Color.DARKSEAGREEN ), new Stop(0.5, Color.CORNSILK),
                new Stop(1, Color.DARKKHAKI)};
        scene.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops));
        return scene;
    }

    public void start(Stage primaryStage) throws Exception{
        Scene scene = windowUI();
        primaryStage.setTitle("Client window");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(Read.getSerialPort() != null)Read.closePort();
                primaryStage.close();
            }
        });

    }
    public static void main(String[] args) {
        launch(args);
    }
}
