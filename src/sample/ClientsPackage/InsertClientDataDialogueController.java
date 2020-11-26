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
        newClient.setId(Clients.getClients().getAllClients().get(Clients.getClients().getAllClients().size()-1).
                getIdProperty().getValue() + 1);
        newClient.setPhoneNumber(phoneNumber.getText());
        newClient.setLastName(lastName.getText());
        newClient.setFirstName(firstName.getText());
        newClient.setEmail(email.getText());
        Clients.getClients().addClient(newClient);
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
        client.setFirstName(firstName.getText());
        client.setLastName(lastName.getText());
        client.setPhoneNumber(phoneNumber.getText());
        client.setEmail(email.getText());
    }
}
