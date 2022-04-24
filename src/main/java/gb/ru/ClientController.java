package gb.ru;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientController {


    public  Button authorization;

    @FXML
    public void registration(ActionEvent actionEvent) throws IOException {
        Client.setRoot("reg");

    }

    @FXML
    public void authorization(ActionEvent actionEvent) throws IOException {
        Client.setRoot("authorization");
    }


    public void clickToClose(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            Stage stage = (Stage) authorization.getScene().getWindow();
            stage.close();
        });
    }


}
