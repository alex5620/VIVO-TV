package sample.ChannelsPackage;

import javafx.beans.property.*;

public class ChannelData {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty startDate;
    private StringProperty endDate;
    private StringProperty type;
    private DoubleProperty frequency;
    private IntegerProperty channel;

    public ChannelData()
    {
        id = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        startDate = new SimpleStringProperty();
        endDate = new SimpleStringProperty();
        type = new SimpleStringProperty();
        frequency = new SimpleDoubleProperty();
        channel = new SimpleIntegerProperty();
    }

    void updateInfo(ChannelData channelData)
    {
        setName(channelData.getNameProperty().getValue());
        setStartDate(channelData.getStartDateProperty().getValue());
        setEndDate(channelData.getEndDateProperty().getValue());
        setType(channelData.getTypeProperty().getValue());
        setFrequency(channelData.getFrequencyProperty().getValue());
        setChannel(channelData.getChannelProperty().getValue());
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty getStartDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public StringProperty getEndDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public DoubleProperty getFrequencyProperty() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency.set(frequency);
    }

    public IntegerProperty getChannelProperty() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel.set(channel);
    }
}
