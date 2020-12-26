package sample.ChannelsPackage;

import sample.DateFormatter;

import java.sql.*;
import java.util.ArrayList;

public class ChannelsDatabaseHandler {
    private static ChannelsDatabaseHandler channelsDatabaseHandler = new ChannelsDatabaseHandler();
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

    private void setAutocommit(boolean val)
    {
        try {
            con.setAutoCommit(val);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void doCommit()
    {
        try {
            Statement stmt = con.createStatement();
            stmt.executeQuery("COMMIT");
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doRollback()
    {
        try {
            Statement stmt = con.createStatement();
            stmt.executeQuery("ROLLBACK");
            stmt.close();
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
                Date startDate = res.getDate("data_start");
                if(startDate != null) {
                    channelData.setStartDate(startDate.toLocalDate().format(DateFormatter.formatter));
                }
                Date endDate = res.getDate("data_end");
                if(endDate != null) {
                    channelData.setEndDate(endDate.toLocalDate().format(DateFormatter.formatter));
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
        setAutocommit(false);
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
            doCommit();
        } catch (Exception e) {
            e.printStackTrace();
            doRollback();
            ChannelsDatabaseErrorChecker.getInstance().checkError(e.getMessage());
        } finally {
            setAutocommit(true);
            closeConnection();
        }
    }

    void updateChannel(ChannelData channel)
    {
        getConnection();
        setAutocommit(false);
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
            doCommit();
        } catch (Exception e) {
            e.printStackTrace();
            doRollback();
            ChannelsDatabaseErrorChecker.getInstance().checkError(e.getMessage());
        } finally {
            setAutocommit(true);
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
       removeFromChannelsDetails(id);
       removeFromChannels(id);
    }

    private void removeFromChannelsDetails(int id)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM detalii_posturi WHERE id_post = ?");
            pStmt.setInt(1, id);
            pStmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void removeFromChannels(int id)
    {
        getConnection();
        try {
            PreparedStatement pStmt = con.prepareStatement( "DELETE FROM posturi WHERE id_post = ?");
            pStmt.setInt(1, id);
            pStmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public ArrayList<ChannelData> getAllChannels() {
        getConnection();
        ArrayList<ChannelData> channels = null;
        try {
            channels = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM posturi");
            while (res.next()) {
                ChannelData channelData = new ChannelData();
                channelData.setId(res.getInt("id_post"));
                channelData.setName(res.getString("denumire_post"));
                Date startDate = res.getDate("data_start");
                if(startDate != null) {
                    channelData.setStartDate(startDate.toLocalDate().format(DateFormatter.formatter));
                }
                Date endDate = res.getDate("data_end");
                if(endDate != null) {
                    channelData.setEndDate(endDate.toLocalDate().format(DateFormatter.formatter));
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
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return channels;
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
