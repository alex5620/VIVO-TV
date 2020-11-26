package sample.ContractsPackage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.ClientsPackage.ClientData;

public class ShowContractDataDialogueController {
    @FXML private Label contractNumber;
    @FXML private Label address;
    @FXML private Label clientId;
    @FXML private Label startDate;
    @FXML private Label endDate;
    @FXML private Label months;
    @FXML private Label billType;

    void updateInfo(ContractData contractData)
    {
        contractNumber.setText(Integer.toString(contractData.getContractNumberProperty().getValue()));
        address.setText(contractData.getAddressProperty().getValue());
        clientId.setText(Integer.toString(contractData.getClientIdProperty().get()));
        startDate.setText(contractData.getStartDataProperty().get());
        endDate.setText(contractData.getEndDataProperty().get());
        months.setText(Integer.toString(contractData.getMonthsProperty().get()));
        billType.setText(contractData.getBillTypeProperty().getValue());
    }
}
