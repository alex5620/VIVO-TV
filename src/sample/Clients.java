package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class Clients {
    private static Clients clients = new Clients();
    private ObservableList<ClientData> allClients;

    private Clients()
    {
        addData();
    }

    private void addData()
    {
        allClients = FXCollections.observableArrayList();
        for (int i = 1; i <= 34; i++) {
            ClientData dataRow = new ClientData();
            Integer value1 = i;
            String value2 = "B222221111111111111111" + i;
            String value3 = "C22222" + i;
            String value4 = Integer.toString(i+1000000);
            dataRow.setId(value1);
            dataRow.setLastName(value2);
            dataRow.setFirstName(value3);
            dataRow.setPhoneNumber(value4);
            if(i%5==0)
            {
                dataRow.setEmail("cevasdaidsa.sudsfs"+i+"@yahoo.com");
            }
            allClients.add(dataRow);
        }
    }

    static Clients getClients()
    {
        return clients;
    }

    void addClient(ClientData client)
    {
        allClients.add(client);
    }

    void removeClient(ClientData client)
    {
        allClients.remove(client);
    }

    ObservableList<ClientData> getAllClients()
    {
        return allClients;
    }

    int getSize()
    {
        return allClients.size();
    }

}
