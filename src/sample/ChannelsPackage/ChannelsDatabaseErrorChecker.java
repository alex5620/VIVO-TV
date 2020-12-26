package sample.ChannelsPackage;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ChannelsDatabaseErrorChecker {
    private static ChannelsDatabaseErrorChecker channelsDatabaseErrorChecker = new ChannelsDatabaseErrorChecker();
    private boolean errorFound;
    private String errorMessage;

    private ChannelsDatabaseErrorChecker() {};

    void checkError(String error)
    {
        errorFound = true;
        if(error.contains("to NULL"))
        {
            if(error.contains("\"POSTURI\".\"DATA_START\""))
            {
                errorMessage = "Data de start trebuie completata.";
            }
            else if(error.contains("\"POSTURI\".\"DENUMIRE_POST\""))
            {
                errorMessage = "Denumirea postului trebuie completata.";
            }
        }
        else if(error.contains("NULL into")) {
            if (error.contains("\"POSTURI\".\"DENUMIRE_POST\"")) {
                errorMessage = "Denumirea postului trebuie completata.";
            }
            else if(error.contains("\"POSTURI\".\"DATA_START\""))
            {
                errorMessage = "Data de start trebuie completata.";
            }
        }
        else if(error.contains("POSTURI_DENUMIRE_POST_CK"))
        {
            errorMessage = "Campul denumire trebuie sa aiba intre 2 si 25 de caractere.";
        }
        else if(error.contains("POSTURI_DENUMIRE_POST_UK"))
        {
            errorMessage = "Postul exista deja in grila.";
        }
        else if(error.contains("DENUMIRE_POSTURI_FRECVENTA_UK"))
        {
            errorMessage = "Exista deja un program la aceasta frecventa.";
        }
        else if(error.contains("DENUMIRE_POSTURI_CANAL_UK"))
        {
            errorMessage = "Exista deja un program cu acest canal.";
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

    public static ChannelsDatabaseErrorChecker getInstance() {
        return channelsDatabaseErrorChecker;
    }
}
