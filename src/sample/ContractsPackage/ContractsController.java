package sample.ContractsPackage;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.SimpleIntegerProperty;
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
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    InsertContractDataDialogueController controller = fxmlLoader.getController();
                    controller.processResult();
                    ContractsDatabaseErrorChecker errorChecker = ContractsDatabaseErrorChecker.getInstance();
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
        dialog.showAndWait();
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
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    controller.updateContract(contract);
                    ContractsDatabaseErrorChecker errorChecker = ContractsDatabaseErrorChecker.getInstance();
                    if(errorChecker.getErrorFound()) {
                        errorChecker.createAlertDialogue();
                        errorChecker.setErrorFound(false);
                        event.consume();
                    }
                    else
                    {
                        table.refresh();
                    }
                }
        );
        dialog.showAndWait();
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
        column2.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        column3.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClientIdProperty().getValue()).asObject());
        column4.setCellValueFactory(cellData -> cellData.getValue().getStartDateProperty());
        column5.setCellValueFactory(cellData -> cellData.getValue().getEndDateProperty());
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
                dialog.setTitle("Informaţii contract");
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
            if(contract!=null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ştergere date contract.");
                alert.setHeaderText("");
                alert.setContentText("În urma ştergerii contractului va dispărea şi istoricul despre " +
                        "pachetele şi dispozitivele închiriate. Doriţi să continuaţi?");
                ButtonType yesButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Nu", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);
                alert.showAndWait().ifPresent(type -> {
                    if (type == yesButton) {
                        ContractsDatabaseHandler.getInstance().removeAllContractData(contract.getContractNumberProperty().getValue());
                        int currentPage = pagination.getCurrentPageIndex();
                        int pageCount = pagination.getPageCount();
                        int clientsOnPage = Contracts.getContracts().getSize();
                        textField.setText(" " + textField.getText());
                        textField.setText(textField.getText().substring(1));
                        if (pageCount - 1 == currentPage && clientsOnPage == 1) {
                            pagination.setCurrentPageIndex(currentPage-1);
                        }
                        else
                        {
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
        modifyButton.setOnMouseEntered(e -> modifyButton.setStyle(sample.Styles.HOVERED_BUTTON_STYLE));
        modifyButton.setOnMouseExited(e -> modifyButton.setStyle(sample.Styles.IDLE_BUTTON_STYLE));
        nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FXMLFileLoader.loadFXML(pane, "ContractsPackage/FXMLs/contracts_menu2.fxml");
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

    private void addListenerToTextField()
    {
        textField.textProperty().addListener((observable, oldValue, newValue) -> doPagination());
    }

    private void doPagination() {
        int pagesNumber = getProperPageNumber(ContractsDatabaseHandler.getInstance().getContractsNumber(textField.getText().trim()));
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        Contracts.getContracts().addData(pageIndex, rowsPerPage(), textField.getText().trim());
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

    private void createAlertDialogue()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, "\n        Niciun contract nu a fost selectat.", ButtonType.OK);
        alert.setHeaderText("");
        alert.setTitle("Error");
        alert.getDialogPane().setMaxWidth(350);
        alert.show();
    }
}



