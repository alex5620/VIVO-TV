package sample.ClientsPackage;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InsertClientDataDialogueController {
    @FXML TextField phoneNumber;
    @FXML TextField lastName;
    @FXML TextField firstName;
    @FXML TextField email;

    void processResult() {
        if(firstName.getText().length() == 0 || lastName.getText().length() == 0)
        {
            ClientsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ClientsDatabaseErrorChecker.getInstance().setErrorMessage("Campurile nume si prenume trebuie sa fie completate.");
            return;
        }
        ClientData newClient = new ClientData();
        newClient.setPhoneNumber(phoneNumber.getText());
        newClient.setLastName(lastName.getText().toUpperCase());
        newClient.setFirstName(firstName.getText().toUpperCase());
        newClient.setEmail(email.getText());
        ClientsDatabaseHandler.getInstance().addClient(newClient);
    }

    void updateTextFields(ClientData client)
    {
        phoneNumber.setText(client.getPhoneNumberProperty().getValue());
        lastName.setText(client.getLastNameProperty().getValue());
        firstName.setText(client.getFirstNameProperty().get());
        email.setText(client.getEmailProperty().get());
    }

    void updateClient(ClientData client)
    {
        if(firstName.getText().length() == 0 || lastName.getText().length() == 0)
        {
            ClientsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ClientsDatabaseErrorChecker.getInstance().setErrorMessage("Campurile nume si prenume trebuie sa fie completate.");
            return;
        }
        ClientData clientData = new ClientData();
        clientData.setId(client.getIdProperty().getValue());
        clientData.setFirstName(firstName.getText().toUpperCase());
        clientData.setLastName(lastName.getText().toUpperCase());
        clientData.setPhoneNumber(phoneNumber.getText());
        clientData.setEmail(email.getText());
        ClientsDatabaseHandler.getInstance().updateClient(clientData);
        if(!ClientsDatabaseErrorChecker.getInstance().getErrorFound())
        {
            client.updateInfo(clientData);
        }
    }
}
