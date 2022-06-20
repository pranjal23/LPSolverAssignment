/*
Copyright (c) 2022 Pranjal Swarup
All rights reserved
*/

package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import solver.SolverTask;
import solver.entity.TraderOrders;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;
import solver.LPSolvePreprocessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

@Component
public class UIController {
    @FXML
    private TextField selectField;
    @FXML
    private Button selectButton;
    @FXML
    private VBox dataContainer;
    @FXML
    private TableView<ObservableList> tableView;
    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        // search panel
        selectField.setEditable(false);
        selectButton.setText("Select");
        selectButton.setOnAction(event -> locateFile());
        selectButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill: #ffffff;");
    }

    private void processTrades(List<TraderOrders> traderOrders) {
        // Create a preprocessor process the file
        LPSolvePreprocessor preprocessor = new LPSolvePreprocessor(traderOrders);
        SolverTask solverTask = new SolverTask(preprocessor,this);
        solverTask.exceptionProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue != null) {
                Exception ex = (Exception) newValue;
                ex.printStackTrace();
                statusLabel.setText(ex.getMessage());
            }
        });

        // Start the task on a seperate thread
        Thread th = new Thread(solverTask);
        th.setDaemon(true);
        th.start();
    }

    public void initTable(LPSolvePreprocessor preprocessor,  ObservableList<ObservableList> period_data) {
        tableView = new TableView<>();
        // Add headers
        ArrayList<String> columnHeaders = new ArrayList<>();
        columnHeaders.add("Day");

        for(TraderOrders t_o : preprocessor.getTraderOrders()){
            String sw = String.format(" {Switching Window: %s}", t_o.max_switching_window);
            columnHeaders.add(t_o.trader + sw);
        }

        tableView = new TableView<>();
        for (int i = 0; i < columnHeaders.size(); i++) {
            TableColumn tc = new TableColumn(columnHeaders.get(i));
            final int colNo = i;
            tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<ObservableList<String>, String> p) {
                    return new SimpleStringProperty((p.getValue().get(colNo)));
                }
            });
            tc.setPrefWidth(90);
            tableView.getColumns().add(tc);
        }

        // Add results of solving the LP problem
        statusLabel.setText("Results");
        tableView.setItems(period_data);
        dataContainer.getChildren().add(tableView);
    }

    @FXML
    protected void locateFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        if(file!=null){
            selectField.setText(file.getAbsolutePath());
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                String myJsonString = new String (bytes);
                ObjectMapper om = new ObjectMapper();
                TraderOrders[] root = om.readValue(myJsonString, TraderOrders[].class);
                List<TraderOrders> traderOrders = Arrays.asList(root);
                if(!traderOrders.isEmpty()){
                    Stage thisStage = (Stage) dataContainer.getScene().getWindow();
                    thisStage.setMaximized(true);
                    processTrades(traderOrders);
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
}
