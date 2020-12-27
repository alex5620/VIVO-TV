package sample.ContractsPackage;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sample.FXMLFileLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ContractsController2 implements Initializable {
    @FXML private Pane pane;
    @FXML private Button backButton, addPackage, addDevice, removeDevice;
    @FXML private TextField textField;
    @FXML private Pagination pagination;
    @FXML private TableView<ContractData> table;
    @FXML private TableColumn<ContractData, String> column2, column3, column4;
    @FXML private TableColumn<ContractData, Integer> column1;
    @FXML private ImageView nextButton;

    @FXML
    private void switchToMainMenuScene()
    {
        FXMLFileLoader.loadFXML(pane, "main_menu.fxml");
    }

    @FXML
    private void addPackage()
    {
        ContractData contract = table.getSelectionModel().getSelectedItem();
        if(contract==null)
        {
            createAlertDialogue("Niciun contract nu a fost selectat.");
            return;
        }
        else if(contract.getPackagesNumber()==2)
        {
            createAlertDialogue("S-a atins numarul maxim de pachete.");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/add_package_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        textField.setText("");
        dialog.setTitle("Adăugare pachet nou");
        ChangePackageDialogueController controller = fxmlLoader.getController();
        controller.setValidPackages(contract);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    controller.processData(contract);
                    ContractsDatabaseErrorChecker errorChecker = ContractsDatabaseErrorChecker.getInstance();
                    if(errorChecker.getErrorFound()) {
                        errorChecker.createAlertDialogue();
                        errorChecker.setErrorFound(false);
                        event.consume();
                    }
                    else
                    {
                        int currentPage = pagination.getCurrentPageIndex();
                        textField.setText(" " + textField.getText());
                        textField.setText(textField.getText().substring(1));
                        pagination.setCurrentPageIndex(currentPage);
                    }
                }
        );
        dialog.showAndWait();
    }

    @FXML
    private void insertDeviceData()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_device_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        textField.setText("");
        dialog.setTitle("Inserare date dispozitiv");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        InsertDeviceDataDialogueController controller = fxmlLoader.getController();
        ContractData contract = table.getSelectionModel().getSelectedItem();
        if(contract==null)
        {
            createAlertDialogue("Niciun contract nu a fost selectat.");
            return;
        }
        if(contract.getDevicesNumber() == 7)
        {
            createAlertDialogue("S-a atins numarul maxim de dispozitive inchiriate.");
            return;
        }
        controller.setValidDevices(contract);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    controller.processResult(contract);
                    ContractsDatabaseErrorChecker errorChecker = ContractsDatabaseErrorChecker.getInstance();
                    if(errorChecker.getErrorFound()) {
                        errorChecker.createAlertDialogue();
                        errorChecker.setErrorFound(false);
                        event.consume();
                    }
                    else
                    {
                        int currentPage = pagination.getCurrentPageIndex();
                        textField.setText(" " + textField.getText());
                        textField.setText(textField.getText().substring(1));
                        pagination.setCurrentPageIndex(currentPage);
                    }
                }
        );
        dialog.showAndWait();
    }

    @FXML
    public void removeDevice()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/remove_device_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Ştergere dispozitive");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        RemoveDeviceDialogueController controller = fxmlLoader.getController();
        ContractData contract = table.getSelectionModel().getSelectedItem();
        if(contract==null)
        {
            createAlertDialogue("Niciun contract nu a fost selectat.");
            return;
        }
        int currentPage = pagination.getCurrentPageIndex();
        controller.addDevices(contract);
        dialog.showAndWait();
        textField.setText(" " + textField.getText());
        textField.setText(textField.getText().substring(1));
        pagination.setCurrentPageIndex(currentPage);
    }

    @FXML
    public void removePackage()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/remove_package_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Ştergere pachete");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        RemovePackageDialogueController controller = fxmlLoader.getController();
        ContractData contract = table.getSelectionModel().getSelectedItem();
        if(contract==null)
        {
            createAlertDialogue("Niciun contract nu a fost selectat.");
            return;
        }
        int currentPage = pagination.getCurrentPageIndex();
        controller.addPackages(contract);
        dialog.showAndWait();
        textField.setText(" " + textField.getText());
        textField.setText(textField.getText().substring(1));
        pagination.setCurrentPageIndex(currentPage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initButtons();
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
        column1.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getContractNumberProperty().getValue()).asObject());
        column2.setCellValueFactory(cellData ->
        {
            if(cellData.getValue().getPackages().size() != 0)
            {
                return cellData.getValue().getPackages().get(0).getPackageType();
            }
            return new SimpleStringProperty("");
        });
        column3.setCellValueFactory(cellData ->
        {
            if(cellData.getValue().getPackages().size() !=0 ) {
                return cellData.getValue().getPackages().get(0).getStartDate();
            }
            return new SimpleStringProperty("");
        });
        column4.setCellValueFactory(cellData -> cellData.getValue().getDevicesBrief());
        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(pane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLs/show_packages_devices_dialogue.fxml"));
                try
                {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                }catch (IOException e)
                {
                    e.printStackTrace();
                    return;
                }
                dialog.setTitle("Pachete şi dispozitive");
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                ShowPackagesAndDevicesDialogueController controller = fxmlLoader.getController();
                ContractData contractData = table.getSelectionModel().getSelectedItem();
                controller.showInfo(contractData);
                dialog.showAndWait();
            }
        });
    }

    private void initButtons()
    {
        backButton.setOnMouseEntered(e -> backButton.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        backButton.setOnMouseExited(e -> backButton.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
        addDevice.setOnMouseEntered(e -> addDevice.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        addDevice.setOnMouseExited(e -> addDevice.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
        removeDevice.setOnMouseEntered(e -> removeDevice.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        removeDevice.setOnMouseExited(e -> removeDevice.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
        addPackage.setOnMouseEntered(e -> addPackage.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        addPackage.setOnMouseExited(e -> addPackage.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
        nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FXMLFileLoader.loadFXML(pane, "ContractsPackage/FXMLs/contracts_menu.fxml");
            event.consume();
        });
        nextButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            nextButton.setImage(new Image("resources/next_coloured.png"));
            event.consume();
        });
        nextButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            nextButton.setImage(new Image("resources/next.png"));
            event.consume();
        });
    }

    void addListenerToTextField()
    {
        textField.textProperty().addListener((observable, oldValue, newValue) -> doPagination());
    }

    private void doPagination() {
        int pagesNumber = getProperPageNumber(ContractsDatabaseHandler.getInstance().getContractsNumber(textField.getText().trim()));
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        Contracts.getContracts().addFullData(pageIndex, rowsPerPage(), textField.getText().trim());
        ObservableList<ContractData> allContracts = Contracts.getContracts().getAllContracts();
        table.setItems(allContracts);
        return new Pane(table);
    }

    private int rowsPerPage()
    {
        return 16;
    }

    private int getProperPageNumber(int rowsNum) {
        int pagesNumber = rowsNum / rowsPerPage() + 1;
        if (rowsNum % rowsPerPage() == 0) {
            --pagesNumber;
        }
        return pagesNumber;
    }

    private void createAlertDialogue(String text)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, "\n        "+text, ButtonType.OK);
        alert.setHeaderText("");
        alert.setTitle("Error");
        alert.getDialogPane().setMaxWidth(350);
        alert.show();
    }
}



