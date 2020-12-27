package sample.ContractsPackage;

import sample.DateFormatter;

import java.sql.*;
import java.util.ArrayList;

public class ContractsDatabaseHandler {
    private static ContractsDatabaseHandler contractsDatabaseHandler = new ContractsDatabaseHandler();
    private Connection con;

    void getConnection() {
        try {
            if(con==null || con.isClosed()) {
                con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:xe", "c##test2", "test2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ContractData> getContracts(int page, int itemsPerPage, String id) {
        getConnection();
        ArrayList<ContractData> contracts = null;
        try {
            contracts = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM contracte WHERE TO_CHAR(nr_contract) LIKE ? ORDER BY nr_contract offset ? ROWS FETCH NEXT ? ROWS ONLY");
            pStmt.setString(1, "%"+id+"%");
            pStmt.setInt(2, page*itemsPerPage);
            pStmt.setInt(3, itemsPerPage);
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                ContractData contractData = new ContractData();
                contractData.setContractNumber(res.getInt("nr_contract"));
                contractData.setAddress(res.getString("adresa_contract"));
                Date startDate = res.getDate("data_start");
                if(startDate != null) {
                    contractData.setStartDate(startDate.toLocalDate().format(DateFormatter.formatter));
                }
                Date endDate = res.getDate("data_end");
                if(endDate != null) {
                    contractData.setEndDate(endDate.toLocalDate().format(DateFormatter.formatter));
                }
                contractData.setMonths(res.getInt("nr_luni"));
                contractData.setBillType(res.getString("tip_factura"));
                contractData.setClientId(res.getInt("id_client"));
                contracts.add(contractData);
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return contracts;
    }

    public ArrayList<ContractPackage> getPackagesByContractNumber(int contractNumber) {
        ArrayList<ContractPackage> packages = null;
        boolean firstPackage = true;
        try {
            packages = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM contracte_pachete WHERE nr_contract = ? ORDER BY data_start");
            pStmt.setInt(1, contractNumber);
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                ContractPackage contractPackage = new ContractPackage();
                contractPackage.setTypeId(res.getInt("id_pachet"));
                Date startDate = res.getDate("data_start");
                if(startDate != null) {
                    contractPackage.setStartDate(startDate.toLocalDate().format(DateFormatter.formatter));
                }
                if(firstPackage)
                {
                    contractPackage.setAdditionalPaperNumber(contractNumber);
                }
                else {
                    contractPackage.setAdditionalPaperNumber(res.getInt("nr_act_aditional"));
                }
                packages.add(contractPackage);
                firstPackage = false;
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packages;
    }

    public ArrayList<Device> getDevicesByContractNumber(int contractNumber) {
        ArrayList<Device> devices = null;
        try {
            devices = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM dispozitive WHERE nr_contract = ?");
            pStmt.setInt(1, contractNumber);
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                Device device = new Device();
                device.setId(res.getInt("id"));
                Date rentDate = res.getDate("data_inchiriere");
                if(rentDate != null) {
                    device.setDate(rentDate.toLocalDate().format(DateFormatter.formatter));
                }
                device.setSeries(res.getString("serie"));
                PreparedStatement pStmt2 = con.prepareStatement("SELECT denumire FROM tipuri_dispozitive WHERE id_tip = ?");
                pStmt2.setInt(1, res.getInt("id_tip"));
                ResultSet res2 = pStmt2.executeQuery();
                if(res2.next())
                {
                    device.setType(res2.getString(1));
                }
                pStmt2.close();
                res2.close();
                devices.add(device);
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices;
    }

    public int getContractsNumber(String id) {
        getConnection();
        int count = 0;
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(*) FROM contracte WHERE TO_CHAR(nr_contract) LIKE ?");
            pStmt.setString(1, "%"+id+"%");
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

    void addContract(ContractData contract)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "INSERT INTO contracte VALUES(?,?,?,?,?,?,?)");
            int maxContractNumber = ContractsDatabaseHandler.getInstance().getMaxContractNumber()+1;
            pStmt.setInt(1, maxContractNumber);
            pStmt.setString(2, contract.getAddressProperty().getValue());
            String startDate = contract.getStartDateProperty().getValue();
            if(startDate!=null && startDate.length()!=0) {
                pStmt.setDate(3, DateFormatter.getDatabaseFormat(startDate));
            }
            else
            {
                pStmt.setDate(3, null);
            }
            String endDate = contract.getEndDateProperty().getValue();
            if(endDate != null && endDate.length()!=0) {
                pStmt.setDate(4, DateFormatter.getDatabaseFormat(endDate));
            }
            else
            {
                pStmt.setDate(4, null);
            }
            pStmt.setInt(5, contract.getMonthsProperty().getValue());
            pStmt.setString(6, contract.getBillTypeProperty().getValue());
            pStmt.setInt(7, contract.getClientIdProperty().getValue());
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            ContractsDatabaseErrorChecker.getInstance().checkError(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    void addDevice(Device device, int contractNumber)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "INSERT INTO dispozitive VALUES(?,?,?,?,?)");
            int maxDeviceNumber = ContractsDatabaseHandler.getInstance().getMaxDeviceNumber()+1;
            pStmt.setInt(1, maxDeviceNumber);
            pStmt.setString(2, device.getSeries().getValue());
            String rentDate = device.getDate().getValue();
            if(rentDate!=null && rentDate.length()!=0) {
                pStmt.setDate(3, DateFormatter.getDatabaseFormat(rentDate));
            }
            else
            {
                pStmt.setDate(3, null);
            }
            pStmt.setInt(4, contractNumber);
            pStmt.setInt(5, device.getTypeId());
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            ContractsDatabaseErrorChecker.getInstance().checkError(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    void updateContract(ContractData contractData) {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement("UPDATE contracte SET adresa_contract = ?," +
                    "data_start = ?," +
                    "data_end = ?," +
                    "nr_luni = ?," +
                    "tip_factura = ?, " +
                    "id_client = ?" +
                    "WHERE nr_contract = ?");
            pStmt.setString(1, contractData.getAddressProperty().getValue());
            String startDate = contractData.getStartDateProperty().getValue();
            if (startDate != null && startDate.length() != 0) {
                pStmt.setDate(2, DateFormatter.getDatabaseFormat(startDate));
            } else {
                pStmt.setDate(2, null);
            }
            String endDate = contractData.getEndDateProperty().getValue();
            if (endDate != null && endDate.length() != 0) {
                pStmt.setDate(3, DateFormatter.getDatabaseFormat(endDate));
            } else {
                pStmt.setDate(3, null);
            }
            pStmt.setInt(4, contractData.getMonthsProperty().getValue());
            pStmt.setString(5, contractData.getBillTypeProperty().getValue());
            pStmt.setInt(6, contractData.getClientIdProperty().getValue());
            pStmt.setInt(7, contractData.getContractNumberProperty().getValue());
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            ContractsDatabaseErrorChecker.getInstance().checkError(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public int getMaxContractNumber() {
        int maxNum = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT MAX(nr_contract) FROM contracte");
            if (res.next()) {
                maxNum = res.getInt(1);
            }
            res.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxNum;
    }

    private void removeContract(int contractNumber)
    {
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM contracte WHERE nr_contract = ?");
            pStmt.setInt(1, contractNumber);
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaxDeviceNumber() {
        int maxNum = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT MAX(id) FROM dispozitive");
            if (res.next()) {
                maxNum = res.getInt(1);
            }
            res.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxNum;
    }

    void removeDevice(int deviceId)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM dispozitive WHERE id = ?");
            pStmt.setInt(1, deviceId);
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void removeDevicesByContractNumber(int contractNumber)
    {
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM dispozitive WHERE nr_contract = ?");
            pStmt.setInt(1, contractNumber);
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addContractPackage(ContractPackage contractPackage, int contractNumber)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "INSERT INTO contracte_pachete VALUES(?,?,?,?)");
            pStmt.setInt(1, contractNumber);
            pStmt.setInt(2, contractPackage.getTypeId());
            String startDate = contractPackage.getStartDate().getValue();
            if(startDate!=null && startDate.length()!=0) {
                pStmt.setDate(3, DateFormatter.getDatabaseFormat(startDate));
            }
            else
            {
                pStmt.setDate(3, null);
            }
            int additionPaperNumber = ContractsDatabaseHandler.getInstance().getMaxAdditionalNumber()+1;
            pStmt.setInt(4, additionPaperNumber);
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            ContractsDatabaseErrorChecker.getInstance().checkError(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public int getMaxAdditionalNumber() {
        int maxNum = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT MAX(nr_act_aditional) FROM contracte_pachete");
            if (res.next()) {
                maxNum = res.getInt(1);
            }
            res.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxNum;
    }

    void removePackage(int contractNumber, int idPackage)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM contracte_pachete WHERE nr_contract = ? AND id_pachet = ?");
            pStmt.setInt(1, contractNumber);
            pStmt.setInt(2, idPackage);
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    void removePackagesByContractNumber(int contractNumber)
    {
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM contracte_pachete WHERE nr_contract = ?");
            pStmt.setInt(1, contractNumber);
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAllContractData(int contractNumber)
    {
        getConnection();
        removeDevicesByContractNumber(contractNumber);
        removePackagesByContractNumber(contractNumber);
        removeContract(contractNumber);
        closeConnection();
    }

    void closeConnection()
    {
        try {
            if(con!=null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ContractsDatabaseHandler getInstance() {
        return contractsDatabaseHandler;
    }
}
