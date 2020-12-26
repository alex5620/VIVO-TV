package sample.TVPackages;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class PackagesDatabaseErrorChecker {
    private static PackagesDatabaseErrorChecker packagesDatabaseErrorChecker = new PackagesDatabaseErrorChecker();
    private boolean errorFound;
    private String errorMessage;

    private PackagesDatabaseErrorChecker() {};

    void checkError(String error)
    {
        errorFound = true;
        if(error.contains("to NULL"))
        {
            if(error.contains("\"PACHETE\".\"DENUMIRE_PACHET\""))
            {
                errorMessage = "Campul denumire trebuie completat.";
            }
            else if(error.contains("\"PACHETE\".\"DATA_START\""))
            {
                errorMessage = "Data de start trebuie completata.";
            }
        }
        else if(error.contains("NULL into")) {
            if (error.contains("\"PACHETE\".\"DENUMIRE_PACHET\"")) {
                errorMessage = "Campul denumire trebuie completat.";
            }
            else if(error.contains("\"PACHETE\".\"DATA_START\""))
            {
                errorMessage = "Data de start trebuie completata.";
            }
        }
        else if(error.contains("value too large") && error.contains("\"PACHETE\".\"DENUMIRE_PACHET\"")
                || error.contains("PACHETE_DENUMIRE_PACHET_CK"))
        {
            errorMessage = "Campul denumire trebuie sa aiba intre 2 si 25 de caractere.";
        }
        else if(error.contains("PACHETE_DENUMIRE_PACHETE_UK"))
        {
            errorMessage = "Exista deja un pachet cu aceasta denumire";
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

    public static PackagesDatabaseErrorChecker getInstance() {
        return packagesDatabaseErrorChecker;
    }
}
