package sample.TVPackages;

import javafx.beans.property.*;
import sample.ChannelsPackage.ChannelData;

import java.util.ArrayList;

public class TVPackageData {
    private IntegerProperty ID;
    private StringProperty name;
    private StringProperty startDate;
    private StringProperty endDate;
    private DoubleProperty price;
    private ArrayList<ChannelData> availableChannels;

    TVPackageData()
    {
        ID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        startDate = new SimpleStringProperty();
        endDate = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        availableChannels = new ArrayList<>();
    }

    void updateInfo(TVPackageData newPackage)
    {
        setName(newPackage.getNameProperty().getValue());
        setStartDate(newPackage.getStartDateProperty().getValue());
        setEndDate(newPackage.getEndDateProperty().getValue());
        setPrice(newPackage.getPriceProperty().getValue());
    }

    public IntegerProperty getIdProperty() {
        return ID;
    }

    public void setID(int ID) {
        this.ID.set(ID);
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

    public DoubleProperty getPriceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void addChannel(ChannelData channel)
    {
        availableChannels.add(channel);
    }

    public void removeChannel(ChannelData channel)
    {
        availableChannels.remove(channel);
    }

    public ArrayList<ChannelData> getAvailableChannels() {
        return availableChannels;
    }
}
