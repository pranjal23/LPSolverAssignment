package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.TraderOrders;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UIController {
    @FXML
    private TextField selectField;
    @FXML
    private Button selectButton;
    @FXML
    private VBox dataContainer;
    @FXML
    private TableView tableView;

    @FXML
    private void initialize() {
        // search panel
        selectField.setEditable(false);
        selectButton.setText("Select");
        selectButton.setOnAction(event -> locateFile());
        selectButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill: #ffffff;");
    }

    private void initTable(List<TraderOrders> traderOrders) {
        tableView = new TableView<>();
        ArrayList<TableColumn> columnHeaders = new ArrayList<>();

        TableColumn date = new TableColumn("Date");
        columnHeaders.add(date);

        tableView.getColumns().addAll(columnHeaders);
        dataContainer.getChildren().add(tableView);
    }

    @FXML
    protected void locateFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        selectField.setText(file.getAbsolutePath());

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            String myJsonString = new String (bytes);
            ObjectMapper om = new ObjectMapper();
            TraderOrders[] root = om.readValue(myJsonString, TraderOrders[].class);
            List<TraderOrders> traderOrders = Arrays.asList(root);
            if(traderOrders != null && !traderOrders.isEmpty()){
                Stage thisStage = (Stage) dataContainer.getScene().getWindow();
                thisStage.setMaximized(true);
                initTable(traderOrders);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(e.getMessage());
            alert.setHeaderText("Unable to read json trader file!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    alert.close();
                }
            });
        }
    }
}
