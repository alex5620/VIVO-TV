package sample.ContractsPackage;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ShowPackagesAndDevicesDialogueController {
    @FXML Label packageName, startDate, paperNumber;
    @FXML Label typeLabel, dateLabel, paperLabel, paperNumberLabel;
    @FXML Label typeField, dateField, paperField;
    @FXML TableView<Device> devicesTable;
    @FXML TableColumn<Device, Integer> idColumn;
    @FXML TableColumn<Device, String> seriesColumn, rentDateColumn, typeColumn;

    void showInfo(ContractData contractData) {
        if (contractData.getPackages().size() > 0) {
            ContractPackage contractPackage = contractData.getPackages().get(0);
            packageName.setText(contractPackage.getPackageType().getValue());
            startDate.setText(contractPackage.getStartDate().getValue());
            paperNumber.setText(Integer.toString(contractPackage.getAdditionalPaperNumber()));
            if (contractData.getPackages().size() > 1) {
                ContractPackage contractPackage2 = contractData.getPackages().get(1);
                typeLabel.setText("Tip:");
                dateLabel.setText("Dată start:");
                paperLabel.setText("Nr. act:");
                paperNumberLabel.setText("Nr. act aditional:");
                typeField.setText(contractPackage2.getPackageType().getValue());
                dateField.setText(contractPackage2.getStartDate().getValue());
                paperField.setText(Integer.toString(contractPackage2.getAdditionalPaperNumber()));
                paperNumber.setLayoutX(paperNumber.getLayoutX() + 60);
                if (contractPackage.getAdditionalPaperNumber() == 0) {
                    paperNumber.setText("-");
                }
            }
            ObservableList<Device> devices = FXCollections.observableArrayList(contractData.getDevices());
            idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId().getValue()).asObject());
            seriesColumn.setCellValueFactory(cellData -> cellData.getValue().getSeries());
            rentDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDate());
            typeColumn.setCellValueFactory(cellData -> cellData.getValue().getDeviceType());
            devicesTable.setItems(devices);
        }
    }
}
