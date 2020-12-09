package it.duck.gui.elements;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class IPField extends HBox {

    private final ArrayList<NumberField> numberFields;

    private final ObservableList<String> promptTexts = FXCollections.observableArrayList();
    private final BooleanProperty maskEnabled = new SimpleBooleanProperty();

    public IPField() {
        numberFields = new ArrayList<>();
        this.setSpacing(2);
        initialize();
    }

    private void initialize() {
        for(int i = 0; i < 4; i++) {
            if (i != 0) {
                Label l = new Label(".");
                l.setPrefHeight(38);
                this.getChildren().add(l);
            }
            NumberField n = new NumberField(3, 255, 42);

            numberFields.add(n);
            this.getChildren().add(n);
        }

        maskEnabled.addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                NumberField n = new NumberField(2, 32, 35);
                n.setVisible(false);
                if(promptTexts.size() == 5 && promptTexts.get(4) != null) {
                    n.setPromptText(promptTexts.get(4));
                }

                Label l = new Label("/");
                l.setPrefHeight(26);
                l.setStyle("-fx-font-size: 24px");
                l.setVisible(false);

                numberFields.add(n);
                this.getChildren().add(l);
                this.getChildren().add(n);
            } else if(numberFields.size() == 5) {
                NumberField n = numberFields.get(4);

                this.getChildren().remove(n);
                this.getChildren().remove(this.getChildren().size() - 1);
                numberFields.remove(n);
            }
        });

        promptTexts.addListener((ListChangeListener<String>) c -> {
            for(int i = 0; i < c.getList().size(); i++) {
                if(i == numberFields.size()) {
                    return;
                }

                numberFields.get(i).setPromptText(c.getList().get(i));
            }
        });
    }


    public String getIP() {
        StringBuilder ip = new StringBuilder();

        for(int i = 0; i < 4; i++) {
            NumberField n = numberFields.get(i);
            int val = n.getValue();
            if(val != -1) {
                ip.append(val).append(".");
            } else {
                return null;
            }
        }

        ip.deleteCharAt(ip.length() - 1);

        return ip.toString();
    }

    public void clear() {
        for(NumberField n : numberFields) {
            n.clear();
        }
    }

    public int getMask() {
        if(isMaskEnabled()) {
            return numberFields.get(4).getValue();
        }
        return -1;
    }

    public void setMaskVisible(boolean visible) {
        if(isMaskEnabled()) {
            NumberField n = numberFields.get(4);
            n.setVisible(visible);

            Label l = (Label)this.getChildren().get(this.getChildren().size() - 2);
            l.setVisible(visible);
        }
    }

    public boolean getMaskVisible() {
        return numberFields.get(4).isVisible();
    }


    public final boolean isMaskEnabled() {
        return maskEnabled.get();
    }

    public final void setMaskEnabled(boolean hasMask) {
        this.maskEnabled.set(hasMask);
    }

    public ObservableList<String> getPromptTexts() {
        return promptTexts;
    }
}
