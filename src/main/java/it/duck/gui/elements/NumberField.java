package it.duck.gui.elements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

public class NumberField extends TextField {

    private final IntegerProperty maxDigits = new SimpleIntegerProperty(Integer.MAX_VALUE);
    private final IntegerProperty minValue = new SimpleIntegerProperty(0);
    private final IntegerProperty maxValue = new SimpleIntegerProperty(Integer.MAX_VALUE);

    public NumberField(int maxDigits, int maxValue, int minValue, double prefWidth) {
        setMaxValue(maxValue);
        setMaxDigits(maxDigits);
        setMinValue(minValue);
        setPrefWidth(prefWidth);
        registerListener();
    }

    public NumberField() {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, -1);
    }

    private void registerListener() {
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxDigits.get()) {
                this.setText(newValue.substring(0, maxDigits.get()));
            } else {
                try {
                    if (!newValue.equals("")) {
                        int val = Integer.parseInt(newValue);
                        if(val > maxValue.get() || val < minValue.get()) {
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

    public void setMinValue(int minValue) {
        this.minValue.set(minValue);
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

    public int getMinValue() {
        return minValue.get();
    }

    public int getValue() {
        String text = getText().trim().isEmpty() ? getPromptText() : getText();
        try {
            return Integer.parseInt(text);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}