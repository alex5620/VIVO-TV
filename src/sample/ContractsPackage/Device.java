package sample.ContractsPackage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Device {
    private IntegerProperty id;
    private StringProperty series;
    private StringProperty date;
    private StringProperty deviceType;

    Device()
    {
        id = new SimpleIntegerProperty();
        series = new SimpleStringProperty();
        date = new SimpleStringProperty();
        deviceType = new SimpleStringProperty();
    }

    public void setType(String type) {
        deviceType.setValue(type);
    }

    public StringProperty getDeviceType() {
        return deviceType;
    }

    public void setSeries(String series) {
        this.series.setValue(series);
    }

    public StringProperty getSeries() {
        return series;
    }

    public void setDate(String date) {
        this.date.setValue(date);
    }

    public StringProperty getDate() {
        return date;
    }

    public void setId(int id)
    {
        this.id.setValue(id);
    }

    public IntegerProperty getId() {
        return id;
    }

    public int getTypeId()
    {
        switch(deviceType.getValue().toUpperCase())
        {
            case "CARTELA CI":
                return 1;
            case "DECODOR":
                return 2;
            case "ANTENA SATELIT":
                return 3;
            case "ACCES APLICATIE MOBILA":
                return 4;
            case "TELEFON NOKIA 1.3":
                return 5;
            default:
                return 0;
        }
    }

    public String getTypeAbbreviation() {
        switch (deviceType.getValue().toUpperCase()) {
            case "CARTELA CI":
                return "CI";
            case "DECODOR":
                return "DEC";
            case "ANTENA SATELIT":
                return "AN";
            case "ACCES APLICATIE MOBILA":
                return "AM";
            case "TELEFON NOKIA 1.3":
                return "TEL";
            default:
                return "";
        }
    }
}
