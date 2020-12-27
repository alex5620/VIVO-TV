package sample.TVPackages;

import javafx.collections.ObservableList;
import sample.ChannelsPackage.ChannelData;
import sample.DateFormatter;

import java.sql.*;
import java.util.ArrayList;

public class PackagesDatabaseHandler {
    private static PackagesDatabaseHandler packagesDatabaseHandler = new PackagesDatabaseHandler();
    private Connection con;

    private void getConnection() {
        try {
            if(con==null || con.isClosed()) {
                con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:xe", "c##test2", "test2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TVPackageData> getPackages(int page, int itemsPerPage, String name) {
        getConnection();
        ArrayList<TVPackageData> packages = null;
        try {
            packages = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM pachete WHERE UPPER(denumire_pachet) LIKE ? ORDER BY id_pachet offset ? ROWS FETCH NEXT ? ROWS ONLY");
            pStmt.setString(1, "%"+name+"%");
            pStmt.setInt(2, page*itemsPerPage);
            pStmt.setInt(3, itemsPerPage);
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                TVPackageData packageData = new TVPackageData();
                int idPachet = res.getInt("id_pachet");
                packageData.setID(idPachet);
                packageData.setName(res.getString("denumire_pachet"));
                Date startDate = res.getDate("data_start");
                if(startDate != null) {
                    packageData.setStartDate(startDate.toLocalDate().format(DateFormatter.formatter));
                }
                Date endDate = res.getDate("data_end");
                if(endDate != null) {
                    packageData.setEndDate(endDate.toLocalDate().format(DateFormatter.formatter));
                }
                packageData.setPrice(res.getDouble("pret"));
                PreparedStatement pStmt2 = con.prepareStatement("SELECT * FROM pachete_posturi WHERE id_pachet = ?");
                pStmt2.setInt(1, idPachet);
                ResultSet res2 = pStmt2.executeQuery();
                while(res2.next())
                {
                    int idPost = res2.getInt("id_post");
                    PreparedStatement pStmt3 = con.prepareStatement("SELECT * FROM posturi WHERE id_post = ?");
                    pStmt3.setInt(1, idPost);
                    ResultSet res3 = pStmt3.executeQuery();
                    if(res3.next()) {
                        ChannelData channelData = new ChannelData();
                        channelData.setId(res3.getInt("id_post"));
                        channelData.setName(res3.getString("denumire_post"));
                        Date channelStartDate = res3.getDate("data_start");
                        if (startDate != null) {
                            channelData.setStartDate(channelStartDate.toLocalDate().format(DateFormatter.formatter));
                        }
                        Date channelEndDate = res3.getDate("data_end");
                        if (endDate != null) {
                            channelData.setEndDate(channelEndDate.toLocalDate().format(DateFormatter.formatter));
                        }
                        channelData.setType(res3.getString("tip"));
                        PreparedStatement pStmt4 = con.prepareStatement("SELECT * FROM detalii_posturi WHERE id_post = ?");
                        pStmt4.setInt(1, idPost);
                        ResultSet res4 = pStmt4.executeQuery();
                        if (res4.next()) {
                            channelData.setFrequency(res4.getDouble("frecventa"));
                            channelData.setChannel(res4.getInt("canal"));
                        }
                        res4.close();
                        pStmt4.close();
                        packageData.addChannel(channelData);
                    }
                    res3.close();
                    pStmt3.close();
                }
                res2.close();
                pStmt2.close();
                packages.add(packageData);
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return packages;
    }

    public int getPackagesNumber(String name) {
        getConnection();
        int count = 0;
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(*) FROM pachete WHERE UPPER(denumire_pachet) LIKE ?");
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

    void addChannelsToPackage(ObservableList<ChannelData> channels, int packageID)
    {
        getConnection();
        try {
            for(ChannelData channelData: channels) {
                PreparedStatement pStmt = con.prepareStatement("INSERT INTO pachete_posturi VALUES(?,?)");
                pStmt.setInt(1, channelData.getIdProperty().getValue());
                pStmt.setInt(2, packageID);
                pStmt.executeQuery();
                pStmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    void removeChannelFromPackage(int idPachet, int idPost)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM pachete_posturi WHERE id_pachet = ? AND id_post = ?");
            pStmt.setInt(1, idPachet);
            pStmt.setInt(2, idPost);
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    void addPackage(TVPackageData packageData)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "INSERT INTO pachete VALUES(?,?,?,?,?)");
            int id = PackagesDatabaseHandler.getInstance().getMaxID()+1;
            pStmt.setInt(1, id);
            pStmt.setString(2, packageData.getNameProperty().getValue().toUpperCase());
            String startDate = packageData.getStartDateProperty().getValue();
            if(startDate!=null && startDate.length()!=0) {
                pStmt.setDate(3, DateFormatter.getDatabaseFormat(startDate));
            }
            else
            {
                pStmt.setDate(3, null);
            }
            String endDate = packageData.getEndDateProperty().getValue();
            if(endDate != null && endDate.length()!=0) {
                pStmt.setDate(4, DateFormatter.getDatabaseFormat(endDate));
            }
            else
            {
                pStmt.setDate(4, null);
            }
            pStmt.setDouble(5, packageData.getPriceProperty().getValue());
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            PackagesDatabaseErrorChecker.getInstance().checkError(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public int getMaxID() {
        int maxID = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT MAX(id_pachet) FROM pachete");
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

    void updatePackage(TVPackageData packageData)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "UPDATE pachete SET denumire_pachet = ?," +
                    "data_start = ?," +
                    "data_end = ?," +
                    "pret = ?" +
                    "WHERE id_pachet = ?");
            pStmt.setString(1, packageData.getNameProperty().getValue());
            String startDate = packageData.getStartDateProperty().getValue();
            if(startDate!=null && startDate.length()!=0) {
                pStmt.setDate(2, DateFormatter.getDatabaseFormat(startDate));
            }
            else
            {
                pStmt.setDate(2, null);
            }
            String endDate = packageData.getEndDateProperty().getValue();
            if(endDate != null && endDate.length()!=0) {
                pStmt.setDate(3, DateFormatter.getDatabaseFormat(endDate));
            }
            else
            {
                pStmt.setDate(3, null);
            }
            pStmt.setDouble(4, packageData.getPriceProperty().getValue());
            pStmt.setInt(5, packageData.getIdProperty().getValue());
            pStmt.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            PackagesDatabaseErrorChecker.getInstance().checkError(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    void removePackage(int idPachet)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM pachete_posturi WHERE id_pachet = ?");
            pStmt.setInt(1, idPachet);
            pStmt.executeQuery();
            PreparedStatement pStmt2 = con.prepareStatement( "DELETE FROM pachete WHERE id_pachet = ?");
            pStmt2.setInt(1, idPachet);
            pStmt2.executeQuery();
            pStmt.close();
            pStmt2.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public ArrayList<String> getPackagesNames() {
        getConnection();
        ArrayList<String> packagesNames = null;
        try {
            packagesNames = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT denumire_pachet FROM pachete");
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                packagesNames.add(res.getString(1));
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return packagesNames;
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

    public static PackagesDatabaseHandler getInstance() {
        return packagesDatabaseHandler;
    }
}
