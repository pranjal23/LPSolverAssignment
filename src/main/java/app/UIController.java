package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.DayTradeOrder;
import entity.TraderOrders;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;
import solver.LPSolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private void processTrades(List<TraderOrders> traderOrders) {
        LPSolver solver = new LPSolver(traderOrders);
        solver.createDayTradeOrders();
        initTable(solver);
    }

    private void initTable(LPSolver solver) {
        tableView = new TableView<>();

        // Add headers
        ArrayList<TableColumn> columnHeaders = new ArrayList<>();
        TableColumn date = new TableColumn("Day");
        columnHeaders.add(date);

        for(TraderOrders t_o : solver.getTraderOrders()){
            String sw = String.format(" {SW:%s}", t_o.max_switching_window);
            TableColumn trader = new TableColumn( t_o.trader + sw);
            columnHeaders.add(trader);
        }
        tableView.getColumns().addAll(columnHeaders);

        // TODO Add rows after solving the problem
        for(Integer k : solver.getDayTradeOrders().keySet()){
            Map<Integer, DayTradeOrder> dayTradeOrders = solver.getDayTradeOrders().get(k);
        }

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
