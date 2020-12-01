package sample.ChannelsPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

public class Channels {
    private static Channels channels = new Channels();
    private ObservableList<ChannelData> allChannels;

    private Channels() {
        addData();
    }

    private void addData() {
        allChannels = FXCollections.observableArrayList();
        for (int i = 1; i <= 34; i++) {
            ChannelData dataRow = new ChannelData();
            Integer value1 = i;
            String value2 = "PRO TV" + i;
            String value3 = "12/20/200" + i;
            String value4 = "12/25/200" + i;
            String value5 = "D";
            Random r = new Random();
            Double value6 = 100 + (8000 - 100) * r.nextDouble();
            Integer value7 = i;
            dataRow.setId(value1);
            dataRow.setName(value2);
            dataRow.setStartDate(value3);
            dataRow.setEndDate(value4);
            dataRow.setType(value5);
            dataRow.setFrequency(value6);
            dataRow.setChannel(value7);
            allChannels.add(dataRow);
        }
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
