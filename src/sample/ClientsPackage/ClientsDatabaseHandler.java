package sample.ClientsPackage;

import java.sql.*;
import java.util.ArrayList;

public class ClientsDatabaseHandler {
    private static ClientsDatabaseHandler clientsDatabaseHandler = new ClientsDatabaseHandler();
    private Connection con;

    private ClientsDatabaseHandler(){};

    private void getConnection() {
        try {
            if(con==null || con.isClosed()) {
                con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:xe", "c##alex", "alex");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getClientsNumberById(String id) {
        getConnection();
        int count = 0;
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(*) FROM clienti WHERE TO_CHAR(id_client) LIKE ?");
            pStmt.setString(1, "%"+id+"%");
            ResultSet res = pStmt.executeQuery();
            res.next();
            count = res.getInt(1);
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return count;
    }

    public ArrayList<ClientData> getClientsWhenIdSelected(int page, int itemsPerPage, String id) {
        getConnection();
        ArrayList<ClientData> clients = null;
        try {
            clients = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM clienti WHERE TO_CHAR(id_client) LIKE ? ORDER BY id_client offset ? ROWS FETCH NEXT ? ROWS ONLY");
            pStmt.setString(1, "%"+id+"%");
            pStmt.setInt(2, page*itemsPerPage);
            pStmt.setInt(3, itemsPerPage);
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                ClientData clientData = new ClientData();
                clientData.setId(res.getInt("id_client"));
                String nume = res.getString("nume_client");
                clientData.setLastName(nume.split("_")[0]);
                clientData.setFirstName(nume.split("_")[1]);
                clientData.setPhoneNumber("0" + res.getInt("telefon"));
                clientData.setEmail(res.getString("email"));
                clients.add(clientData);
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return clients;
    }

    public int getClientsNumberByPhone(String phone) {
        getConnection();
        int count = 0;
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(*) FROM clienti WHERE telefon LIKE ?");
            pStmt.setString(1, "%"+phone+"%");
            ResultSet res = pStmt.executeQuery();
            res.next();
            count = res.getInt(1);
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return count;
    }

    public ArrayList<ClientData> getClientsWhenPhoneSelected(int page, int itemsPerPage, String phone) {
        getConnection();
        ArrayList<ClientData> clients = null;
        try {
            clients = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM clienti WHERE telefon LIKE ? ORDER BY id_client offset ? ROWS FETCH NEXT ? ROWS ONLY");
            pStmt.setString(1, "%"+phone+"%");
            pStmt.setInt(2, page*itemsPerPage);
            pStmt.setInt(3, itemsPerPage);
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                ClientData clientData = new ClientData();
                clientData.setId(res.getInt("id_client"));
                String nume = res.getString("nume_client");
                clientData.setLastName(nume.split("_")[0]);
                clientData.setFirstName(nume.split("_")[1]);
                clientData.setPhoneNumber("0" + res.getInt("telefon"));
                clientData.setEmail(res.getString("email"));
                clients.add(clientData);
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return clients;
    }

    public int getClientsNumberByName(String name) {
        getConnection();
        int count = 0;
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(*) FROM clienti WHERE nume_client LIKE ?");
            pStmt.setString(1, "%"+name+"%");
            ResultSet res = pStmt.executeQuery();
            if(res.next()) {
                count = res.getInt(1);
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return count;
    }

    public ArrayList<ClientData> getClientsWhenNameSelected(int page, int itemsPerPage, String name) {
        getConnection();
        ArrayList<ClientData> clients = null;
        try {
            clients = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM clienti WHERE nume_client LIKE ? ORDER BY id_client offset ? ROWS FETCH NEXT ? ROWS ONLY");
            pStmt.setString(1, "%"+name+"%");
            pStmt.setInt(2, page*itemsPerPage);
            pStmt.setInt(3, itemsPerPage);
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                ClientData clientData = new ClientData();
                clientData.setId(res.getInt("id_client"));
                String nume = res.getString("nume_client");
                clientData.setLastName(nume.split("_")[0]);
                clientData.setFirstName(nume.split("_")[1]);
                clientData.setPhoneNumber("0" + res.getInt("telefon"));
                clientData.setEmail(res.getString("email"));
                clients.add(clientData);
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return clients;
    }

    void updateClient(ClientData client)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "UPDATE clienti SET nume_client = ?," +
                                                                "telefon = ?," +
                                                                "email = ?" +
                                                                "WHERE id_client = ?");
            pStmt.setString(1, client.getLastNameProperty().getValue().toUpperCase()+"_"+
                    client.getFirstNameProperty().getValue().toUpperCase());
            pStmt.setInt(2, Integer.parseInt(client.getPhoneNumberProperty().getValue().substring(1)));
            pStmt.setString(3, client.getEmailProperty().getValue());
            pStmt.setInt(4, client.getIdProperty().getValue());
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    void removeClient(int id)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM clienti WHERE id_client = ?");
            pStmt.setInt(1, id);
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    void addClient(ClientData client)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "INSERT INTO clienti VALUES(?,?,?,?)");
            pStmt.setInt(1, ClientsDatabaseHandler.getInstance().getMaxID()+1);
            pStmt.setString(2, client.getLastNameProperty().getValue().toUpperCase()+"_"+
                    client.getFirstNameProperty().getValue().toUpperCase());
            pStmt.setInt(3, Integer.parseInt(client.getPhoneNumberProperty().getValue().substring(1)));
            pStmt.setString(4, client.getEmailProperty().getValue());
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public int getMaxID() {
        int maxID = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT MAX(id_client) FROM clienti");
            if (res.next()) {
                maxID = res.getInt(1);
            }
            res.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxID;
    }

    private void closeConnection()
    {
        try {
            if(con!=null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ClientsDatabaseHandler getInstance() {
        return clientsDatabaseHandler;
    }
}
