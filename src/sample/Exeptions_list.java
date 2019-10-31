package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Exeptions_list{
    public static ObservableList<String> except = FXCollections.observableArrayList("");
    public void add_ex(String str){
        except.remove(0,1);
        except.add(str);
    }
}
