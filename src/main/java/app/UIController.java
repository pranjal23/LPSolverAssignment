package app;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;

@Component
public class UIController {
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private VBox dataContainer;
    @FXML
    private TableView tableView;

    @FXML
    private void initialize() {
        // search panel
        searchButton.setText("Select Nominal Trade File");
        searchButton.setOnAction(event -> locateFile());
        searchButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill: #ffffff;");
    }

    private void initTable(File file) {
        tableView = new TableView<>();
        ArrayList<TableColumn> columnHeaders = new ArrayList<>();

        TableColumn date = new TableColumn("Date");
        columnHeaders.add(date);

        tableView.getColumns().addAll(columnHeaders);
        dataContainer.getChildren().add(tableView);
    }

    @FXML
    protected void locateFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        initTable(file);
    }
}
