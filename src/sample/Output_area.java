package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class Output_area {
    public static TextArea readingWindow = new TextArea();
    public static void call_label(String str){
        readingWindow.setText(str);
    }

}
