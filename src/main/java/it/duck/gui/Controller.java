package it.duck.gui;

import it.duck.gui.elements.IPField;
import it.duck.sanner.Scanner;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button btn_Scan;
    public HBox endIPBox;
    public ComboBox<String> combo_IP;
    public IPField startAddress;
    public IPField endAddress;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_IP.getItems().setAll("Host", "Network", "Custom-Range");
        combo_IP.getSelectionModel().select(0);
    }


    public void on_ComboIP_Change() {
        String selected = combo_IP.getSelectionModel().getSelectedItem();
        startAddress.setMaskVisible(selected.equalsIgnoreCase("Network"));
        endAddress.setVisible(selected.equalsIgnoreCase("Custom-Range"));

        endAddress.clear();
        startAddress.clear();
    }

    public void on_Scan_Click() {
        String firstIP = startAddress.getIP();
        int mask = startAddress.getMask();
        String endIP = endAddress.getIP();

        if(endIP == null && mask == -1) {
            System.out.println("Scanning Host " + firstIP);
            Scanner.scanIP(firstIP, null, null, null);
        } else if(mask != -1) {
            System.out.println("Scanning Network " + firstIP + "/" + mask);
            Scanner.scanNetwork(firstIP, mask, null, null, null);
        } else {
            System.out.println("Scanning Range " + firstIP + " - " + endIP);
            Scanner.scanNetwork(firstIP, endIP, null, null, null);
        }
    }
}
