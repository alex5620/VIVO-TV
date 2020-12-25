package sample.ContractsPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contracts {
    private static Contracts contracts = new Contracts();
    private ObservableList<ContractData> allContracts;

    private Contracts() {}

    void addData(int page, int itemsPerPage, String id)
    {
        allContracts = FXCollections.observableArrayList(ContractsDatabaseHandler.getInstance().getContracts(page, itemsPerPage, id));
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
