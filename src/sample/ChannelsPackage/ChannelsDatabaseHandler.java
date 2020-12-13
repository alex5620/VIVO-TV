package sample.ChannelsPackage;

import sample.ClientsPackage.ClientData;
import sample.DateFormatter;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ChannelsDatabaseHandler {
    private static ChannelsDatabaseHandler channelsDatabaseHandler = new ChannelsDatabaseHandler();
    private Connection con;

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

    public int getChannelsNumber(String name) {
        getConnection();
        int count = 0;
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(*) FROM posturi WHERE UPPER(denumire_post) LIKE ?");
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

    public ArrayList<ChannelData> getChannels(int page, int itemsPerPage, String name) {
        getConnection();
        ArrayList<ChannelData> channels = null;
        try {
            channels = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM posturi WHERE UPPER(denumire_post) LIKE ? ORDER BY id_post offset ? ROWS FETCH NEXT ? ROWS ONLY");
            pStmt.setString(1, "%"+name+"%");
            pStmt.setInt(2, page*itemsPerPage);
            pStmt.setInt(3, itemsPerPage);
            ResultSet res = pStmt.executeQuery();
            while (res.next()) {
                ChannelData channelData = new ChannelData();
                channelData.setId(res.getInt("id_post"));
                channelData.setName(res.getString("denumire_post"));
                channelData.setStartDate(res.getDate("data_start").toString());
                Date endDate = res.getDate("data_end");
                if(endDate != null) {
                    channelData.setEndDate(endDate.toString());
                }
                channelData.setType(res.getString("tip"));
                PreparedStatement pStmt2 = con.prepareStatement("SELECT * FROM detalii_posturi WHERE id_post = ?");
                pStmt2.setInt(1, res.getInt("id_post"));
                ResultSet res2 = pStmt2.executeQuery();
                if(res2.next())
                {
                    channelData.setFrequency(res2.getDouble("frecventa"));
                    channelData.setChannel(res2.getInt("canal"));
                }
                channels.add(channelData);
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return channels;
    }

    void addChannel(ChannelData channel)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "INSERT INTO posturi VALUES(?,?,?,?,?)");
            int id = ChannelsDatabaseHandler.getInstance().getMaxID()+1;
            pStmt.setInt(1, id);
            pStmt.setString(2, channel.getNameProperty().getValue());
            String startDate = channel.getStartDateProperty().getValue();
            if(startDate!=null && startDate.length()!=0) {
                pStmt.setDate(3, DateFormatter.getDatabaseFormat(startDate));
            }
            else
            {
                pStmt.setDate(3, null);
            }
            String endDate = channel.getEndDateProperty().getValue();
            if(endDate != null && endDate.length()!=0) {
                pStmt.setDate(4, DateFormatter.getDatabaseFormat(endDate));
            }
            else
            {
                pStmt.setDate(4, null);
            }
            pStmt.setString(5, channel.getTypeProperty().getValue());
            pStmt.executeQuery();
            pStmt.close();
            PreparedStatement pStmt2 = con.prepareStatement( "INSERT INTO detalii_posturi VALUES(?,?,?)");
            pStmt2.setInt(1, id);
            pStmt2.setDouble(2, channel.getFrequencyProperty().getValue());
            pStmt2.setInt(3, channel.getChannelProperty().getValue());
            pStmt2.executeQuery();
            pStmt2.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    void updateChannel(ChannelData channel)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "UPDATE posturi SET denumire_post = ?," +
                    "data_start = ?," +
                    "data_end = ?," +
                    "tip = ?" +
                    "WHERE id_post = ?");
            pStmt.setString(1, channel.getNameProperty().getValue());
            String startDate = channel.getStartDateProperty().getValue();
            if(startDate!=null && startDate.length()!=0) {
                pStmt.setDate(2, DateFormatter.getDatabaseFormat(startDate));
            }
            else
            {
                pStmt.setDate(2, null);
            }
            String endDate = channel.getEndDateProperty().getValue();
            if(endDate != null && endDate.length()!=0) {
                pStmt.setDate(3, DateFormatter.getDatabaseFormat(endDate));
            }
            else
            {
                pStmt.setDate(3, null);
            }
            pStmt.setString(4, channel.getTypeProperty().getValue());
            pStmt.setInt(5, channel.getIdProperty().getValue());
            pStmt.executeQuery();
            pStmt.close();
            PreparedStatement pStmt2 = con.prepareStatement( "UPDATE detalii_posturi SET frecventa = ?," +
                    "canal = ?" +
                    "WHERE id_post = ?");
            pStmt2.setDouble(1, channel.getFrequencyProperty().getValue());
            pStmt2.setInt(2, channel.getChannelProperty().getValue());
            pStmt2.setInt(3, channel.getIdProperty().getValue());
            pStmt2.executeQuery();
            pStmt2.close();
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
            ResultSet res = stmt.executeQuery("SELECT MAX(id_post) FROM posturi");
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

    void removeChannel(int id)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM detalii_posturi WHERE id_post = ?");
            pStmt.setInt(1, id);
            pStmt.executeQuery();
            PreparedStatement pStmt2 = con.prepareStatement( "DELETE FROM posturi WHERE id_post = ?");
            pStmt2.setInt(1, id);
            pStmt2.executeQuery();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
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

    public static ChannelsDatabaseHandler getInstance() {
        return channelsDatabaseHandler;
    }
}