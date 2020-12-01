package sample.TVPackages;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.ChannelsPackage.ChannelData;
import sample.ChannelsPackage.Channels;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InsertPackageDataDialogueController{
    @FXML private TextField name, price;
    @FXML private DatePicker startDate, endDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

    void processResult() {
        TVPackageData newPackage = new TVPackageData();
        newPackage.setID(Packages.getPackages().getAllPackages().get(Packages.getPackages().getAllPackages().size()-1).
                getIdProperty().getValue() + 1);
        newPackage.setName(name.getText());
        newPackage.setPrice(Double.parseDouble(price.getText()));
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(formatter);
            newPackage.setStartDate(formattedString);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(formatter);
            newPackage.setEndDate(formattedString);
        }
        Packages.getPackages().addPackage(newPackage);
    }

    void updateTextFields(TVPackageData packageData)
    {
        name.setText(packageData.getNameProperty().getValue());
        String stringStartDate = packageData.getStartDateProperty().getValue();
        if (stringStartDate != null) {
            startDate.setValue(LocalDate.parse(stringStartDate, formatter));
        }
        String stringEndDate = packageData.getEndDateProperty().getValue();
        if (stringEndDate != null) {
            endDate.setValue(LocalDate.parse(stringEndDate, formatter));
        }
        price.setText(Double.toString(packageData.getPriceProperty().get()));
    }

    void updateChannel(TVPackageData packageData)
    {
        packageData.setName(name.getText());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(formatter);
            packageData.setStartDate(formattedString);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(formatter);
            packageData.setEndDate(formattedString);
        }
        packageData.setPrice(Double.parseDouble(price.getText()));
    }
}
