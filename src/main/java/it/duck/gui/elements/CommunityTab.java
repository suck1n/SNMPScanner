package it.duck.gui.elements;

import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.soulwing.snmp.Varbind;
import org.soulwing.snmp.VarbindCollection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommunityTab extends Tab {

    private final VarbindTable table;
    private final VarbindList list;

    private final Map<String, Map<String, Varbind>> collections;

    public CommunityTab(String community) {
        super(community);

        this.collections = new HashMap<>();

        this.table = new VarbindTable();
        this.list = new VarbindList((observable, oldValue, newValue) -> {
            if(newValue == null) {
                return;
            }
            Collection<Varbind> col = collections.get(newValue).values();
            table.setVarbinds(col);
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(list, table);
        HBox.setHgrow(table, Priority.ALWAYS);

        this.setContent(hBox);
    }

    public void addResult(String ip, VarbindCollection collection) {
        Map<String, Varbind> col = new HashMap<>();
        Map<String, Varbind> oldCollection = collections.getOrDefault(ip, collection.asMap());
        Map<String, Varbind> newCollection = collection.asMap();

        for(String key : oldCollection.keySet()) {
            col.put(key, oldCollection.get(key));
        }
        for(String key : newCollection.keySet()) {
            col.put(key, newCollection.get(key));
        }

        collections.put(ip, col);

        if(!list.containsIP(ip)) {
            list.addIP(ip);
        } else {
            list.updateIP(ip);
        }
    }
}
