package sample.ContractsPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Contracts {
    private static Contracts contracts = new Contracts();
    private ObservableList<ContractData> allContracts;

    private Contracts() {}

    void addData(int page, int itemsPerPage, String id)
    {
        allContracts = FXCollections.observableArrayList(ContractsDatabaseHandler.getInstance().getContracts(page, itemsPerPage, id));
    }

    void addFullData(int page, int itemsPerPage, String id)
    {
        addData(page, itemsPerPage, id);
        ContractsDatabaseHandler.getInstance().getConnection();
        for(ContractData contract : allContracts)
        {
            ArrayList<ContractPackage> packages = ContractsDatabaseHandler.getInstance().
                    getPackagesByContractNumber(contract.getContractNumberProperty().getValue());
            for(ContractPackage contractPackage: packages)
            {
                contract.addPackage(contractPackage);
            }
            ArrayList<Device> devices = ContractsDatabaseHandler.getInstance().
                    getDevicesByContractNumber(contract.getContractNumberProperty().getValue());
            for(Device device: devices)
            {
                contract.addDevice(device);
            }
        }
        ContractsDatabaseHandler.getInstance().closeConnection();
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
