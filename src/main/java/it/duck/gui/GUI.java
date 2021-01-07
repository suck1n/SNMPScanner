package it.duck.gui;

import it.duck.gui.elements.*;
import it.duck.gui.utility.AlertUtility;
import it.duck.scanner.Scanner;
import it.duck.scanner.StandardSettings;
import it.duck.scanner.listener.SNMPListener;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.soulwing.snmp.VarbindCollection;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GUI implements Initializable {

    public TabPane tabPane;
    public Button btn_Scan;
    public ComboBox<String> combo_IP;
    public IPField startAddress;
    public IPField endAddress;
    public TabPane scannerResultsTabPane;
    public TabPane listenerResultsTabPane;
    public CommunityBox combo_Community;
    public ComboBox<String> combo_Method;
    public Setting settingMIBs;
    public Setting settingOIDs;
    public Setting settingCommunities;
    public TextArea txt_Console;
    public TextArea txt_ListenerConsole;
    public CheckBox cb_Listener;
    public NumberField txt_Port;

    private final Map<String, CommunityTab> scannerCommunityTabs = new HashMap<>();
    private final Map<String, CommunityTab> listenerCommunityTabs = new HashMap<>();
    private final SNMPListener listener = new SNMPListener();
    private static GUI INSTANCE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_IP.getItems().setAll("Host", "Network", "Custom-Range");
        combo_IP.getSelectionModel().select(0);

        combo_Method.getItems().addAll( "GetNext", "Get");
        combo_Method.getSelectionModel().select(0);
        combo_Method.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            List<String> oldOids = StandardSettings.getOIDs(null, oldValue.equalsIgnoreCase("Get"));
            List<String> newOids = StandardSettings.getOIDs(null, newValue.equalsIgnoreCase("Get"));

            for(int i = 0; i < oldOids.size(); i++) {
                String oldOid = oldOids.get(i);
                String newOid = newOids.get(i);

                if(settingOIDs.getItems().contains(oldOid)) {
                    settingOIDs.getItems().remove(oldOid);
                    settingOIDs.getItems().add(newOid);
                }
            }
        });

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

        List<String> mibs = getSettingMIBs();
        List<String> oids = getSettingOIDs();
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

    public void on_Listener_Change() {
        if(cb_Listener.isSelected()) {
            listener.changePort(txt_Port.getValue() == -1 ? 162 : txt_Port.getValue());
            listener.start();
        } else {
            listener.stop();
        }
        txt_Port.setDisable(!txt_Port.isDisabled());
    }


    public void addScannerResult(String community, String ip, VarbindCollection collection) {
        addResult(scannerCommunityTabs, scannerResultsTabPane, community, ip, collection);
    }

    public void addListenerResult(String community, String ip, VarbindCollection collection) {
        addResult(listenerCommunityTabs, listenerResultsTabPane, community, ip, collection);
    }

    private void addResult(Map<String, CommunityTab> tabs, TabPane pane, String community, String ip, VarbindCollection collection) {
        Platform.runLater(() -> {
            if(tabs.size() != pane.getTabs().size()) {
                tabs.clear();
                pane.getTabs().clear();
            }

            CommunityTab tab = tabs.getOrDefault(community, new CommunityTab(community));
            tab.addResult(ip, collection);

            if(!tabs.containsKey(community)) {
                tabs.put(community, tab);
                pane.getTabs().add(tab);
            }
        });
    }


    public void log(String message) {
        Platform.runLater(() -> {
            if(tabPane.getSelectionModel().getSelectedItem() != null) {
                String text = tabPane.getSelectionModel().getSelectedItem().getText();
                if(text.equalsIgnoreCase("Scan")) {
                    txt_Console.appendText(message + "\n");
                } else if(text.equalsIgnoreCase("Trap Server")) {
                    txt_ListenerConsole.appendText(message + "\n");
                }
            }
        });
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


    public List<String> getSettingMIBs() {
        return new ArrayList<>(settingMIBs.getItems());
    }

    public List<String> getSettingOIDs() {
        return new ArrayList<>(settingOIDs.getItems());
    }

    public List<String> getSettingCommunities() {
        return new ArrayList<>(settingCommunities.getItems());
    }


    public static GUI getInstance() {
        return INSTANCE;
    }
}
