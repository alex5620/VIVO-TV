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

public class ContractsController implements Initializable {
    @FXML private Pane pane;
    @FXML private Button backButton, addButton, removeButton, modifyButton;
    @FXML private TextField textField;
    @FXML private Pagination pagination;
    @FXML private TableView<ContractData> table;
    @FXML private TableColumn<ContractData, String> column2, column4, column5, column7;
    @FXML private TableColumn<ContractData, Integer> column1, column3, column6;
    @FXML private ImageView nextButton;
    private FilteredList<ContractData> filteredList;

    @FXML
    private void switchToMainMenuScene()
    {
        FXMLFileLoader.loadFXML(pane, "main_menu.fxml");
    }

    @FXML
    private void addNewContract()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_contract_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        textField.setText("");
        dialog.setTitle("Adăugare contract nou");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            InsertContractDataDialogueController controller = fxmlLoader.getController();
            controller.processResult();
            if(Contracts.getContracts().getSize()%rowsPerPage()==1)
            {
                pagination.setPageCount(pagination.getPageCount()+1);
            }
            pagination.setCurrentPageIndex(getProperPageNumber() - 1);
        }
    }

    @FXML
    private void modifyContract()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_contract_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Actualizare date contract");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        InsertContractDataDialogueController controller = fxmlLoader.getController();
        ContractData contract = table.getSelectionModel().getSelectedItem();
        if(contract==null)
        {
            createAlertDialogue();
            return;
        }
        controller.updateTextFields(contract);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.updateContract(contract);
            table.refresh();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initButtons();
        initPagination();
        addListenerToTextField();
        nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FXMLFileLoader.loadFXML(pane, "ContractsPackage/FXMLs/contracts_menu2.fxml");
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
        column2.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        column3.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClientIdProperty().getValue()).asObject());
        column4.setCellValueFactory(cellData -> cellData.getValue().getStartDataProperty());
        column5.setCellValueFactory(cellData -> cellData.getValue().getEndDataProperty());
        column6.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMonthsProperty().getValue()).asObject());
        column7.setCellValueFactory(cellData -> cellData.getValue().getBillTypeProperty());
        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(pane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLs/show_contract_data_dialogue.fxml"));
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
                ShowContractDataDialogueController controller = fxmlLoader.getController();
                ContractData contractData = table.getSelectionModel().getSelectedItem();
                controller.updateInfo(contractData);
                dialog.showAndWait();
            }
        });
    }

    private void initButtons()
    {
        backButton.setOnMouseEntered(e -> backButton.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        backButton.setOnMouseExited(e -> backButton.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
        addButton.setOnMouseEntered(e -> addButton.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        addButton.setOnMouseExited(e -> addButton.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
        removeButton.setOnMouseEntered(e -> removeButton.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        removeButton.setOnMouseExited(e -> removeButton.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
        removeButton.setOnAction(event ->
        {
            ContractData contract = table.getSelectionModel().getSelectedItem();
            if(contract!=null)
            {
                Contracts.getContracts().removeContract(contract);
                if(Contracts.getContracts().getSize()%rowsPerPage()==0)
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
        modifyButton.setOnMouseEntered(e -> modifyButton.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        modifyButton.setOnMouseExited(e -> modifyButton.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
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

    private void createAlertDialogue()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, "\n        Niciun contract nu a fost selectat.", ButtonType.OK);
        alert.setHeaderText("");
        alert.setTitle("Error");
        alert.getDialogPane().setMaxWidth(350);
        alert.show();
    }
}



