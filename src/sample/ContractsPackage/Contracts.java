package sample.ContractsPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contracts {
    private static Contracts contracts = new Contracts();
    private ObservableList<ContractData> allContracts;

    private Contracts()
    {
        addData();
    }

    private void addData()
    {
        String [] devices = {"Cartela CI", "Decoder", "Antena", "Aplicatie mobila"};
        allContracts = FXCollections.observableArrayList();
        for (int i = 1; i <= 34; i++) {
            ContractData dataRow = new ContractData();
            Integer value1 = i;
            String value2 = "B2222211" + i;
            Integer value3 = (int)(Math.random()*2000000)%100000;
//            String value4 = Integer.toString(i+1000000);
//            String value5 = Integer.toString(i+2000000);
            Integer value6 = 12;
            String value7 = "E";
            dataRow.setContractNumber(value1);
            dataRow.setAddress(value2);
            dataRow.setClientId(value3);
//            dataRow.setStartData(value4);
//            dataRow.setEndData(value5);
            dataRow.setMonths(value6);
            dataRow.setBillType(value7);
            Device device2 = new Device();
            device2.setType(devices[3]);
            device2.setSeries(Integer.toString((int)(Math.random()*1000000)));
            device2.setId(devices.length+2);
            device2.setDate("23/9/2020");
            dataRow.addDevice(device2);
            Device device = new Device();
            device.setType(devices[(int)(Math.random()*5)%4]);
            device.setSeries(Integer.toString((int)(Math.random()*1000000)));
            device.setId(devices.length+1);
            device.setDate("11/2/2020");
            dataRow.addDevice(device);
            TVPackage tvPackage = new TVPackage();
            tvPackage.setTypeId((int)(Math.random()*5)%4);
            dataRow.addPackage(tvPackage);
            allContracts.add(dataRow);
        }
    }

    public static Contracts getContracts()
    {
        return contracts;
    }

    public void addContract(ContractData contract)
    {
        allContracts.add(contract);
    }

    public void removeContract(ContractData contract)
    {
        allContracts.remove(contract);
    }

    public ObservableList<ContractData> getAllContracts()
    {
        return allContracts;
    }

    public int getSize()
    {
        return allContracts.size();
    }
}
