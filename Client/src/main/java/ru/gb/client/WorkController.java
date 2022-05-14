package ru.gb.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.gb.client.net.ClientService;
import ru.gb.client.net.NettyClient;
import ru.gb.dto.FileInfo;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class WorkController implements Initializable {


    private final String clientPath = "client-dir";
    private final String serverPath = "server-dir";

    @FXML
    public Button move;
    @FXML
    public TableView<FileInfo> clientTable;
    @FXML
    public TextField clientDir;
    @FXML
    public TextField serverDir;
    @FXML
    public TableView<FileInfo> serverTable;


    @FXML
    public void copyFile(ActionEvent actionEvent) {

    }
    @FXML
    public void clickToClose(ActionEvent actionEvent) {
        Platform.runLater(() -> {
           Stage stage =(Stage) move.getScene().getWindow();
           stage.close();
            NettyClient.getChannel().close();
            NettyClient.getEventLoopGroup().shutdownGracefully();
        });
    }

    @FXML
    public void selectDiskAction(ActionEvent actionEvent) {
    }
    @FXML
    public void btnPathUpAction(ActionEvent actionEvent) {
    }
    @FXML
    public void deleteBtnAction(ActionEvent actionEvent) {
    }
    @FXML
    public void copyBtnAction(ActionEvent actionEvent) {
        System.out.println(ClientService.getAuth());
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientService.setWorkController(this);
        new WorkWindow(clientTable, Paths.get(clientPath), clientDir);
        new WorkWindow(serverTable, Paths.get(serverPath), serverDir);
    }

}
