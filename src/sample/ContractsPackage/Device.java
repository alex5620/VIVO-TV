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

    public String getTypeAbbreviation() {
        switch (deviceType.getValue()) {
            case "Cartela CI":
                return "CI";
            case "Decoder":
                return "DEC";
            case "Antena":
                return "AN";
            case "Aplicatie mobila":
                return "AM";
            default:
                return "";
        }
    }
}
