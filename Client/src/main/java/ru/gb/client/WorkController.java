package ru.gb.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import ru.gb.client.net.ClientService;
import ru.gb.client.net.NettyClient;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class WorkController implements Initializable {


    public VBox clientPanel;
    public VBox serverPanel;



    @FXML
    public void clickToClose(ActionEvent actionEvent) {
        Platform.exit();
        NettyClient.getEventLoopGroup().shutdownGracefully();
    }


    @FXML
    public void deleteBtnAction(ActionEvent actionEvent) {
    }
    @FXML
    public void copyBtnAction(ActionEvent actionEvent) {
        WorkWindow clientWorkWindow = (WorkWindow) clientPanel.getProperties().get("ctrl");
        WorkWindow serverWorkWindow = (WorkWindow) serverPanel.getProperties().get("ctrl");

        if(clientWorkWindow.selectedFilename() == null && serverWorkWindow.selectedFilename() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Missing files", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        WorkWindow srcWorkWindow = null, dstWorkWindow = null;
        if(clientWorkWindow.selectedFilename() != null){
            srcWorkWindow = clientWorkWindow;
            dstWorkWindow = serverWorkWindow;
        }
        if(serverWorkWindow.selectedFilename() != null){
            srcWorkWindow = serverWorkWindow;
            dstWorkWindow = clientWorkWindow;
        }

        Path srcPath = Paths.get(srcWorkWindow.getCurrentPath(), srcWorkWindow.selectedFilename());
        Path dstPath = Paths.get(dstWorkWindow.getCurrentPath()).resolve(srcPath.getFileName().toString());
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientService.setWorkController(this);

    }
    @FXML
    public void openBtnAction(ActionEvent actionEvent) {
    }
}
