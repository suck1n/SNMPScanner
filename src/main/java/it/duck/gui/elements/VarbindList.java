package it.duck.gui.elements;


import javafx.beans.value.ChangeListener;
import javafx.scene.control.ListView;

public class VarbindList extends ListView<String> {

    public VarbindList(ChangeListener<? super String> listener) {
        this.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void addIP(String ip) {
        getItems().add(ip);
    }

    public boolean containsIP(String ip) {
        return getItems().contains(ip);
    }
}
