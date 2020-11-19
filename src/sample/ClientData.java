package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientData {
    private IntegerProperty id;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty phoneNumber;
    private StringProperty email;

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

    ClientData()
    {
        id = new SimpleIntegerProperty();
        firstName = new SimpleStringProperty();
        lastName = new SimpleStringProperty();
        phoneNumber = new SimpleStringProperty();
        email = new SimpleStringProperty();
    }


    void setId(Integer id) {
        this.id.setValue(id);
    }

    IntegerProperty getIdProperty() {
        return id;
    }

    void setFirstName(String firstName) {
        this.firstName.setValue(firstName);
    }

    StringProperty getFirstNameProperty() {
        return firstName;
    }

    void setLastName(String lastName) {
        this.lastName.setValue(lastName);
    }

    StringProperty getLastNameProperty() {
        return lastName;
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    StringProperty getPhoneNumberProperty() {
        return phoneNumber;
    }

    void setEmail(String email) {
        this.email.setValue(email);
    }

    StringProperty getEmailProperty() {
        return email;
    }
}
