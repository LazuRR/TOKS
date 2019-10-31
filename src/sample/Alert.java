package sample;

public class Alert {
    public static void AlertWin(){
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Incorrect choise");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Please choose two different COM ports to READ/WRITE");
        alert.showAndWait();
    }
    public static void NoPorts(){
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("No COM's detected");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port. Reload app after.");
        alert.showAndWait();
    }
}
