package sample.TVPackages;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.ChannelsPackage.ChannelData;
import sample.ChannelsPackage.Channels;
import sample.ChannelsPackage.ChannelsDatabaseHandler;
import sample.DateFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InsertPackageDataDialogueController{
    @FXML private TextField name, price;
    @FXML private DatePicker startDate, endDate;

    void processResult() {
        TVPackageData newPackage = new TVPackageData();
        newPackage.setID(Packages.getPackages().getAllPackages().get(Packages.getPackages().getAllPackages().size()-1).
                getIdProperty().getValue() + 1);
        newPackage.setName(name.getText().toUpperCase());
        newPackage.setPrice(Double.parseDouble(price.getText()));
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
        packageData.setName(name.getText().toUpperCase());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            packageData.setStartDate(formattedString);
        }
        else
        {
            packageData.setStartDate(null);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(DateFormatter.formatter);
            packageData.setEndDate(formattedString);
        }
        else
        {
            packageData.setEndDate(null);
        }
        packageData.setPrice(Double.parseDouble(price.getText()));
        PackagesDatabaseHandler.getInstance().updatePackage(packageData);
    }
}
