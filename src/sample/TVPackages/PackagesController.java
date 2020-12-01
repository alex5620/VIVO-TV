package sample.TVPackages;

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
import sample.ChannelsPackage.Channels;
import sample.ContractsPackage.ContractData;
import sample.ContractsPackage.RemoveDeviceDialogueController;
import sample.ContractsPackage.ShowPackagesAndDevicesDialogueController;
import sample.Controller;
import sample.Styles;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PackagesController implements Initializable {
    @FXML private Pane pane;
    @FXML private Button backButton, addButton, removeButton, modifyButton, addChannels, removeChannels;
    @FXML private TextField textField;
    @FXML private Pagination pagination;
    @FXML private TableView<TVPackageData> table;
    @FXML private TableColumn<TVPackageData, Integer> idColumn;
    @FXML private TableColumn<TVPackageData, String> nameColumn, startDateColumn, endDateColumn;
    @FXML private TableColumn<TVPackageData, Double> priceColumn;
    private FilteredList<TVPackageData> filteredList;


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
    private void addNewPackage()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_package_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Adăugare pachet nou");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            InsertPackageDataDialogueController controller = fxmlLoader.getController();
            controller.processResult();
            if(Channels.getChannels().getSize()%rowsPerPage()==1)
            {
                pagination.setPageCount(pagination.getPageCount()+1);
            }
            pagination.setCurrentPageIndex(getProperPageNumber() - 1);
        }
    }

    @FXML
    private void modifyPackage()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/insert_package_data_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Actualizare date pachet");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        InsertPackageDataDialogueController controller = fxmlLoader.getController();
        TVPackageData packageData = table.getSelectionModel().getSelectedItem();
        if(packageData==null)
        {
            createAlertDialogue("Niciun pachet nu a fost selectat.");
            return;
        }
        controller.updateTextFields(packageData);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.updateChannel(packageData);
            table.refresh();
        }
    }

    @FXML
    public void listAvailableChannels()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/add_channels_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Adăugare canale");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        AddChannelsDialogueController controller = fxmlLoader.getController();
        TVPackageData packageData = table.getSelectionModel().getSelectedItem();
        if(packageData==null)
        {
            createAlertDialogue("Niciun pachet nu a fost selectat.");
            return;
        }
        controller.listAvailableChannels(packageData);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.addSelectedChannels(packageData);
            table.refresh();
        }
    }

    @FXML
    public void removeChannels()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXMLs/remove_channels_dialogue.fxml"));
        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Ştergere canale");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        RemoveChannelsDialogueController controller = fxmlLoader.getController();
        TVPackageData tvPackage = table.getSelectionModel().getSelectedItem();
        if(tvPackage==null)
        {
            createAlertDialogue("Niciun pachet nu a fost selectat.");
            return;
        }
        controller.addChannels(tvPackage);
        dialog.showAndWait();
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
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPriceProperty().getValue()).asObject());
        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(pane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLs/show_channels_dialogue.fxml"));
                try
                {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                }catch (IOException e)
                {
                    e.printStackTrace();
                    return;
                }
                dialog.setTitle("Canale disponibile");
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                ShowChannelsDialogueController controller = fxmlLoader.getController();
                TVPackageData packageData = table.getSelectionModel().getSelectedItem();
                controller.showInfo(packageData);
                dialog.showAndWait();
            }
        });
    }

    private void initButtons()
    {
        backButton.setOnMouseEntered(e -> backButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        backButton.setOnMouseExited(e -> backButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        addButton.setOnMouseEntered(e -> addButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        addButton.setOnMouseExited(e -> addButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        removeButton.setOnMouseEntered(e -> removeButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        removeButton.setOnMouseExited(e -> removeButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        removeButton.setOnAction(event ->
        {
            TVPackageData packageData = table.getSelectionModel().getSelectedItem();
            if(packageData!=null)
            {
                Packages.getPackages().removePackage(packageData);
                if(Packages.getPackages().getSize()%rowsPerPage()==0)
                {
                    pagination.setPageCount(pagination.getPageCount()-1);
                    pagination.setCurrentPageIndex(pagination.getPageCount()-1);
                }
            }
            else
            {
                createAlertDialogue("Niciun pachet nu a fost selectat.");
            }
        });
        modifyButton.setOnMouseEntered(e -> modifyButton.setStyle(Styles.HOVERED_BUTTON_STYLE));
        modifyButton.setOnMouseExited(e -> modifyButton.setStyle(Styles.IDLE_BUTTON_STYLE));
        addChannels.setOnMouseEntered(e -> addChannels.setStyle(Styles.HOVERED_BUTTON_STYLE));
        addChannels.setOnMouseExited(e -> addChannels.setStyle(Styles.IDLE_BUTTON_STYLE));
        removeChannels.setOnMouseEntered(e -> removeChannels.setStyle(Styles.HOVERED_BUTTON_STYLE));
        removeChannels.setOnMouseExited(e -> removeChannels.setStyle(Styles.IDLE_BUTTON_STYLE));
    }

    private void initPagination() {
        int pagesNumber = getProperPageNumber();
        pagination.setPageCount(pagesNumber);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        ObservableList<TVPackageData> allPackages = Packages.getPackages().getAllPackages();
        int fromIndex = pageIndex * rowsPerPage();
        int toIndex = Math.min(fromIndex + rowsPerPage(), allPackages.size());
        table.setItems(FXCollections.observableList(allPackages.subList(fromIndex, toIndex)));
        return new Pane(table);
    }

    private int rowsPerPage()
    {
        return 18;
    }

    private int getProperPageNumber() {
        int size = Packages.getPackages().getSize();
        int pagesNumber = size / rowsPerPage() + 1;
        if (size % rowsPerPage() == 0) {
            --pagesNumber;
        }
        return pagesNumber;
    }

    private void addListenerToTextField() {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList = new FilteredList(Packages.getPackages().getAllPackages());
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

    private void createAlertDialogue(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, "\n        "+message, ButtonType.OK);
        alert.setHeaderText("");
        alert.setTitle("Error");
        alert.getDialogPane().setMaxWidth(350);
        alert.show();
    }
}
