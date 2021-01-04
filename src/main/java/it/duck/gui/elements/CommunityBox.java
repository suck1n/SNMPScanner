package it.duck.gui.elements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.controlsfx.control.CheckComboBox;

import java.util.List;

public class CommunityBox extends CheckComboBox<String> {

    private final IntegerProperty defaultSelectedIndex = new SimpleIntegerProperty(-1);

    public CommunityBox() {
        defaultSelectedIndex.addListener((observable, oldValue, newValue) -> {
            int newVal = newValue.intValue();
            int oldVal = oldValue.intValue();

            if(newVal >= 0 && newVal < getItems().size()) {
                getCheckModel().check(newVal);

                getItemBooleanProperty(newVal).addListener((obs, old, newV) -> getCheckModel().check(newVal));
            }

            if(oldVal >= 0 && oldVal < getItems().size()) {
                getCheckModel().clearCheck(oldVal);
            }
        });

        setTitle("Communities");
    }

    public List<String> getSelectedCommunities() {
        return getCheckModel().getCheckedItems();
    }



    public int getDefaultSelectedIndex() {
        return defaultSelectedIndex.get();
    }

    public void setDefaultSelectedIndex(int defaultSelectedIndex) {
        this.defaultSelectedIndex.set(defaultSelectedIndex);
    }
}
