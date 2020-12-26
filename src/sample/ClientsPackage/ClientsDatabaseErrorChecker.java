package sample.ClientsPackage;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ClientsDatabaseErrorChecker {
    private static ClientsDatabaseErrorChecker clientsDatabaseErrorChecker = new ClientsDatabaseErrorChecker();
    private boolean errorFound;
    private String errorMessage;

    private ClientsDatabaseErrorChecker() {};

    void checkError(String error)
    {
        errorFound = true;
        if(error.contains("value too large"))
        {
            if(error.contains("\"CLIENTI\".\"TELEFON\""))
            {
                errorMessage = "Numarul de telefon trebuie sa fie format din 10 cifre.";
            }
            else if(error.contains("\"CLIENTI\".\"EMAIL\""))
            {
                errorMessage = "Adresa de e-mail trebuie sa aiba mai putin de 40 de caractere.";
            }
            else if(error.contains("\"CLIENTI\".\"NUME_CLIENT\""))
            {
                errorMessage = "Numele trebuie sa aiba mai putin de 40 de caractere (NUME_PRENUME < 40).";
            }
        }
        else if(error.contains("\"CLIENTI\".\"TELEFON\"") && error.contains("to NULL"))
        {
            errorMessage = "Campul telefon trebuie completat.";
        }
        else if(error.contains("CLIENTI_EMAIL_CK"))
        {
             errorMessage = "Adresa de e-mail este invalida.";
        }
        else if(error.contains("CLIENTI_NUME_CLIENT_CK"))
        {
            errorMessage = "Numele si prenumele nu pot contine cifre.";
        }
        else if(error.contains("CLIENTI_TELEFON_CK"))
        {
            errorMessage = "Numarul de telefon trebuie sa fie de forma:\n 02xxxxxxxx, 03xxxxxxxx sau 07xxxxxxxx.";
        }
        else if(error.contains("CLIENTI_TELEFON_UK"))
        {
            errorMessage = "Numarul de telefon exista deja in baza de date.";
        }
        else if(error.contains("CLIENTI_EMAIL_UK"))
        {
            errorMessage = "Adresa de e-mail exista deja in baza de date";
        }
    }

    public void setErrorMessage(String message)
    {
        errorMessage = message;
    }

    public void setErrorFound(boolean value)
    {
        errorFound = value;
    }

    public boolean getErrorFound()
    {
        return errorFound;
    }

    public void createAlertDialogue()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
        alert.setHeaderText("");
        alert.setTitle("Error");
        alert.getDialogPane().setMaxWidth(350);
        alert.show();
    }

    public static ClientsDatabaseErrorChecker getInstance() {
        return clientsDatabaseErrorChecker;
    }
}
