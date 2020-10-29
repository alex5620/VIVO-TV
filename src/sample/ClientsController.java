package sample;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    @FXML private Pane pane;
    @FXML private TableView tableView;
    @FXML private Button backButton, addButton, removeButton, modifyButton;
    @FXML private TableColumn column1, column2, column3, column4;
    @FXML private Label infoIcon;
    @FXML private RadioButton idRButton, nameRButton, phoneRButton;
    @FXML private TextField textField;
    private ObservableList<Map> allData;
    private ToggleGroup toggleGroup;

    @FXML
    private void switchToMainMenuScene()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 640));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableView();
        initButtons();
        initInfoIcon();
        initToggleGroup();
//        textField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if(idRButton.isSelected())
//            {
//                FilteredList filteredList = new FilteredList(allData);
//                filteredList.setPredicate( p -> {
//                    return true;
//                });
//                System.out.println("textfield changed from " + oldValue + " to " + newValue);
//            }
//        });
    }

    private void initTableView()
    {
        tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
            TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable1, oldValue1, newValue1) -> header.setReordering(false));
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        column1.setCellValueFactory(new MapValueFactory("1"));
        column2.setCellValueFactory(new MapValueFactory("2"));
        column3.setCellValueFactory(new MapValueFactory("3"));
        column4.setCellValueFactory(new MapValueFactory("4"));
        allData = FXCollections.observableArrayList();
        allData.addListener((ListChangeListener<Map>) pChange -> {
            if(allData.size()==18)
            {
                column1.setPrefWidth(110);
                column4.setPrefWidth(127);
            }
        });
        for (int i = 1; i < 15; i++) {
            Map<String, String> dataRow = new HashMap<>();
            String value1 = Integer.toString(i);
            String value2 = "B22222" + i;
            String value3 = "C22222" + i;
            String value4 = Integer.toString(i+1000000);
            dataRow.put("1", value1);
            dataRow.put("2", value2);
            dataRow.put("3", value3);
            dataRow.put("4", value4);
            allData.add(dataRow);
        }
        tableView.setItems(allData);
    }

    private void initButtons()
    {
        backButton.setOnMouseEntered(e -> backButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        backButton.setOnMouseExited(e -> backButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        addButton.setOnMouseEntered(e -> addButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        addButton.setOnMouseExited(e -> addButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        removeButton.setOnMouseEntered(e -> removeButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        removeButton.setOnMouseExited(e -> removeButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        modifyButton.setOnMouseEntered(e -> modifyButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        modifyButton.setOnMouseExited(e -> modifyButton.setStyle(Styles.IDLE_BUTTON_STYLE));
    }

    private void initInfoIcon()
    {
        Image img = new Image("resources/info.png");
        ImageView view = new ImageView(img);
        infoIcon.setGraphic(view);
        Tooltip tooltip = new Tooltip("Căutarea se poate face după:\n" +
                "- id sau\n" +
                "- nume prenume sau\n" +
                "- telefon.\n");
        tooltip.setFont(new Font("System", 14));
        infoIcon.setTooltip(tooltip);
    }

    private void initToggleGroup()
    {
        toggleGroup = new ToggleGroup();
        idRButton.setToggleGroup(toggleGroup);
        idRButton.setSelected(true);
        nameRButton.setToggleGroup(toggleGroup);
        phoneRButton.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> System.out.println(newVal + " was selected"));
    }
}
