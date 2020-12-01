package sample.TVPackages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.ChannelsPackage.Channels;

public class Packages {
    private static Packages packages = new Packages();
    private ObservableList<TVPackageData> allPackages;

    private Packages() {
        addData();
    }

    private void addData() {
        allPackages = FXCollections.observableArrayList();
        for (int i = 1; i <= 5; i++) {
            TVPackageData dataRow = new TVPackageData();
            Integer value1 = i;
            String value2 = "Pachet" + i;
            String value3 = "12/20/200" + i;
            String value4 = "12/25/200" + i;
            Double value5 = 10*(double)i/2-i;
            dataRow.setID(value1);
            dataRow.setName(value2);
            dataRow.setStartDate(value3);
            dataRow.setEndDate(value4);
            dataRow.setPrice(value5);
            dataRow.addChannel(Channels.getChannels().getAllChannels().get((int)(Math.random()*20)));
            dataRow.addChannel(Channels.getChannels().getAllChannels().get((int)(Math.random()*10)+20));
            allPackages.add(dataRow);
        }
    }

    public static Packages getPackages() {
        return packages;
    }

    public int getSize() {
        return allPackages.size();
    }

    public void addPackage(TVPackageData packageData)
    {
        allPackages.add(packageData);
    }

    public void removePackage(TVPackageData packageData)
    {
        allPackages.remove(packageData);
    }

    public ObservableList<TVPackageData> getAllPackages() {
        return allPackages;
    }
}
