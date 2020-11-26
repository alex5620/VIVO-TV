package sample.ContractsPackage;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;

public class InsertDeviceDataDialogueController {
    @FXML ChoiceBox deviceType;
    @FXML TextField series;
    @FXML DatePicker rentDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

    void setValidDevices(ContractData contractData)
    {
        boolean isSet = false;
        if(contractData.getDevicesNumberByType("CI")<2) {
            deviceType.getItems().add("Cartela CI");
            if (isSet == false) {
                deviceType.setValue("Cartela CI");
                isSet = true;
            }
        }
        if(contractData.getDevicesNumberByType("DEC")<2)
        {
            deviceType.getItems().add("Decoder");
            if (isSet == false) {
                deviceType.setValue("Decoder");
                isSet = true;
            }
        }
        if(contractData.getDevicesNumberByType("AN")<1)
        {
            deviceType.getItems().add("Antena");
            if (isSet == false) {
                deviceType.setValue("Antena");
                isSet = true;
            }
        }
        if(contractData.getDevicesNumberByType("AM")<1)
        {
            deviceType.getItems().add("Aplicatie mobila");
            if (isSet == false) {
                deviceType.setValue("Aplicatie mobila");
            }
        }
    }

    void processResult(ContractData contractData) {
        Device device = new Device();
        device.setType((String)deviceType.getValue());
        device.setSeries(series.getText());
        if(rentDate.getValue()!=null) {
            String formattedString = rentDate.getValue().format(formatter);
            device.setDate(formattedString);
        }
        device.setId(contractData.getDevices().get(contractData.getDevicesNumber()-1).getId().getValue()+1);
        contractData.addDevice(device);
    }
}
