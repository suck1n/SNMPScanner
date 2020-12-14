package it.duck.gui.elements;

import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

@DefaultProperty("promptTexts")
public class IPField extends HBox {

    private final ArrayList<NumberField> numberFields;
    private NumberField maskField;

    private final ObservableList<String> promptTexts = FXCollections.observableArrayList();
    private final BooleanProperty maskVisible = new SimpleBooleanProperty(true);
    private final BooleanProperty maskDisabled = new SimpleBooleanProperty(false);

    public IPField() {
        numberFields = new ArrayList<>();
        this.setSpacing(2);
        initialize();
    }

    private void initialize() {
        NumberField lastElement = null;
        for(int i = 0; i < 4; i++) {
            lastElement = addElement(i != 0 ? "." : null, lastElement);
        }

        maskField = addElement(2, 32, 40,"/", lastElement);
        maskField.visibleProperty().bind(maskVisible);
        maskField.disableProperty().bind(maskDisabled);

        promptTexts.addListener((ListChangeListener<String>) c -> {
            for(int i = 0; i < c.getList().size(); i++) {
                if(i == numberFields.size()) {
                    return;
                }

                numberFields.get(i).setPromptText(c.getList().get(i));
            }
        });
    }


    private NumberField addElement(String delim, NumberField previous) {
        return addElement(3, 255, 42, delim, previous);
    }

    private NumberField addElement(int maxDigits, int maxValue, double width, String delimiter, NumberField previous) {
        if(delimiter != null) {
            Label l = new Label(delimiter);
            l.setPrefHeight(26 + (delimiter.equals(".") ? 12 : 0));
            l.setStyle(delimiter.equals(".") ? "" : "-fx-font-size: 24px");
            if(delimiter.equals("/")) {
                l.visibleProperty().bind(maskVisible);
                l.disableProperty().bind(maskDisabled);
            }

            this.getChildren().add(l);
        }

        NumberField n = new NumberField(maxDigits, maxValue, width);

        if(delimiter == null || delimiter.equals(".")) {
            numberFields.add(n);
        }
        this.getChildren().add(n);

        if(previous != null) {
            previous.setOnKeyPressed((event) -> {
                if(event.getCode() == KeyCode.PERIOD) {
                    n.requestFocus();
                    Platform.runLater(n::selectAll);
                }
            });
        }

        return n;
    }


    public void clear() {
        for(NumberField n : numberFields) {
            n.clear();
        }
        maskField.clear();
    }


    public String getIP() {
        StringBuilder ip = new StringBuilder();

        for (NumberField n : numberFields) {
            int val = n.getValue();
            if (val != -1) {
                ip.append(val).append(".");
            } else {
                return null;
            }
        }

        ip.deleteCharAt(ip.length() - 1);

        return ip.toString();
    }

    public int getMask() {
        return maskField.getValue();
    }




    public final boolean isMaskVisible() {
        return maskVisible.get();
    }

    public final void setMaskVisible(boolean hasMask) {
        this.maskVisible.set(hasMask);
    }

    public boolean isMaskDisabled() {
        return maskDisabled.get();
    }

    public void setMaskDisabled(boolean maskDisabled) {
        this.maskDisabled.set(maskDisabled);
    }

    public ObservableList<String> getPromptTexts() {
        return promptTexts;
    }
}
