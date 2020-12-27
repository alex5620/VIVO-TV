package sample.ContractsPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.DateFormatter;
import sample.TVPackages.PackagesDatabaseHandler;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChangePackageDialogueController implements Initializable {
    @FXML ChoiceBox packetType;
    @FXML DatePicker startDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startDate.setConverter(DateFormatter.stringConverter);
        startDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate day = LocalDate.of(2000,1,1);
                setDisable(empty || date.compareTo(day) < 0 );
            }});
    }

    void setValidPackages(ContractData contractData)
    {
        ArrayList<String> packagesTypes = PackagesDatabaseHandler.getInstance().getPackagesNames();
        boolean cond = contractData.getPackages().size() == 0;
        for (String type : packagesTypes) {
            if (cond || !contractData.getPackages().get(0).getPackageType().getValue().equals(type)) {
                packetType.getItems().add(type);
            }
        }
        packetType.setValue(packetType.getItems().get(0));
    }

    void processData(ContractData contractData)
    {
        ContractPackage contractPackage = new ContractPackage();
        contractPackage.setPackageType((String)packetType.getValue());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            contractPackage.setStartDate(formattedString);
        }
        ContractsDatabaseHandler.getInstance().addContractPackage(contractPackage, contractData.getContractNumberProperty().getValue());
    }
}
