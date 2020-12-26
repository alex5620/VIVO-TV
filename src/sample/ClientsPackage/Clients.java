package sample.ClientsPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class Clients {
    private static Clients clients = new Clients();
    private ObservableList<ClientData> allClients;

    private Clients(){};

    public void addDataWhenIdSelected(int page, int itemsPerPage, String id)
    {
        allClients = FXCollections.observableArrayList(ClientsDatabaseHandler.getInstance().getClientsWhenIdSelected(page, itemsPerPage, id));
    }

    public void addDataWhenPhoneSelected(int page, int itemsPerPage, String phone)
    {
        allClients = FXCollections.observableArrayList(ClientsDatabaseHandler.getInstance().getClientsWhenPhoneSelected(page, itemsPerPage, phone));
    }

    public void addDataWhenNameSelected(int page, int itemsPerPage, String name)
    {
        allClients = FXCollections.observableArrayList(ClientsDatabaseHandler.getInstance().getClientsWhenNameSelected(page, itemsPerPage, name));
    }

    public static Clients getClients()
    {
        return clients;
    }

    public ObservableList<ClientData> getAllClients()
    {
        return allClients;
    }

    public int getSize()
    {
        return allClients.size();
    }

}
