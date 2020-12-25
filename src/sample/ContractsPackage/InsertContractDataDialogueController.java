package sample.ContractsPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.DateFormatter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InsertContractDataDialogueController implements Initializable {
    @FXML TextField address, clientId;
    @FXML DatePicker startDate, endDate;
    @FXML ChoiceBox months, billType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billType.getItems().add("Fizică");
        billType.getItems().add("Electronică");
        billType.setValue("Fizică");
        months.getItems().add(12);
        months.getItems().add(24);
        months.getItems().add(36);
        months.setValue(24);
        startDate.setConverter(DateFormatter.stringConverter);
        endDate.setConverter(DateFormatter.stringConverter);
    }

    void processResult() {
        ContractData newContract = new ContractData();
        newContract.setContractNumber(Contracts.getContracts().getAllContracts().get(Contracts.getContracts().getAllContracts().size()-1).
                getContractNumberProperty().getValue() + 1);
        newContract.setAddress(address.getText());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            newContract.setStartDate(formattedString);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(DateFormatter.formatter);
            newContract.setEndDate(formattedString);
        }
        newContract.setMonths((int)months.getValue());
        System.out.println((int)months.getValue());
        newContract.setBillType(((String)billType.getValue()).substring(0,1));
        System.out.println((String)billType.getValue());
        newContract.setClientId(Integer.parseInt(clientId.getText()));
        ContractsDatabaseHandler.getInstance().addContract(newContract);
    }

    void updateTextFields(ContractData contract) {
        address.setText(contract.getAddressProperty().getValue());
        months.setValue(contract.getMonthsProperty().getValue());
        String type = contract.getBillTypeProperty().get();
        billType.setValue(type.equals("F") ? "Fizică" : "Electronică");
        clientId.setText(Integer.toString(contract.getClientIdProperty().get()));
        String stringStartDate = contract.getStartDateProperty().getValue();
        if (stringStartDate != null) {
            startDate.setValue(LocalDate.parse(stringStartDate, DateFormatter.formatter));
        }
        String stringEndDate = contract.getEndDateProperty().getValue();
        if (stringEndDate != null) {
            endDate.setValue(LocalDate.parse(stringEndDate, DateFormatter.formatter));
        }
    }

    void updateContract(ContractData contract)
    {
        contract.setAddress(address.getText());
        contract.setMonths((Integer)months.getValue());
        contract.setBillType(((String)billType.getValue()).substring(0,1));
        contract.setClientId(Integer.parseInt(clientId.getText()));
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            contract.setStartDate(formattedString);
        }
        else
        {
            contract.setStartDate(null);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(DateFormatter.formatter);
            contract.setEndDate(formattedString);
        }
        else
        {
            contract.setEndDate(null);
        }
    }
}
