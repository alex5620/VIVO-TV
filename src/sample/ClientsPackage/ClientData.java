package sample.ClientsPackage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientData{
    private IntegerProperty id;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty phoneNumber;
    private StringProperty email;

    public ClientData()
    {
        id = new SimpleIntegerProperty();
        firstName = new SimpleStringProperty();
        lastName = new SimpleStringProperty();
        phoneNumber = new SimpleStringProperty();
        email = new SimpleStringProperty();
    }

    @Override
    public String toString() {
        return "ClientData{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", phoneNumber=" + phoneNumber +
                ", email=" + email +
                '}';
    }

    void updateInfo(ClientData clientInfo)
    {
        setFirstName(clientInfo.getFirstNameProperty().getValue());
        setLastName(clientInfo.getLastNameProperty().getValue());
        setPhoneNumber(clientInfo.getPhoneNumberProperty().getValue());
        setEmail(clientInfo.getEmailProperty().getValue());
    }

    public void setId(Integer id) {
        this.id.setValue(id);
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName.setValue(firstName);
    }

    public StringProperty getFirstNameProperty() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName.setValue(lastName);
    }

    public StringProperty getLastNameProperty() {
        return lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    public StringProperty getPhoneNumberProperty() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public StringProperty getEmailProperty() {
        return email;
    }
}
