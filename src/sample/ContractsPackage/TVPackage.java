package sample.ContractsPackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TVPackage {
    private StringProperty packageType;
    private StringProperty startDate;
    private int additionalPaperNumber;

    public TVPackage()
    {
        packageType = new SimpleStringProperty();
        startDate = new SimpleStringProperty();
    }

    public TVPackage(TVPackage tvPackage)
    {
        packageType = new SimpleStringProperty(tvPackage.getPackageType().getValue());
        startDate = new SimpleStringProperty(tvPackage.getStartDate().getValue());
        additionalPaperNumber = tvPackage.getAdditionalPaperNumber();
    }

    public void setPackageType(String type)
    {
        packageType.set(type);
    }

    public void setTypeId(int id)
    {
        switch(id)
        {
            case 0:
                packageType.setValue("Standard");
                break;
            case 1:
                packageType.setValue("Family");
                break;
            case 2:
                packageType.setValue("Sport");
                break;
            case 3:
                packageType.setValue("HBO Pack");
                break;
            default:
                packageType.setValue("");
        }
    }

    public StringProperty getPackageType()
    {
        return packageType;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public StringProperty getStartDate() {
        return startDate;
    }

    public void setAdditionalPaperNumber(int additionalPaperNumber) {
        this.additionalPaperNumber = additionalPaperNumber;
    }

    public int getAdditionalPaperNumber() {
        return additionalPaperNumber;
    }

    @Override
    public String toString() {
        return "TVPackage{" +
                "packageType=" + packageType +
                ", startDate='" + startDate + '\'' +
                ", additionalPaperNumber=" + additionalPaperNumber +
                '}';
    }
}
