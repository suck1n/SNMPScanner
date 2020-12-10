package it.duck.gui.elements;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.soulwing.snmp.Varbind;
import org.soulwing.snmp.VarbindCollection;

public class VarbindTable extends TableView<VarbindTable.VarbindValue> {


    public VarbindTable() {
        this(null);
    }

    public VarbindTable(VarbindCollection collection) {
        TableColumn<VarbindValue, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<VarbindValue, String> oidCol = new TableColumn<>("OID");
        oidCol.setCellValueFactory(new PropertyValueFactory<>("oid"));

        TableColumn<VarbindValue, String> valueCol = new TableColumn<>("Response");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        getColumns().add(nameCol);
        getColumns().add(oidCol);
        getColumns().add(valueCol);

        setVarbinds(collection);
    }


    public void setVarbinds(VarbindCollection collection) {
        if(collection != null) {
            getItems().clear();
            for(Varbind varbind : collection) {
                getItems().add(new VarbindValue(varbind));
            }
        }
    }

    protected static class VarbindValue {

        private final String name;
        private final String oid;
        private final String value;

        public VarbindValue(Varbind varbind) {
            this.name = varbind.getName();
            this.oid = varbind.getOid();
            this.value = varbind.asString();
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getOid() {
            return oid;
        }
    }
}
