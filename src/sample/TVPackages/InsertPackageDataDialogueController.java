package sample.TVPackages;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.ChannelsPackage.ChannelData;
import sample.ChannelsPackage.Channels;
import sample.ChannelsPackage.ChannelsDatabaseErrorChecker;
import sample.ChannelsPackage.ChannelsDatabaseHandler;
import sample.DateFormatter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InsertPackageDataDialogueController implements Initializable {
    @FXML private TextField name, price;
    @FXML private DatePicker startDate, endDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startDate.setConverter(DateFormatter.stringConverter);
        endDate.setConverter(DateFormatter.stringConverter);
        startDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate day = LocalDate.of(2000,1,1);
                setDisable(empty || date.compareTo(day) < 0 );
            }});
        endDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate day = LocalDate.of(2000,1,1);
                setDisable(empty || date.compareTo(day) < 0 );
            }});
    }

    void processResult() {
        TVPackageData newPackage = new TVPackageData();
        newPackage.setID(Packages.getPackages().getAllPackages().get(Packages.getPackages().getAllPackages().size()-1).
                getIdProperty().getValue() + 1);
        newPackage.setName(name.getText().toUpperCase());
        double priceValue;
        try {
            priceValue = Double.parseDouble(price.getText());
            newPackage.setPrice(priceValue);
        }
        catch (NumberFormatException e)
        {
            PackagesDatabaseErrorChecker.getInstance().setErrorFound(true);
            PackagesDatabaseErrorChecker.getInstance().setErrorMessage("Introduceti un pret corespunzator.");
            return;
        }
        if(priceValue < 12 || priceValue > 200)
        {
            PackagesDatabaseErrorChecker.getInstance().setErrorFound(true);
            PackagesDatabaseErrorChecker.getInstance().setErrorMessage("Pretul trebuie sa fie cuprins in intre 12 si 200 de lei.");
            return;
        }
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            newPackage.setStartDate(formattedString);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(DateFormatter.formatter);
            newPackage.setEndDate(formattedString);
        }
        PackagesDatabaseHandler.getInstance().addPackage(newPackage);
    }

    void updateTextFields(TVPackageData packageData)
    {
        name.setText(packageData.getNameProperty().getValue());
        String stringStartDate = packageData.getStartDateProperty().getValue();
        if (stringStartDate != null) {
            startDate.setValue(LocalDate.parse(stringStartDate, DateFormatter.formatter));
        }
        String stringEndDate = packageData.getEndDateProperty().getValue();
        if (stringEndDate != null) {
            endDate.setValue(LocalDate.parse(stringEndDate, DateFormatter.formatter));
        }
        price.setText(Double.toString(packageData.getPriceProperty().get()));
    }

    void updatePackage(TVPackageData packageData)
    {
        TVPackageData newPackage = new TVPackageData();
        newPackage.setID(packageData.getIdProperty().getValue());
        newPackage.setName(name.getText().toUpperCase());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            newPackage.setStartDate(formattedString);
        }
        else
        {
            newPackage.setStartDate(null);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(DateFormatter.formatter);
            newPackage.setEndDate(formattedString);
        }
        else
        {
            newPackage.setEndDate(null);
        }
        double priceValue;
        try {
            priceValue = Double.parseDouble(price.getText());
            newPackage.setPrice(priceValue);
        }
        catch (NumberFormatException e)
        {
            PackagesDatabaseErrorChecker.getInstance().setErrorFound(true);
            PackagesDatabaseErrorChecker.getInstance().setErrorMessage("Introduceti un pret corespunzator.");
            return;
        }
        if(priceValue < 12 || priceValue > 200)
        {
            PackagesDatabaseErrorChecker.getInstance().setErrorFound(true);
            PackagesDatabaseErrorChecker.getInstance().setErrorMessage("Pretul trebuie sa fie cuprins in intre 12 si 200 de lei.");
            return;
        }
        PackagesDatabaseHandler.getInstance().updatePackage(newPackage);
        if(!PackagesDatabaseErrorChecker.getInstance().getErrorFound())
        {
            packageData.updateInfo(newPackage);
        }
    }
}
