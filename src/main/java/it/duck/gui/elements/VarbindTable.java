package it.duck.gui.elements;

import it.duck.gui.elements.VarbindTable.VarbindValue;
import it.duck.gui.utility.TableUtility;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.soulwing.snmp.Varbind;

import java.util.Collection;

public class VarbindTable extends TableView<VarbindValue> {

    private boolean isDefault = true;

    public VarbindTable() {
        TableColumn<VarbindValue, String> col = new TableColumn<>("Kein Client ausgewählt");

        getColumns().add(col);

        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    }


    public void setVarbinds(Collection<Varbind> collection) {
        if(collection == null) {
            return;
        }

        if(isDefault) {
            setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);
            initializeTable();
        }

        getItems().clear();
        for(Varbind varbind : collection) {
            getItems().add(new VarbindValue(varbind));
        }
        TableUtility.autoFitTable(this);
    }

    private void initializeTable() {
        TableColumn<VarbindValue, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<VarbindValue, String> oidCol = new TableColumn<>("OID");
        oidCol.setCellValueFactory(new PropertyValueFactory<>("oid"));

        TableColumn<VarbindValue, String> valueCol = new TableColumn<>("Response");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        getColumns().clear();
        getColumns().add(nameCol);
        getColumns().add(oidCol);
        getColumns().add(valueCol);

        isDefault = false;
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
