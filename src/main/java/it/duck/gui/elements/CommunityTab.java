package it.duck.gui.elements;

import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.soulwing.snmp.VarbindCollection;

import java.util.HashMap;
import java.util.Map;

public class CommunityTab extends Tab {

    private final VarbindTable table;
    private final VarbindList list;

    private final Map<String, VarbindCollection> collections;

    public CommunityTab(String community) {
        super(community);

        this.collections = new HashMap<>();

        this.table = new VarbindTable();
        this.list = new VarbindList((observable, oldValue, newValue) -> {
            VarbindCollection col = collections.get(newValue);
            table.setVarbinds(col);
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(list, table);
        HBox.setHgrow(table, Priority.ALWAYS);

        this.setContent(hBox);
    }

    public void addResult(String ip, VarbindCollection collection) {
        list.addIP(ip);
        collections.put(ip, collection);
    }
}
