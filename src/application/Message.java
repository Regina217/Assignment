package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Message {
    public Message(String title, String message){
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(300);
        stage.setMinHeight(100);
        VBox vBox = new VBox();
        Label label = new Label();
        label.setText(message);
        vBox.getChildren().add(label);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
