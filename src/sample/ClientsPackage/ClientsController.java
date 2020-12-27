package sample.ClientsPackage;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    InsertClientDataDialogueController controller = fxmlLoader.getController();
                    controller.processResult();
                    ClientsDatabaseErrorChecker errorChecker = ClientsDatabaseErrorChecker.getInstance();
                    if(errorChecker.getErrorFound()) {
                        errorChecker.createAlertDialogue();
                        errorChecker.setErrorFound(false);
                        event.consume();
                    }
                    else
                    {
                        textField.setText(" " + textField.getText());
                        textField.setText(textField.getText().substring(1));
                        pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    }
                }
        );
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.showAndWait();
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
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    controller.updateClient(client);
                    ClientsDatabaseErrorChecker errorChecker = ClientsDatabaseErrorChecker.getInstance();
                    if(errorChecker.getErrorFound()) {
                        errorChecker.createAlertDialogue();
                        errorChecker.setErrorFound(false);
                        event.consume();
                    }
                }
        );
        dialog.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initButtons();
        initInfoIcon();
        initToggleGroup();
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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ştergere date client.");
                alert.setHeaderText("");
                alert.setContentText("În urma ştergerii clientului va dispărea şi istoricul despre " +
                        "contractele şi dispozitivele închiriate. Doriţi să continuaţi?");
                ButtonType yesButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Nu", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);
                alert.showAndWait().ifPresent(type -> {
                    if (type == yesButton) {
                        ClientsDatabaseHandler.getInstance().removeAllClientData(client.getIdProperty().getValue());
                        int currentPage = pagination.getCurrentPageIndex();
                        int pageCount = pagination.getPageCount();
                        int clientsOnPage = Clients.getClients().getSize();
                        textField.setText(" " + textField.getText());
                        textField.setText(textField.getText().substring(1));
                        if (pageCount - 1 == currentPage && clientsOnPage == 1) {
                            pagination.setCurrentPageIndex(currentPage - 1);
                        } else {
                            pagination.setCurrentPageIndex(currentPage);
                        }
                    }
                });
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

    private void addListenerToTextField() {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (idRButton.isSelected()) {
                doPaginationWhenIdSelected();
            }
            else if (phoneRButton.isSelected()) {
                doPaginationWhenPhoneSelected();
            }
            else {
                doPaginationWhenNameSelected();
            }
        });
    }

    private void doPaginationWhenIdSelected()
    {
        int pagesNumber = getProperPageNumber(ClientsDatabaseHandler.getInstance().getClientsNumberById(textField.getText().trim()));
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPageWhenIdSelected);
    }

    private Node createPageWhenIdSelected(int pageIndex) {
        Clients.getClients().addDataWhenIdSelected(pageIndex, rowsPerPage(), textField.getText().trim());
        ObservableList<ClientData> allClients = Clients.getClients().getAllClients();
        table.setItems(allClients);
        return new Pane(table);
    }

    private void doPaginationWhenPhoneSelected()
    {
        int pagesNumber = getProperPageNumber(ClientsDatabaseHandler.getInstance().getClientsNumberByPhone(textField.getText()));
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPageWhenPhoneSelected);
    }

    private Node createPageWhenPhoneSelected(int pageIndex) {
        Clients.getClients().addDataWhenPhoneSelected(pageIndex, rowsPerPage(), textField.getText());
        ObservableList<ClientData> allClients = Clients.getClients().getAllClients();
        table.setItems(allClients);
        return new Pane(table);
    }

    private void doPaginationWhenNameSelected()
    {
        int pagesNumber = getProperPageNumber(ClientsDatabaseHandler.getInstance().getClientsNumberByName(textField.getText().trim().toUpperCase()));
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPageWhenNameSelected);
    }

    private Node createPageWhenNameSelected(int pageIndex) {
        Clients.getClients().addDataWhenNameSelected(pageIndex, rowsPerPage(), textField.getText().trim().toUpperCase());
        ObservableList<ClientData> allClients = Clients.getClients().getAllClients();
        table.setItems(allClients);
        return new Pane(table);
    }

    private int rowsPerPage()
    {
        return 17;
    }

    private int getProperPageNumber(int rowsNum) {
        int pagesNumber = rowsNum / rowsPerPage() + 1;
        if (rowsNum % rowsPerPage() == 0) {
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

