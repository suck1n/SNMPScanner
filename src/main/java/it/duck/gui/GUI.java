package it.duck.gui;

import it.duck.gui.elements.CommunityBox;
import it.duck.gui.elements.CommunityTab;
import it.duck.gui.elements.IPField;
import it.duck.gui.elements.Setting;
import it.duck.gui.utility.AlertUtility;
import it.duck.scanner.Scanner;
import it.duck.scanner.StandardSettings;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import org.soulwing.snmp.VarbindCollection;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GUI implements Initializable {

    public Button btn_Scan;
    public ComboBox<String> combo_IP;
    public IPField startAddress;
    public IPField endAddress;
    public TabPane resultsTabPane;
    public CommunityBox combo_Community;
    public ComboBox<String> combo_Method;
    public Setting settingMIBs;
    public Setting settingOIDs;
    public Setting settingCommunities;
    public TextArea txt_Console;

    private final Map<String, CommunityTab> communityTabs = new HashMap<>();
    private static GUI INSTANCE;

    // TODO
    //   - Integrate Functionality
    //   - Connect Settings Page with Scan Page

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_IP.getItems().setAll("Host", "Network", "Custom-Range");
        combo_IP.getSelectionModel().select(0);

        combo_Method.getItems().addAll( "GetNext", "Get");
        combo_Method.getSelectionModel().select(0);

        combo_Community.setItems(settingCommunities.getItems());

        settingMIBs.getItems().addAll(StandardSettings.getMIBs());
        settingOIDs.getItems().addAll(StandardSettings.getOIDs(null, false));
        settingCommunities.getItems().addAll(StandardSettings.getCommunities(null));

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
        String endIP = endAddress.getIP();
        int mask = startAddress.getMask();

        boolean useGet = combo_Method.getSelectionModel().getSelectedItem().equalsIgnoreCase("Get");

        List<String> mibs = settingMIBs.getItems();
        List<String> oids = settingOIDs.getItems();
        List<String> communities = combo_Community.getSelectedCommunities();

        if(communities.isEmpty()) {
            AlertUtility.showError("Es muss mindestens eine Community ausgewählt sein!");
            return;
        }

        if(endIP == null && mask == -1) {
            info("IP " + firstIP + " wird gescannt!");
            Scanner.scanIP(firstIP, mibs, oids, communities, useGet);
        } else if(endIP == null) {
            info("Netzwerk " + firstIP + "/" + mask + " wird gescannt!");
            Scanner.scanNetwork(firstIP, mask, mibs, oids, communities, useGet);
        } else {
            info("Bereich " + firstIP + " - " + endIP + " wird gescannt!");
            Scanner.scanNetwork(firstIP, endIP, mibs, oids, communities, useGet);
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

    public void log(String message) {
        Platform.runLater(() -> txt_Console.appendText(message + "\n"));
    }

    public void info(String message) {
        log("[INFO][" + getDateAsString() + "] " + message);
    }

    public void warning(String message) {
        log("[WARNUNG][" + getDateAsString() + "] " + message);
    }

    public void error(String message) {
        log("[FEHLER][" + getDateAsString() + "] " + message);
    }

    private String getDateAsString() {
        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm:ss");

        return dateTime.format(formatter);
    }

    public static GUI getInstance() {
        return INSTANCE;
    }
}
