# Contents


1. [General](#general)
    * [Introduction](#introduction)
    * [Dependencies](#dependencies)
    * [Installation/Start](#installationstart)
2. [SNMP Scanner](#snmp-scanner)
    * [Status](#status)
    * [Start Scanning](#start-scanning)
    * [Trap Listener](#trap-listener)

# General

## Introduction
SNMP Scanner Tool welches in Java geschrieben wurde.

## Dependencies
Das Programm braucht lediglich nur die Bibliothek 
[tnm4j](https://github.com/soulwing/tnm4j). \
Es wurde die Java Version 8 genutzt und die tnm4j Version 11.

## Installation/Start
Zum Installieren muss die `SNMPScanner.jar` Datei im Release Commit
heruntergeladen werden. Dann um das Programm zu starten muss Java 8 auf dem Rechner
installiert sein. Letztendlich genügt es die `SNMPScanner.jar` Datei mit einem 
Doppelklick zu starten oder den Befehl `java -jar SNMPScanner.jar` in der Konsole 
auszuführen.

# SNMP Scanner
## Status
Derzeit muss der Großteil des Programms noch über den Code geändert werden.
Die GUI kann jedoch schon einen Host, ein Netzwerk oder eine Range scannen.
Der Output erfolgt immer noch in der Konsole. 

### Start Scanning
Um einen Scan, für eine IP auszuführen muss man nur auf die Klasse
``Scanner`` zugreifen und die Methode ``scanIP`` aufrufen. Um ein ganzes
Netzwerk zu scannen, die Methode ``scanNetwork`` in der Klasse
``Scanner`` aufrufen.

#### Example

```java
import it.duck.sanner.Scanner;

class ScannerExample {

    public static void main(String[] args) {
        // Die Resultate werden in der Konsole ausgeben
        scanHost();
        scanNetwork();
        scanRange();
    }

    private static void scanHost() {
        // Die zu scannende IP
        String ip = "192.168.0.1";
        // Können null sein, da die Funktion default Argumente besitzt
        Scanner.scanIP(ip, null, null, null);
    }
    
    private static void scanNetwork() {
        // Das zu scannende Netzwerk
        String ip = "192.168.0.0";
        // Die Maske des Netzwerkes
        int mask = 24;
        // Können null sein, da die Funktion default Argumente besitzt
        Scanner.scanNetwork(ip, mask, null, null, null);
    }
    
    private static void scanRange() {
        // Die IP bei der die Range anfängt (inklusiv)
        String startIP = "192.168.0.5";
        // Die IP bei der die Range aufhört (inklusiv)
        String endIP = "192.168.0.10";
        // Können null sein, da die Funktion default Argumente besitzt
        Scanner.scanNetwork(startIP, endIP, null, null, null);
    }
}
```

### Trap Listener
Für einen Listener wird eine neue Instanz der Klasse ```SNMPListener```
erstellt. Um den Listener dann noch zu starten wird die ``start`` Methode
des Objekts aufgerufen. Der Listener kann dann wieder über die Methode
``stop`` gestoppt werden. Damit Traps nicht nur vom eigenem Computer
gesendet werden können, muss noch eine Firewall Regel erstellt
werden. Und zwar eine eingehende Regel für UDP Protokolle, welche den 
Port für alle Netzwerke (Private, Öffentlich und Domäne) zulässt.

#### Example

```java

import it.duck.sanner.listener.SNMPListener;

class TrapListenerExample {

    private static int PORT = 10124;
    
    // Die Ergebnisse werden in der Konsole ausgegeben
    public static void main(String[] args) {
        // Der Listener wird auf den Port 10124 hören
        SNMPListener listener = new SNMPListener(PORT);
        // Der Handler wird registriert und somit startet der Listener
        listener.start();
        // Für das Beispiel wird 6 Sekunden lang gewartet
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            System.out.println("Sleep wurde unterbrochen!");
        } finally {
            // Zum Schluss muss der Listener noch gestoppt werden
            listener.stop();
        }
    }
}
```
