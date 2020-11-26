package sample.ClientsPackage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShowClientDataDialogueController {
    @FXML Label phoneNumber;
    @FXML Label lastName;
    @FXML Label firstName;
    @FXML Label email;
    @FXML Label id;

    void updateInfo(ClientData client)
    {
        phoneNumber.setText(client.getPhoneNumberProperty().getValue());
        lastName.setText(client.getLastNameProperty().getValue());
        firstName.setText(client.getFirstNameProperty().get());
        email.setText(client.getEmailProperty().get());
        id.setText(Integer.toString(client.getIdProperty().get()));
    }
}
