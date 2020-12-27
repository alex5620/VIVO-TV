package sample.ContractsPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class RemovePackageDialogueController {
    @FXML ListView listView;
    private ContractData contractData;
    void addPackages(ContractData contractData)
    {
        ObservableList<ContractPackage> list = FXCollections.observableArrayList(contractData.getPackages());
        listView.setCellFactory(param -> new TableCell());
        listView.setItems(list);
        this.contractData = contractData;
    }

    class TableCell extends ListCell<ContractPackage> {
        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button button = new Button("Delete");

        TableCell() {
            hbox.getChildren().addAll(label, pane, button);
            button.setOnAction(event ->
            {
                ContractsDatabaseHandler.getInstance().removePackage(contractData.getContractNumberProperty().getValue(), getItem().getTypeId());
                getListView().getItems().remove(getItem());
            });
            HBox.setHgrow(pane, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(ContractPackage item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                String name = item.getPackageType().getValue();
                label.setText("Denumire: "+name);
                setGraphic(hbox);
            }
        }
    }
}
