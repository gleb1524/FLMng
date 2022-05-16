package ru.gb.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.gb.client.net.ClientService;
import ru.gb.client.net.NettyClient;
import ru.gb.dto.AuthRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private static Stage stage;
    @FXML
    public  Button authorization;
    @FXML
    public TextField login;
    @FXML
    public PasswordField password;
    @FXML
    public TextArea textArea;


    public  Client getClient() {
        return Client.getClient();
    }

    @FXML
    public void registration(ActionEvent actionEvent) throws IOException {
        Client.setRoot("reg");
    }

    @FXML
    public void authorization(ActionEvent actionEvent) throws IOException {
        AuthRequest authRequest = new AuthRequest(login.getText(), password.getText());
        ClientService.setLogin(login.getText());
        NettyClient.getChannel().writeAndFlush(authRequest);
    }

    @FXML
    public void clickToClose(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            Stage stage = (Stage) authorization.getScene().getWindow();
            stage.close();
            NettyClient.getEventLoopGroup().shutdownGracefully();
        });
    }

    public  void workPlace(boolean is) throws IOException {
        if(is){
            Alert alert = new Alert(Alert.AlertType.NONE, "Wrong login or password. Try again");
            alert.show();
        }else{
            Client.setRoot("auth");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientService.setClientController(this);
        Platform.runLater(() -> {
            stage = (Stage) authorization.getScene().getWindow();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    stage.close();
                   NettyClient.getEventLoopGroup().shutdownGracefully();
                }
            });
        });
    }

}
