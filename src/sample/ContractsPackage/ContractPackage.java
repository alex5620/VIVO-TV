package sample.ContractsPackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sample.TVPackages.PackagesDatabaseHandler;

import java.util.ArrayList;

public class ContractPackage {
    private static ArrayList<String> packagesTypes;
    private StringProperty packageType;
    private StringProperty startDate;
    private int additionalPaperNumber;

    static {
        packagesTypes = PackagesDatabaseHandler.getInstance().getPackagesNames();
    }

    public ContractPackage()
    {
        packageType = new SimpleStringProperty();
        startDate = new SimpleStringProperty();
    }

    public ContractPackage(ContractPackage contractPackage)
    {
        packageType = new SimpleStringProperty(contractPackage.getPackageType().getValue());
        startDate = new SimpleStringProperty(contractPackage.getStartDate().getValue());
        additionalPaperNumber = contractPackage.getAdditionalPaperNumber();
    }

    public void setPackageType(String type)
    {
        packageType.set(type);
    }

    public void setTypeId(int id)
    {
        packageType.setValue(packagesTypes.get(id-1));
    }

    public int getTypeId()
    {
        for(int i=0;i<packagesTypes.size();++i)
        {
            if(packageType.getValue().equals(packagesTypes.get(i)))
            {
                return i+1;
            }
        }
        return 0;
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
