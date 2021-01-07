# Contents


1. [General](#general)
    * [Introduction](#introduction)
    * [Dependencies](#dependencies)
    * [Installation/Start](#installationstart)
2. [SNMP Scanner](#snmp-scanner)
    * [Status](#status)
    * [Planned](#planned) 
    * [Start Scanning](#start-scanning)
    * [Trap Listener](#trap-listener)

# General

## Introduction
SNMP Scanner Tool welches in Java geschrieben wurde.

## Dependencies
Das Programm braucht lediglich die Bibliothek
[tnm4j](https://github.com/soulwing/tnm4j) und [controlsfx](https://github.com/controlsfx/controlsfx).

### Versions
 - Java: 1.8.0_172
 - tnm4j: 1.0.11
 - controlsfx: 8.40.18


## Installation/Start
Zum Installieren muss die `SNMPScanner.jar` Datei im Release Commit
heruntergeladen werden. Dann um das Programm zu starten muss Java 8 auf dem Rechner
installiert sein. Letztendlich genügt es die `SNMPScanner.jar` Datei mit einem
Doppelklick zu starten oder den Befehl `java -jar SNMPScanner.jar` in der Konsole
auszuführen. Falls man die Source Files selbst kompilieren möchte, kann man das
über Maven machen, und zwar mit dem Befehl `mvn package`. Falls alles funktioniert
hat sollte man im Ordner `target` zwei Jar-Dateien finden mit den Namen
`SNMPScanner-1.0.jar` und `SNMPScanner-1.0-jar-with-dependencies.jar`.
Die Jar-Datei mit den Dependencies `SNMPScanner-1.0-jar-with-dependencies.jar`,
kann dann über den Befehl `java -jar SNMPScanner-1.0-jar-with-dependencies.jar` ausgeführt werden.

# SNMP Scanner
## Status
Das Programm ist komplett. Das heißt, dass alles nötige über die GUI geändert und
genutzt werden kann. 


## Planned
Alle nötigen Ziele wurden erreicht. Jedoch wird noch an kleinen Extrafeatures gearbeitet, wie zum Beispiel
die Community aus einer Trap zu lesen.

## Start Scanning
Um einen Scan, für eine IP auszuführen muss man im Feld die IP des
Hosts eintragen und dann die Methode auswählen, also `Get` oder `GetNext`.
Per Klick auf Scan wird schon eine Anfrage geschickt. Falls eine Request an
ein ganzes Netzwerk oder nur an eine Range geschickt werden sollte, kann
das in der ComboBox links geändert werden. In der ComboBox `Communities` können
jeweils Communities aktiviert bzw. deaktiviert werden.

### Examples

#### GUI

![GUI of the Scanner](src/main/resources/images/snmp_scanner_results.png)

#### Host, Network or Range

![Scan Type](src/main/resources/images/snmp_scanner_host.png)

#### Method

![Scan Method](src/main/resources/images/snmp_scanner_method.png)

#### Communities

![Communities](src/main/resources/images/snmp_scanner_communities.png)

## Trap Listener
Um den Listener zu starten, muss die CheckBox aktiviert werden. Vorher kann man
noch darunter den zu nutzenden Port deklarieren, falls das Textfield leer bleibt
wird der Standardport 162 benutzt. Damit Traps nicht nur vom eigenem Computer
gesendet werden können, muss noch eine Firewall Regel erstellt
werden. Und zwar eine eingehende Regel für UDP Protokolle, welche den
Port für alle Netzwerke (Private, Öffentlich und Domäne) zulässt.

### Testing

Um selbst eine Trap zu senden, kann man das über einen Befehl auf einem Linuxsystem
erreichen. Ein Beispiel dafür wäre:

![SNMP Trap Command](src/main/resources/images/console_trap.png)

### Example

![SNMP Trap Result](src/main/resources/images/snmp_scanner_trap_listener.png)

## Settings

![SNMP Settings](src/main/resources/images/snmp_scanner_settings.png)