package sample.ContractsPackage;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ContractsDatabaseErrorChecker {
    private static ContractsDatabaseErrorChecker contractsDatabaseErrorChecker = new ContractsDatabaseErrorChecker();
    private boolean errorFound;
    private String errorMessage;

    private ContractsDatabaseErrorChecker() {};

    void checkError(String error)
    {
        errorFound = true;
        if(error.contains("to NULL"))
        {
            if(error.contains("\"CONTRACTE\".\"ADRESA_CONTRACT\""))
            {
                errorMessage = "Campul adresa trebuie completat.";
            }
            else if(error.contains("\"CONTRACTE\".\"DATA_START\""))
            {
                errorMessage = "Data de start trebuie completata.";
            }
        }
        else if(error.contains("NULL into")) {
            if (error.contains("\"CONTRACTE\".\"ADRESA_CONTRACT\"")) {
                errorMessage = "Campul adresa trebuie completat.";
            }
            else if(error.contains("\"CONTRACTE\".\"DATA_START\""))
            {
                errorMessage = "Data de start trebuie completata.";
            }
        }
        else if(error.contains("value too large"))
        {
            if(error.contains("\"CONTRACTE\".\"ADRESA_CONTRACT\""))
            {
                errorMessage = "Campul adresa trebuie sa aiba intre 1 si 60 de caractere.";
            }
        }
        else if(error.contains("CONTRACTE_ADRESA_CONTRACT_UK"))
        {
            errorMessage = "Exista deja un contract la aceasta adresa.";
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

    public static ContractsDatabaseErrorChecker getInstance() {
        return contractsDatabaseErrorChecker;
    }
}
