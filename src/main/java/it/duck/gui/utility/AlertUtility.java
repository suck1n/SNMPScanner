package it.duck.gui.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class AlertUtility {


    public static Optional<ButtonType> showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.setGraphic(null);

        return alert.showAndWait();
    }

    public static Optional<ButtonType> showError(String content) {
        return showAlert(Alert.AlertType.ERROR, "Error", content);
    }

    public static Optional<ButtonType> showInformation(String content) {
        return showAlert(Alert.AlertType.INFORMATION, "Information", content);
    }

    public static Optional<String> showInput(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);

        return dialog.showAndWait();
    }
}
