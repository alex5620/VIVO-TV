package sample.TVPackages;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.ChannelsPackage.ChannelData;
import sample.ChannelsPackage.Channels;
import sample.ChannelsPackage.ChannelsDatabaseHandler;

import java.util.ArrayList;

public class AddChannelsDialogueController {
    @FXML private TableView<ChannelData> channelsTable;
    @FXML private TableColumn<ChannelData, String> nameColumn, typeColumn;
    @FXML private TableColumn<ChannelData, Integer> channelColumn;
    @FXML private TableColumn<ChannelData, Double> frequencyColumn;

    void listAvailableChannels(TVPackageData packageData)
    {
        channelsTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        ObservableList<ChannelData> allChannels = FXCollections.observableArrayList(ChannelsDatabaseHandler.getInstance().getAllChannels());
        ArrayList<ChannelData> packageChannels = packageData.getAvailableChannels();
        ObservableList<ChannelData> availableChannels = FXCollections.observableArrayList();
        boolean found;
        for(ChannelData channel: allChannels)
        {
            found = false;
            for(ChannelData packageChannel: packageChannels)
            {
                if(channel.getIdProperty().getValue().equals(packageChannel.getIdProperty().getValue()))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                availableChannels.add(channel);
            }
        }
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        frequencyColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFrequencyProperty().getValue()).asObject());
        channelColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getChannelProperty().getValue()).asObject());
        if(availableChannels.size()>=14)
        {
            channelsTable.setPrefWidth(channelsTable.getPrefWidth()+12);
        }
        channelsTable.setItems(availableChannels);
    }

    void addSelectedChannels(TVPackageData packageData)
    {
        ObservableList<ChannelData> selectedChannels = channelsTable.getSelectionModel().getSelectedItems();
        PackagesDatabaseHandler.getInstance().addChannelsToPackage(selectedChannels, packageData.getIdProperty().getValue());
        for(ChannelData channel: selectedChannels)
        {
            packageData.addChannel(channel);
        }
    }
}
