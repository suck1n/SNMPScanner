package it.duck.gui;

import it.duck.gui.elements.CommunityTab;
import it.duck.gui.elements.IPField;
import it.duck.scanner.Scanner;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import org.soulwing.snmp.VarbindCollection;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GUI implements Initializable {

    public Button btn_Scan;
    public ComboBox<String> combo_IP;
    public IPField startAddress;
    public IPField endAddress;
    public TabPane resultsTabPane;
    // TODO Make Own ComboBox with CheckBoxes
    public ComboBox<CheckBox> combo_Community;
    public ComboBox<String> combo_Method;

    private final Map<String, CommunityTab> communityTabs = new HashMap<>();
    private static GUI INSTANCE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_IP.getItems().setAll("Host", "Network", "Custom-Range");
        combo_IP.getSelectionModel().select(0);

        CheckBox cb = new CheckBox("Public");
        cb.setSelected(true);
        combo_Community.getItems().setAll(cb, new CheckBox("Private"));
        combo_Community.getSelectionModel().select(0);

        combo_Method.getItems().addAll( "GetNext", "Get");
        combo_Method.getSelectionModel().select(0);

        INSTANCE = this;
    }


    public void on_ComboIP_Change() {
        String selected = combo_IP.getSelectionModel().getSelectedItem();
        startAddress.setMaskDisabled(!selected.equalsIgnoreCase("Network"));
        endAddress.setDisable(!selected.equalsIgnoreCase("Custom-Range"));

        endAddress.clear();
        startAddress.clear();
    }

    public void on_Scan_Click() {
        String firstIP = startAddress.getIP();
        int mask = startAddress.getMask();
        String endIP = endAddress.getIP();
        boolean useGet = combo_Method.getSelectionModel().getSelectedItem().equalsIgnoreCase("Get");

        if(endIP == null && mask == -1) {
            System.out.println("Scanning Host " + firstIP);
            Scanner.scanIP(firstIP, null, null, null, useGet);
        } else if(endIP == null) {
            System.out.println("Scanning Network " + firstIP + "/" + mask);
            Scanner.scanNetwork(firstIP, mask, null, null, null, useGet);
        } else {
            System.out.println("Scanning Range " + firstIP + " - " + endIP);
            Scanner.scanNetwork(firstIP, endIP, null, null, null, useGet);
        }
    }

    public void addResult(String community, String ip, VarbindCollection collection) {
        Platform.runLater(() -> {
            if(communityTabs.size() != resultsTabPane.getTabs().size()) {
                resultsTabPane.getTabs().clear();
                communityTabs.clear();
            }

            CommunityTab tab = communityTabs.getOrDefault(community, new CommunityTab(community));
            tab.addResult(ip, collection);

            if(!communityTabs.containsKey(community)) {
                communityTabs.put(community, tab);
                resultsTabPane.getTabs().add(tab);
            }
        });
    }

    public static GUI getInstance() {
        return INSTANCE;
    }
}
