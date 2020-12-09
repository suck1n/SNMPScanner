package it.duck.gui.elements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

public class NumberField extends TextField {

    private final IntegerProperty maxDigits = new SimpleIntegerProperty();
    private final IntegerProperty maxValue = new SimpleIntegerProperty();

    public NumberField(int maxDigits, int maxValue, double prefWidth) {
        setMaxValue(maxValue);
        setMaxDigits(maxDigits);
        setPrefWidth(prefWidth);
        registerListener();
    }

    public NumberField() {
        this(0, 0, -1);
    }

    private void registerListener() {
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxDigits.get()) {
                this.setText(newValue.substring(0, maxDigits.get()));
            } else {
                try {
                    if (!newValue.equals("")) {
                        int val = Integer.parseInt(newValue);
                        if(val > maxValue.get()) {
                            throw new NumberFormatException();
                        }
                    }
                    this.setText(newValue);
                } catch (NumberFormatException e) {
                    this.setText(oldValue);
                }
            }
        });
    }

    public final void setMaxDigits(int maxDigits) {
        this.maxDigits.set(maxDigits);
    }

    public final void setMaxValue(int maxValue) {
        this.maxValue.set(maxValue);
    }

    public final int getMaxDigits() {
        return this.maxDigits.get();
    }

    public final int getMaxValue() {
        return this.maxValue.get();
    }

    public int getValue() {
        String text = getText();
        try {
            return Integer.parseInt(text);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}