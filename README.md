# General
## Introduction
SNMP Scanner Tool welches in Java geschrieben wurde.

## Dependencies
Das Programm braucht lediglich nur die Bibliothek 
<a href="https://github.com/soulwingtnm4j">tnm4j</a>. <br>
Es wurde die Java Version 8 genutzt und die tnm4j Version 11.

## Installation/Start
Zum Installieren muss die `SNMPScanner.jar` Datei im Release Commit
heruntergeladen werden. Dann um das Programm zu starten muss Java 8 auf dem Rechner
installiert sein. Letztendlich genügt es die `SNMPScanner.jar` Datei mit einem 
Doppelklick zu starten oder den Befehl `java -jar SNMPScanner.jar` in der Konsole 
auszuführen.

# SNMP Scanner
##Status
Derzeit muss das Programm noch über den Code geändert werden. Die GUI 
kann deshalb an sich noch nichts ausführen.

### Start Scanning
Um einen Scan, für eine IP auszuführen muss man nur auf die Klasse
``Scanner`` zugreifen und die Methode ``scanIP`` aufrufen. Um ein ganzes
Netzwerk zu scannen, die Methode ``scanNetwork`` in der Klasse
``Scanner`` aufrufen.

### Trap Listener
Für einen Listener wird eine neue Instanz der Klasse ```SNMPListener```
erstellt. Um den Listener dann noch zu starten wird die ``start`` Methode
des Objekts aufgerufen. Der Listener kann dann wieder über die Methode
``stop`` gestoppt werden.
