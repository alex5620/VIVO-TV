package sample.ContractsPackage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;

public class ShowPackagesAndDevicesDialogueController {
    @FXML Label packageName, startDate, paperNumber;
    @FXML Label typeLabel, dateLabel, paperLabel;
    @FXML Label typeField, dateField, paperField;
    @FXML TableView<Device> devicesTable;
    @FXML TableColumn<Device, Integer> idColumn;
    @FXML TableColumn<Device, String> seriesColumn, rentDateColumn, typeColumn;

    void showInfo(ContractData contractData)
    {
        TVPackage tvPackage = contractData.getPackages().get(0);
        packageName.setText(tvPackage.getPackageType().getValue());
        startDate.setText(tvPackage.getStartDate().getValue());
        paperNumber.setText(Integer.toString(tvPackage.getAdditionalPaperNumber()));
        if(contractData.getPackages().size()>1)
        {
            TVPackage tvPackage2 = contractData.getPackages().get(1);
            typeLabel.setText("Tip:");
            dateLabel.setText("Dată start:");
            paperLabel.setText("Nr. act:");
            typeField.setText(tvPackage2.getPackageType().getValue());
            dateField.setText(tvPackage2.getStartDate().getValue());
            paperField.setText(Integer.toString(tvPackage2.getAdditionalPaperNumber()));
        }
        ObservableList<Device> devices = FXCollections.observableArrayList(contractData.getDevices());
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId().getValue()).asObject());
        seriesColumn.setCellValueFactory(cellData -> cellData.getValue().getSeries());
        rentDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDate());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getDeviceType());
        devicesTable.setItems(devices);
    }
}
