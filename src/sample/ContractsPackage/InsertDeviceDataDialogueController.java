package sample.ContractsPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.DateFormatter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InsertDeviceDataDialogueController implements Initializable {
    @FXML ChoiceBox deviceType;
    @FXML TextField series;
    @FXML DatePicker rentDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rentDate.setConverter(DateFormatter.stringConverter);
        rentDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate day = LocalDate.of(2000,1,1);
                setDisable(empty || date.compareTo(day) < 0 );
            }});
    }

    void setValidDevices(ContractData contractData)
    {
        if(contractData.getDevicesNumberByType("CI")<2) {
            deviceType.getItems().add("Cartela CI");
        }
        if(contractData.getDevicesNumberByType("DEC")<2)
        {
            deviceType.getItems().add("Decodor");
        }
        if(contractData.getDevicesNumberByType("AN")<1)
        {
            deviceType.getItems().add("Antena satelit");
        }
        if(contractData.getDevicesNumberByType("AM")<1)
        {
            deviceType.getItems().add("Acces aplicatie mobila");
        }
        if(contractData.getDevicesNumberByType("TEL")<1)
        {
            deviceType.getItems().add("Telefon Nokia 1.3");
        }
        deviceType.setValue(deviceType.getItems().get(0));
    }

    void processResult(ContractData contractData) {
        Device device = new Device();
        device.setType((String)deviceType.getValue());
        device.setSeries(series.getText());
        if(rentDate.getValue()!=null) {
            String formattedString = rentDate.getValue().format(DateFormatter.formatter);
            device.setDate(formattedString);
        }
        ContractsDatabaseHandler.getInstance().addDevice(device, contractData.getContractNumberProperty().getValue());
    }
}
