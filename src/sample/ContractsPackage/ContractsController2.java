package sample.ContractsPackage;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    @FXML private Button backButton, changePackage, addDevice, removeDevice;
    @FXML private TextField textField;
    @FXML private Pagination pagination;
    @FXML private TableView<ContractData> table;
    @FXML private TableColumn<ContractData, String> column2, column3, column4;
    @FXML private TableColumn<ContractData, Integer> column1;
    @FXML private ImageView nextButton;
    private FilteredList<ContractData> filteredList;

    @FXML
    private void switchToMainMenuScene()
    {
        FXMLFileLoader.loadFXML(pane, "main_menu.fxml");
    }

    @FXML
    private void changePackage()
    {
        ContractData contract = table.getSelectionModel().getSelectedItem();
        if(contract==null)
        {
            createAlertDialogue("Niciun contract nu a fost selectat.");
            return;
        }
        else if(contract.getPackagesNumber()==2)
        {
            createAlertDialogue("Pachetul nu mai poate fi schimbat.");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/change_package_dialogue.fxml"));
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
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        ChangePackageDialogueController controller = fxmlLoader.getController();
        controller.setValidPackages(contract);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.processData(contract);
        }
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
        if(contract.getDevicesNumber()==6)
        {
            createAlertDialogue("S-a atins numarul maxim de dispozitive inchiriate.");
            return;
        }
        controller.setValidDevices(contract);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.processResult(contract);
            table.refresh();
        }
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
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        RemoveDeviceDialogueController controller = fxmlLoader.getController();
        ContractData contract = table.getSelectionModel().getSelectedItem();
        if(contract==null)
        {
            createAlertDialogue("Niciun contract nu a fost selectat.");
            return;
        }
        controller.addDevices(contract);
        dialog.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initButtons();
        initPagination();
        addListenerToTextField();
        nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FXMLFileLoader.loadFXML(pane, "ContractsPackage/FXMLs/contracts_menu.fxml");
            event.consume();
        });
    }

    private void initTable() {
        table.widthProperty().addListener((observable, oldValue, newValue) -> {
            TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable1, oldValue1, newValue1) -> header.setReordering(false));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        column1.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getContractNumberProperty().getValue()).asObject());
        column2.setCellValueFactory(cellData -> cellData.getValue().getPackages().get(0).getPackageType());
        column3.setCellValueFactory(cellData -> cellData.getValue().getPackages().get(0).getStartDate());
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
        changePackage.setOnMouseEntered(e -> changePackage.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        changePackage.setOnMouseExited(e -> changePackage.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
    }

    private void initPagination() {
        int pagesNumber = getProperPageNumber();
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        ObservableList<ContractData> allContracts = Contracts.getContracts().getAllContracts();
        int fromIndex = pageIndex * rowsPerPage();
        int toIndex = Math.min(fromIndex + rowsPerPage(), allContracts.size());
        table.setItems(FXCollections.observableList(allContracts.subList(fromIndex, toIndex)));
        return new Pane(table);
    }

    void addListenerToTextField()
    {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList = new FilteredList(Contracts.getContracts().getAllContracts());
                filteredList.setPredicate(p -> Integer.toString(p.getContractNumberProperty().getValue()).contains(textField.getText().trim()));
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
        return 16;
    }

    private int getProperPageNumber() {
        int size = Contracts.getContracts().getSize();
        int pagesNumber = size / rowsPerPage() + 1;
        if (size % rowsPerPage() == 0) {
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



