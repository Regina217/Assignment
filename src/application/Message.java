package application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Message extends Alert {
    public Message(AlertType alertType) {
        super(alertType);
    }

    public void display(String title, String message){
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(300);
        stage.setMinHeight(100);
        VBox vBox = new VBox();
        Label label = new Label();
        label.setText(message);
        label.setFont(Font.font(null, FontWeight.BOLD, 20));
        vBox.getChildren().add(label);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

}
