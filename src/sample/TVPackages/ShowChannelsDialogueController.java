package sample.TVPackages;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.ChannelsPackage.ChannelData;

public class ShowChannelsDialogueController {
    @FXML TableView<ChannelData> channelsTable;
    @FXML TableColumn<ChannelData, String> nameColumn, typeColumn;
    @FXML private TableColumn<ChannelData, Integer> channelColumn;
    @FXML TableColumn<ChannelData, Double> frequencyColumn;

    void showInfo(TVPackageData packageData)
    {
        ObservableList<ChannelData> channels = FXCollections.observableArrayList(packageData.getAvailableChannels());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        frequencyColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFrequencyProperty().getValue()).asObject());
        channelColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getChannelProperty().getValue()).asObject());
        if(channels.size()>=13)
        {
            channelsTable.setPrefWidth(channelsTable.getPrefWidth()+12);
        }
        channelsTable.setItems(channels);
    }
}
