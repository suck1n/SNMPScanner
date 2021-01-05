package it.duck.gui.elements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.controlsfx.control.CheckComboBox;

import java.util.ArrayList;

public class CommunityBox extends CheckComboBox<String> {

    private final IntegerProperty defaultSelectedIndex = new SimpleIntegerProperty(-1);
    private ObservableList<String> items = FXCollections.observableArrayList();

    public CommunityBox() {
        addListenerToItems();

        setTitle("Communities");
    }

    private void addListenerToItems() {
        items.addListener((ListChangeListener<String>) c -> {
            if(c.next()) {
                ArrayList<String> items = getSelectedCommunities();
                getCheckModel().clearChecks();

                if(c.wasAdded()) {
                    super.getItems().addAll(c.getAddedSubList());
                }
                if(c.wasRemoved()) {
                    super.getItems().removeAll(c.getRemoved());
                }

                if(c.getFrom() <= defaultSelectedIndex.get() && defaultSelectedIndex.get() <= c.getTo()) {
                    getCheckModel().check(defaultSelectedIndex.get());
                }

                for(String item : items) {
                    getCheckModel().check(item);
                }
            }
        });
    }

    public ArrayList<String> getSelectedCommunities() {
        return new ArrayList<>(getCheckModel().getCheckedItems());
    }



    public int getDefaultSelectedIndex() {
        return defaultSelectedIndex.get();
    }

    public void setDefaultSelectedIndex(int defaultSelectedIndex) {
        this.defaultSelectedIndex.set(defaultSelectedIndex);
    }

    @Override
    public ObservableList<String> getItems() {
        return items;
    }

    public void setItems(ObservableList<String> items) {
        this.items = items;

        addListenerToItems();
    }
}
