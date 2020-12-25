package sample.ChannelsPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
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
        startDate.setConverter(DateFormatter.stringConverter);
        endDate.setConverter(DateFormatter.stringConverter);
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
        newChannel.setFrequency(Double.parseDouble(frequency.getText()));
        newChannel.setChannel(Integer.parseInt(channel.getText()));
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
        channelData.setName(name.getText());
        channelData.setType(type.getValue().toString());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(DateFormatter.formatter);
            channelData.setStartDate(formattedString);
        }
        else
        {
            channelData.setStartDate(null);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(DateFormatter.formatter);
            channelData.setEndDate(formattedString);
        }
        else
        {
            channelData.setEndDate(null);
        }
        channelData.setFrequency(Double.parseDouble(frequency.getText()));
        channelData.setChannel(Integer.parseInt(channel.getText()));
        ChannelsDatabaseHandler.getInstance().updateChannel(channelData);
    }
}
