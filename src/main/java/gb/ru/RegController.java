package gb.ru;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class RegController {

    @FXML
    public Button back;

    @FXML
    public void clickToClose(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            Stage stage = (Stage) back.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    public void registration(ActionEvent actionEvent) {
        //Производим регистрацию

    }

    @FXML
    public void back(ActionEvent actionEvent) throws IOException {
        Client.setRoot("client");
    }
}
