package ru.gb.client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.gb.dto.FileInfo;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WorkWindow implements Initializable {
    @FXML
    public ComboBox disksBox;
    @FXML
    public TextField pathField;
    @FXML
    public TableView<FileInfo> filesTable;
    private final String clientPath = "client-dir";
    private final String serverPath = "server-dir";



    public void updateList(Path path) {
        try {
            Path currentPath = path.normalize().toAbsolutePath();
            pathField.setText(currentPath.toString());
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "По какой-то неведомой причине не удалось обновить список файлов", ButtonType.OK);
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn("Type");
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileType().toString()));
        fileTypeColumn.setPrefWidth(50);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn("Filename");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(100);

        TableColumn<FileInfo, String> lastModifiedColumn = new TableColumn("Last Modified");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lastModifiedColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()
                .getLastModified().format(dtf)));
        lastModifiedColumn.setPrefWidth(120);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> new TableCell<FileInfo, Long>(){
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if(item==null|| empty){
                    setText(null);
                    setStyle("");
                }else{
                    String text = String.format("%,d bytes", item);
                    if(item == -1L){
                        text = "[DIR]";
                    }else if(item >=10_000 && item <= 1_000_000){
                        text = String.format("%d KB", item/1024);
                    }else if(item >=1_000_000){
                        text = String.format("%d MB", item/(1024*1024));
                    }
                    setText(text);
                }
            }
        });
        fileSizeColumn.setPrefWidth(50);

        disksBox.getItems().clear();
        for(Path p : FileSystems.getDefault().getRootDirectories()){
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

        updateList(Paths.get("."));

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2){
                    Path path = Paths.get(pathField.getText()).resolve(filesTable.getSelectionModel().getSelectedItem().getFileName());
                    if(Files.isDirectory(path)){
                        updateList(path);
                    }
                }
            }
        });

        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn,fileSizeColumn, lastModifiedColumn);
    }
    @FXML
    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }
    @FXML
    public void btnPathUpAction(ActionEvent actionEvent) {
        Path parentPath = Paths.get(pathField.getText()).getParent();
        if(parentPath != null){
            updateList(parentPath);
        }
        System.out.println(filesTable.getId());
    }

    public String selectedFilename(){
        if(!filesTable.isFocused()){
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem().getFileName();
    }

    public String getCurrentPath(){
        return pathField.getText();
    }
}
