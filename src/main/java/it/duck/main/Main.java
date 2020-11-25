package it.duck.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.soulwing.snmp.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            Mib mib = MibFactory.getInstance().newMib();
            mib.load("SNMPv2-MIB");

            SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
            target.setAddress("192.168.1.30");
            target.setCommunity("public");

            SnmpContext context = SnmpFactory.getInstance().newContext(target, mib);
            try {
                VarbindCollection result = context.getNext("sysUpTime").get();
                System.out.println("System-Up-Time: " + result.get("sysUpTime"));
            }
            finally {
                context.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
// launch(args);
