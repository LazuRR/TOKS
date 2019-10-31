package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;


public class Set_mode {
    public static RadioButton RTS_CTS = new RadioButton("RTS/CTS");
    public static RadioButton DTR_DSR = new RadioButton("DTR/DSR");
    public static RadioButton NO_SYNCHRONISATION = new RadioButton("No synchronization");
    public static HBox hBox = new HBox();
    public static void add(ToggleGroup toggleGroup,int x,int y){
        RTS_CTS.setToggleGroup(toggleGroup);
        DTR_DSR.setToggleGroup(toggleGroup);
        NO_SYNCHRONISATION.setToggleGroup(toggleGroup);
        hBox.getChildren().addAll(RTS_CTS, DTR_DSR,NO_SYNCHRONISATION);
        hBox.setLayoutY(y);
        hBox.setLayoutX(x);
        hBox.setSpacing(10);
    }
}
