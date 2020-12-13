package sample.ChannelsPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InsertChannelDataDialogueController implements Initializable{
    @FXML private TextField name, frequency, channel;
    @FXML private DatePicker startDate, endDate;
    @FXML private ChoiceBox type;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.getItems().add("4K");
        type.getItems().add("A");
        type.getItems().add("D");
        type.getItems().add("HD");
    }

    void processResult() {
        ChannelData newChannel = new ChannelData();
        newChannel.setId(Channels.getChannels().getAllChannels().get(Channels.getChannels().getAllChannels().size()-1).
                getIdProperty().getValue() + 1);
        newChannel.setName(name.getText());
        newChannel.setType(type.getValue().toString());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(formatter);
            newChannel.setStartDate(formattedString);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(formatter);
            newChannel.setEndDate(formattedString);
        }
        newChannel.setFrequency(Double.parseDouble(frequency.getText()));
        newChannel.setChannel(Integer.parseInt(channel.getText()));
        ChannelsDatabaseHandler.getInstance().addChannel(newChannel);
    }

    void updateTextFields(ChannelData channelData)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        name.setText(channelData.getNameProperty().getValue());
        type.setValue(channelData.getTypeProperty().getValue());
        String stringStartDate = channelData.getStartDateProperty().getValue();
        if (stringStartDate != null) {
            startDate.setValue(LocalDate.parse(stringStartDate, formatter));
        }
        String stringEndDate = channelData.getEndDateProperty().getValue();
        if (stringEndDate != null) {
            endDate.setValue(LocalDate.parse(stringEndDate, formatter));
        }
        frequency.setText(Double.toString(channelData.getFrequencyProperty().get()));
        channel.setText(Integer.toString(channelData.getChannelProperty().get()));
    }

    void updateChannel(ChannelData channelData)
    {
        channelData.setName(name.getText());
        channelData.setType(type.getValue().toString());
        if(startDate.getValue()!=null) {
            String formattedString = startDate.getValue().format(formatter);
            channelData.setStartDate(formattedString);
        }
        if(endDate.getValue()!=null) {
            String formattedString = endDate.getValue().format(formatter);
            channelData.setEndDate(formattedString);
        }
        channelData.setFrequency(Double.parseDouble(frequency.getText()));
        channelData.setChannel(Integer.parseInt(channel.getText()));
        ChannelsDatabaseHandler.getInstance().updateChannel(channelData);
    }
}
