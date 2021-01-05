package it.duck.gui.elements;

import it.duck.gui.utility.AlertUtility;
import javafx.beans.DefaultProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

@DefaultProperty("items")
public class Setting extends VBox {

    private final StringProperty title = new SimpleStringProperty();
    private final ObservableList<String> items = FXCollections.observableArrayList();


    public Setting() {
        Label lbl_Title = new Label();
        lbl_Title.textProperty().bind(title);

        ListView<String> list = new ListView<>();
        list.setEditable(true);
        list.setCellFactory(TextFieldListCell.forListView());
        list.setItems(items);
        list.setContextMenu(getContextMenuForList(list));

        list.addEventFilter(MouseEvent.MOUSE_PRESSED, (e) -> {
            String text = null;

            if(e.getTarget() instanceof TextFieldListCell<?>) {
                TextFieldListCell<?> cell = (TextFieldListCell<?>) e.getTarget();
                text = cell.getText();
            } else if(e.getTarget() instanceof Text) {
                Text t = (Text)e.getTarget();
                text = t.getText();
            }

            if(text != null) {
                if(list.getSelectionModel().getSelectedItem() != null && list.getSelectionModel().getSelectedItem().equals(text)) {
                    e.consume();
                }
            }
        });

        list.setOnEditCommit((e) -> {
            if(e.getNewValue().trim().isEmpty()) {
                e.consume();
            } else {
                int index = e.getIndex();
                List<String> items = list.getItems();

                if (index >= 0 && index < items.size()) {
                    items.set(index, e.getNewValue());
                }
            }
        });

        HBox buttons = getButtons(list);

        getChildren().addAll(lbl_Title, list, buttons);

        setSpacing(10);
    }


    private HBox getButtons(ListView<String> list) {
        HBox buttons = new HBox();
        buttons.setSpacing(20);

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("settings-button");
        addButton.setOnAction((e) -> {
            Optional<String> result = AlertUtility.showInput(title.get(), "New Item: ");

            result.ifPresent((res) -> {
                if(!items.contains(res)) {
                    items.add(res);
                } else {
                    AlertUtility.showError("Es gibt bereits einen Eintrag mit diesem Wert!");
                }
            });
        });


        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("settings-button");
        editButton.setOnAction((e) -> {
            if(list.getSelectionModel().getSelectedItem() != null) {
                list.edit(list.getSelectionModel().getSelectedIndex());
            } else {
                AlertUtility.showInformation("Ein Eintrag muss ausgewählt sein!");
            }
        });


        Button removeButton = new Button("Remove");
        removeButton.getStyleClass().add("settings-button");
        removeButton.setOnAction((e) -> {
            if(list.getSelectionModel().getSelectedItem() != null) {
                items.remove(list.getSelectionModel().getSelectedItem());
            } else {
                AlertUtility.showInformation("Ein Eintrag muss ausgewählt sein!");
            }
        });

        buttons.getChildren().addAll(addButton, editButton, removeButton);

        return buttons;
    }


    private ContextMenu getContextMenuForList(ListView<String> list) {
        ContextMenu menu = new ContextMenu();

        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(new MenuItem("Add"));
        menu.getItems().add(new MenuItem("Edit"));
        menu.getItems().add(new MenuItem("Remove"));
        menu.getItems().add(new SeparatorMenuItem());

        menu.setOnAction((event) -> {
            if(event.getTarget() instanceof MenuItem) {
                MenuItem item = (MenuItem) event.getTarget();
                String text = item.getText();

                switch (text) {
                    case "Add":
                        Optional<String> result = AlertUtility.showInput(title.get(), "New Item: ");

                        result.ifPresent((res) -> {
                            if(!items.contains(res)) {
                                items.add(res);
                            } else {
                                AlertUtility.showError("Es gibt bereits einen Eintrag mit diesem Wert!");
                            }
                        });
                        break;
                    case "Edit":
                        if(list.getSelectionModel().getSelectedItem() != null) {
                            list.edit(list.getSelectionModel().getSelectedIndex());
                        } else {
                            AlertUtility.showInformation("Ein Eintrag muss ausgewählt sein!");
                        }
                        break;
                    case "Remove":
                        if(list.getSelectionModel().getSelectedItem() != null) {
                            items.remove(list.getSelectionModel().getSelectedItem());
                        } else {
                            AlertUtility.showInformation("Ein Eintrag muss ausgewählt sein!");
                        }
                        break;
                }
            }
        });

        return menu;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public ObservableList<String> getItems() {
        return items;
    }
}
