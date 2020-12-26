package sample.TVPackages;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.ChannelsPackage.ChannelData;

public class ShowChannelsDialogueController {
    @FXML private TableView<ChannelData> channelsTable;
    @FXML private TableColumn<ChannelData, String> nameColumn, typeColumn;
    @FXML private TableColumn<ChannelData, Integer> channelColumn;
    @FXML private TableColumn<ChannelData, Double> frequencyColumn;
    @FXML private Label textLabel;

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
        textLabel.setText("Pachetul "+packageData.getNameProperty().getValue());
        channelsTable.setItems(channels);
    }
}
