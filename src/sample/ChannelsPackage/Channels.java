package sample.ChannelsPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Channels {
    private static Channels channels = new Channels();
    private ObservableList<ChannelData> allChannels;

    private Channels() {}

    public void addData(int page, int itemsPerPage, String name)
    {
        allChannels = FXCollections.observableArrayList(ChannelsDatabaseHandler.getInstance().getChannels(page, itemsPerPage, name));
    }

    public static Channels getChannels() {
        return channels;
    }

    public ObservableList<ChannelData> getAllChannels() {
        return allChannels;
    }

    public void addChannel(ChannelData channelData)
    {
        allChannels.add(channelData);
    }

    public int getSize() {
        return allChannels.size();
    }

    public void removeChannel(ChannelData channel)
    {
        allChannels.remove(channel);
    }
}
