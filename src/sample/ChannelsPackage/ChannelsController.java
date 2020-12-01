package sample.ChannelsPackage;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Controller;
import sample.Styles;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChannelsController implements Initializable {
    @FXML private Pane pane;
    @FXML private Button backButton, addButton, removeButton, modifyButton;
    @FXML private TextField textField;
    @FXML private Pagination pagination;
    @FXML private TableView<ChannelData> table;
    @FXML private TableColumn<ChannelData, Integer> idColumn, channelColumn;
    @FXML private TableColumn<ChannelData, String> nameColumn, startDateColumn, endDateColumn, typeColumn;
    @FXML private TableColumn<ChannelData, Double> frequencyColumn;
    private FilteredList<ChannelData> filteredList;


    @FXML
    private void switchToMainMenuScene()
    {
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("main_menu.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 640));
    }

    @FXML
    private void addNewChannel()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_channel_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Adăugare program nou");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            InsertChannelDataDialogueController controller = fxmlLoader.getController();
            controller.processResult();
            if(Channels.getChannels().getSize()%rowsPerPage()==1)
            {
                pagination.setPageCount(pagination.getPageCount()+1);
            }
            pagination.setCurrentPageIndex(getProperPageNumber() - 1);
        }
    }

    @FXML
    private void modifyChannel()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_channel_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Actualizare date program");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        InsertChannelDataDialogueController controller = fxmlLoader.getController();
        ChannelData channelData = table.getSelectionModel().getSelectedItem();
        if(channelData==null)
        {
            createAlertDialogue();
            return;
        }
        controller.updateTextFields(channelData);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.updateChannel(channelData);
            table.refresh();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initButtons();
        initPagination();
        addListenerToTextField();
    }

    private void initTable() {
        table.widthProperty().addListener((observable, oldValue, newValue) -> {
            TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable1, oldValue1, newValue1) -> header.setReordering(false));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdProperty().getValue()).asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().getStartDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().getEndDateProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        frequencyColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFrequencyProperty().getValue()).asObject());
        channelColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getChannelProperty().getValue()).asObject());
    }

    private void initButtons()
    {
        backButton.setOnMouseEntered(e -> backButton.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        backButton.setOnMouseExited(e -> backButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        addButton.setOnMouseEntered(e -> addButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        addButton.setOnMouseExited(e -> addButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        removeButton.setOnMouseEntered(e -> removeButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        removeButton.setOnMouseExited(e -> removeButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        removeButton.setOnAction(event ->
        {
            ChannelData channelData = table.getSelectionModel().getSelectedItem();
            if(channelData!=null)
            {
                Channels.getChannels().removeChannel(channelData);
                if(Channels.getChannels().getSize()%rowsPerPage()==0)
                {
                    pagination.setPageCount(pagination.getPageCount()-1);
                    pagination.setCurrentPageIndex(pagination.getPageCount()-1);
                }
            }
            else
            {
                createAlertDialogue();
            }
        });
        modifyButton.setOnMouseEntered(e -> modifyButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        modifyButton.setOnMouseExited(e -> modifyButton.setStyle(Styles.IDLE_BUTTON_STYLE));
    }

    private void initPagination() {
        int pagesNumber = getProperPageNumber();
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        ObservableList<ChannelData> allChannels = Channels.getChannels().getAllChannels();
        int fromIndex = pageIndex * rowsPerPage();
        int toIndex = Math.min(fromIndex + rowsPerPage(), allChannels.size());
        table.setItems(FXCollections.observableList(allChannels.subList(fromIndex, toIndex)));
        return new Pane(table);
    }

    private int rowsPerPage()
    {
        return 18;
    }

    private int getProperPageNumber() {
        int size = Channels.getChannels().getSize();
        int pagesNumber = size / rowsPerPage() + 1;
        if (size % rowsPerPage() == 0) {
            --pagesNumber;
        }
        return pagesNumber;
    }

    private void addListenerToTextField() {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList = new FilteredList(Channels.getChannels().getAllChannels());
            filteredList.setPredicate(p -> p.getNameProperty().getValue().toUpperCase().
                    contains(textField.getText().trim().toUpperCase()));
            doPagination();
        });
    }

    private void doPagination() {
        int pagesNumber = filteredList.size() % rowsPerPage() == 0 ? filteredList.size()/rowsPerPage() : filteredList.size()/rowsPerPage() + 1;
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPage2);
    }

    private Node createPage2(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage();
        int toIndex = Math.min(fromIndex + rowsPerPage(), filteredList.size());
        table.setItems(FXCollections.observableList(filteredList.subList(fromIndex, toIndex)));
        return new Pane(table);
    }

    private void createAlertDialogue()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, "\n        Niciun program nu a fost selectat.", ButtonType.OK);
        alert.setHeaderText("");
        alert.setTitle("Error");
        alert.getDialogPane().setMaxWidth(350);
        alert.show();
    }
}
