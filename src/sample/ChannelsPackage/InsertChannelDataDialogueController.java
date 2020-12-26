package sample.ChannelsPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.DateFormatter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InsertChannelDataDialogueController implements Initializable{
    @FXML private TextField name, frequency, channel;
    @FXML private DatePicker startDate, endDate;
    @FXML private ChoiceBox type;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.getItems().add("4K");
        type.getItems().add("A");
        type.getItems().add("D");
        type.getItems().add("HD");
        type.setValue("A");
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
        ChannelData newChannel = new ChannelData();
        newChannel.setId(Channels.getChannels().getAllChannels().get(Channels.getChannels().getAllChannels().size()-1).
                getIdProperty().getValue() + 1);
        newChannel.setName(name.getText());
        newChannel.setType(type.getValue().toString());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            newChannel.setStartDate(formattedString);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(DateFormatter.formatter);
            newChannel.setEndDate(formattedString);
        }
        double frequencyValue;
        try {
            frequencyValue = Double.parseDouble(frequency.getText());
            newChannel.setFrequency(frequencyValue);
        }
        catch (NumberFormatException e)
        {
            ChannelsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ChannelsDatabaseErrorChecker.getInstance().setErrorMessage("Introduceti o valoare a frecventei corespunzatoare.");
            return;
        }
        if(frequencyValue < 50 || frequencyValue > 13500)
        {
            ChannelsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ChannelsDatabaseErrorChecker.getInstance().setErrorMessage("Frecventa trebuie sa fie cuprinsa in\nintervalul [50,13500].");
            return;
        }
        int channelValue;
        try {
            channelValue = Integer.parseInt(channel.getText());
            newChannel.setChannel(channelValue);
        }catch (NumberFormatException e)
        {
            ChannelsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ChannelsDatabaseErrorChecker.getInstance().setErrorMessage("Introduceti un canal corespunzator.");
            return;
        }
        if(channelValue < 1 || channelValue >= 1000)
        {
            ChannelsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ChannelsDatabaseErrorChecker.getInstance().setErrorMessage("Canalul trebuie sa fie cuprins in\nintervalul [1,999].");
            return;
        }
        ChannelsDatabaseHandler.getInstance().addChannel(newChannel);
    }

    void updateTextFields(ChannelData channelData)
    {
        name.setText(channelData.getNameProperty().getValue());
        type.setValue(channelData.getTypeProperty().getValue());
        String stringStartDate = channelData.getStartDateProperty().getValue();
        if (stringStartDate != null) {
            startDate.setValue(LocalDate.parse(stringStartDate, DateFormatter.formatter));
        }
        String stringEndDate = channelData.getEndDateProperty().getValue();
        if (stringEndDate != null) {
            endDate.setValue(LocalDate.parse(stringEndDate, DateFormatter.formatter));
        }
        frequency.setText(Double.toString(channelData.getFrequencyProperty().get()));
        channel.setText(Integer.toString(channelData.getChannelProperty().get()));
    }

    void updateChannel(ChannelData channelData)
    {
        ChannelData newChannel = new ChannelData();
        newChannel.setId(channelData.getIdProperty().getValue());
        newChannel.setName(name.getText());
        newChannel.setType(type.getValue().toString());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            newChannel.setStartDate(formattedString);
        }
        else
        {
            newChannel.setStartDate(null);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(DateFormatter.formatter);
            newChannel.setEndDate(formattedString);
        }
        else
        {
            newChannel.setEndDate(null);
        }
        double frequencyValue;
        try {
            frequencyValue = Double.parseDouble(frequency.getText());
            newChannel.setFrequency(frequencyValue);
        }
        catch (NumberFormatException e)
        {
            ChannelsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ChannelsDatabaseErrorChecker.getInstance().setErrorMessage("Introduceti o valoare a frecventei corespunzatoare.");
            return;
        }
        if(frequencyValue < 50 || frequencyValue > 13500)
        {
            ChannelsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ChannelsDatabaseErrorChecker.getInstance().setErrorMessage("Frecventa trebuie sa fie cuprinsa in\nintervalul [50,13500].");
            return;
        }
        int channelValue;
        try {
            channelValue = Integer.parseInt(channel.getText());
            newChannel.setChannel(channelValue);
        }catch (NumberFormatException e)
        {
            ChannelsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ChannelsDatabaseErrorChecker.getInstance().setErrorMessage("Introduceti un canal corespunzator.");
            return;
        }
        if(channelValue < 1 || channelValue >= 1000)
        {
            ChannelsDatabaseErrorChecker.getInstance().setErrorFound(true);
            ChannelsDatabaseErrorChecker.getInstance().setErrorMessage("Canalul trebuie sa fie cuprins in\nintervalul [1,999].");
            return;
        }
        ChannelsDatabaseHandler.getInstance().updateChannel(newChannel);
        if(!ChannelsDatabaseErrorChecker.getInstance().getErrorFound())
        {
            channelData.updateInfo(newChannel);
        }
    }
}
