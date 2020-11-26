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

public class RemoveDeviceDialogueController {
    @FXML ListView listView;
    private ContractData contractData;
    void addDevices(ContractData contractData)
    {
        ObservableList<Device> list = FXCollections.observableArrayList(contractData.getDevices());
        listView.setCellFactory(param -> new TableCell());
        listView.setItems(list);
        this.contractData = contractData;
    }

    class TableCell extends ListCell<Device> {
        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button button = new Button("Delete");

        TableCell() {
            hbox.getChildren().addAll(label, pane, button);
            button.setOnAction(event ->
            {
                contractData.removeDevice(getItem());
                getListView().getItems().remove(getItem());
            });
            HBox.setHgrow(pane, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(Device item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                if(item.getTypeAbbreviation().equals("AM"))
                {
                    hbox.getChildren().remove(button);
                }
                String id = Integer.toString(item.getId().getValue());
                String series = item.getSeries().getValue();
                String type = item.getDeviceType().getValue();
                label.setText("ID: "+id+", seria:   "+series+", tip:   "+type);
                setGraphic(hbox);
            }
        }
    }
}
