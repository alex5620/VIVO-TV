package sample.ClientsPackage;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InsertClientDataDialogueController {
    @FXML TextField phoneNumber;
    @FXML TextField lastName;
    @FXML TextField firstName;
    @FXML TextField email;

    void processResult() {
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
        client.setFirstName(firstName.getText().toUpperCase());
        client.setLastName(lastName.getText().toUpperCase());
        client.setPhoneNumber(phoneNumber.getText());
        client.setEmail(email.getText());
        ClientsDatabaseHandler.getInstance().updateClient(client);
    }
}
