package sample.ClientsPackage;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Controller;
import sample.Styles;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    @FXML private Pane pane;
    @FXML private Button backButton, addButton, removeButton, modifyButton;
    @FXML private Label infoIcon;
    @FXML private RadioButton idRButton, nameRButton, phoneRButton;
    @FXML private TextField textField;
    @FXML private Pagination pagination;
    @FXML private TableView<ClientData> table;
    @FXML private TableColumn<ClientData, Integer> column1;
    @FXML private TableColumn<ClientData, String> column2, column3, column4, column5;
    private FilteredList<ClientData> filteredList;

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
    private void addNewClient()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_client_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        textField.setText("");
        dialog.setTitle("Adăugare client nou");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            InsertClientDataDialogueController controller = fxmlLoader.getController();
            controller.processResult();
            if(Clients.getClients().getSize()%rowsPerPage()==1)
            {
                pagination.setPageCount(pagination.getPageCount()+1);
            }
            pagination.setCurrentPageIndex(getProperPageNumber() - 1);
        }
    }

    @FXML
    private void modifyClient()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_client_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Actualizare date client");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        InsertClientDataDialogueController controller = fxmlLoader.getController();
        ClientData client = table.getSelectionModel().getSelectedItem();
        if(client==null)
        {
            createAlertDialogue();
            return;
        }
        controller.updateTextFields(client);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.updateClient(client);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initButtons();
        initInfoIcon();
        initToggleGroup();
        initPagination();
        addListenerToTextField();
        textField.setText(" ");
        textField.setText("");
    }

    private void initTable() {
        table.widthProperty().addListener((observable, oldValue, newValue) -> {
            TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable1, oldValue1, newValue1) -> header.setReordering(false));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        column1.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdProperty().getValue()).asObject());
        column2.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());
        column3.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
        column4.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumberProperty());
        column5.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(pane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLs/show_client_data_dialogue.fxml"));
                try
                {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                }catch (IOException e)
                {
                    e.printStackTrace();
                    return;
                }
                dialog.setTitle("Informaţii client");
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                ShowClientDataDialogueController controller = fxmlLoader.getController();
                ClientData client = table.getSelectionModel().getSelectedItem();
                controller.updateInfo(client);
                dialog.showAndWait();
            }
        });
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
            ClientData client = table.getSelectionModel().getSelectedItem();
            if(client!=null)
            {
                Clients.getClients().removeClient(client);
                if(Clients.getClients().getSize()%17==0)
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

    private void initInfoIcon()
    {
        Image img = new Image("resources/info.png");
        ImageView view = new ImageView(img);
        infoIcon.setGraphic(view);
        Tooltip tooltip = new Tooltip("Căutarea se poate face după:\n" +
                "- id sau\n" +
                "- nume_prenume sau\n" +
                "- telefon.\n");
        tooltip.setFont(new Font("System", 14));
        infoIcon.setTooltip(tooltip);
    }

    private void initToggleGroup()
    {
        ToggleGroup toggleGroup = new ToggleGroup();
        idRButton.setToggleGroup(toggleGroup);
        idRButton.setSelected(true);
        nameRButton.setToggleGroup(toggleGroup);
        phoneRButton.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> textField.clear());
    }

    private void initPagination() {
        int pagesNumber = getProperPageNumber();
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        ObservableList<ClientData> allClients = Clients.getClients().getAllClients();
        int fromIndex = pageIndex * rowsPerPage();
        int toIndex = Math.min(fromIndex + rowsPerPage(), allClients.size());
        table.setItems(FXCollections.observableList(allClients.subList(fromIndex, toIndex)));
        return new Pane(table);
    }

    void addListenerToTextField() {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList = new FilteredList(Clients.getClients().getAllClients());
            if (idRButton.isSelected()) {
                filteredList.setPredicate(p -> Integer.toString(p.getIdProperty().getValue()).contains(textField.getText().trim()));
            } else if (phoneRButton.isSelected()) {
                filteredList.setPredicate(p -> p.getPhoneNumberProperty().getValue().contains(textField.getText().trim()));
            } else if (nameRButton.isSelected()) {
                filteredList.setPredicate(p ->
                {
                    String lastName = p.getLastNameProperty().getValue();
                    String firstName = p.getFirstNameProperty().getValue();
                    return (lastName.toUpperCase() + "_" + firstName.toUpperCase()).contains(textField.getText().trim().toUpperCase());
                });
            }
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

    private int rowsPerPage()
    {
        return 17;
    }

    private int getProperPageNumber() {
        int size = Clients.getClients().getSize();
        int pagesNumber = size / rowsPerPage() + 1;
        if (size % rowsPerPage() == 0) {
            --pagesNumber;
        }
        return pagesNumber;
    }

    private void createAlertDialogue()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, "\n        Niciun client nu a fost selectat.", ButtonType.OK);
        alert.setHeaderText("");
        alert.setTitle("Error");
        alert.getDialogPane().setMaxWidth(350);
        alert.show();
    }
}

