package sample.ContractsPackage;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.format.DateTimeFormatter;

public class ChangePackageDialogueController {
    @FXML ChoiceBox packetType;
    @FXML DatePicker startDate;
    @FXML TextField paperNumber;
    private String [] packagesTypes = {"Standard", "Family", "Sport", "HBO Pack"};
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

    void setValidPackages(ContractData contractData)
    {
        for (String type : packagesTypes) {
            if (!contractData.getPackages().get(0).getPackageType().getValue().equals(type)) {
                packetType.getItems().add(type);
            }
        }
        packetType.setValue(packetType.getItems().get(0));
    }

    void processData(ContractData contractData)
    {
        TVPackage tvPackage = new TVPackage();
        tvPackage.setPackageType((String)packetType.getValue());
        tvPackage.setStartDate(startDate.getValue().format(formatter));
        tvPackage.setAdditionalPaperNumber(Integer.parseInt(paperNumber.getText()));
        contractData.addPackage(tvPackage);
    }
}
