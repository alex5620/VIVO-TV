package sample.ContractsPackage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class ContractData {
    private IntegerProperty contractNumber;
    private StringProperty address;
    private IntegerProperty clientId;
    private StringProperty startDate;
    private StringProperty endDate;
    private IntegerProperty months;
    private StringProperty billType;
    private ArrayList<Device> devices;
    private ArrayList<ContractPackage> packages;
    private StringProperty devicesBrief;

    public ContractData()
    {
        contractNumber = new SimpleIntegerProperty();
        address = new SimpleStringProperty();
        clientId = new SimpleIntegerProperty();
        startDate = new SimpleStringProperty();
        endDate = new SimpleStringProperty();
        months = new SimpleIntegerProperty();
        billType = new SimpleStringProperty();
        devices = new ArrayList<>();
        packages = new ArrayList<>();
        devicesBrief = new SimpleStringProperty("");
    }

    void updateInfo(ContractData contractData)
    {
        setAddress(contractData.getAddressProperty().getValue());
        setClientId(contractData.getClientIdProperty().getValue());
        setStartDate(contractData.getStartDateProperty().getValue());
        setEndDate(contractData.getEndDateProperty().getValue());
        setMonths(contractData.getMonthsProperty().getValue());
        setBillType(contractData.getBillTypeProperty().getValue());
    }

    @Override
    public String toString() {
        return "ContractData{" +
                "contractNumber=" + contractNumber +
                ", address=" + address +
                ", clientId=" + clientId +
                ", startData=" + startDate +
                ", endData=" + endDate +
                ", months=" + months +
                ", billType=" + billType +
                '}';
    }

    public IntegerProperty getContractNumberProperty() {
        return contractNumber;
    }

    public void setContractNumber(Integer contractNumber) {
        this.contractNumber.setValue(contractNumber);
    }

    public StringProperty getAddressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.setValue(address);
    }

    public IntegerProperty getClientIdProperty() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId.setValue(clientId);
    }

    public StringProperty getStartDateProperty() {
        return startDate;
    }

    public void setStartDate(String startData) {
        this.startDate.setValue(startData);
    }

    public StringProperty getEndDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.setValue(endDate);
    }

    public IntegerProperty getMonthsProperty() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months.setValue(months);
    }

    public StringProperty getBillTypeProperty() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType.setValue(billType);
    }

    public void addDevice(Device newDevice)
    {
        devices.add(newDevice);
        rewriteDevicesBrief();
    }

    public void rewriteDevicesBrief()
    {
        StringBuilder builder = new StringBuilder();
        for(int i=0;i < devices.size();++i) {
            builder.append(devices.get(i).getTypeAbbreviation());
            if (i < devices.size() - 1) {
                builder.append(",");
            }
        }
        devicesBrief.set(builder.toString());
    }

    public void addPackage(ContractPackage newContractPackage) {
        if (packages.size() != 0) {
            ContractPackage contractPackage = packages.get(0);
            ContractPackage contractPackage2 = new ContractPackage(contractPackage);
            contractPackage.setPackageType(newContractPackage.getPackageType().getValue());
            contractPackage.setStartDate(newContractPackage.getStartDate().getValue());
            contractPackage.setAdditionalPaperNumber(newContractPackage.getAdditionalPaperNumber());
            packages.add(contractPackage2);
        } else {
            packages.add(newContractPackage);
        }
    }

    public ArrayList<ContractPackage> getPackages()
    {
        return packages;
    }

    public ArrayList<Device> getDevices()
    {
        return devices;
    }

    public StringProperty getDevicesBrief() {
        return devicesBrief;
    }

    public int getPackagesNumber()
    {
        return packages.size();
    }

    public int getDevicesNumberByType(String typeAbbreviation)
    {
        int num = 0;
        for(Device device: devices)
        {
            if(typeAbbreviation.equals(device.getTypeAbbreviation()))
            {
                ++num;
            }
        }
        return num;
    }

    public int getDevicesNumber()
    {
        return devices.size();
    }
}
