# General
## Introduction
Einfaches SNMP Scanner Tool welches in Java geschrieben wurde.

## Dependencies
Das Programm braucht lediglich nur die Bibliothek 
<a href="https://github.com/soulwingtnm4j">tnm4j</a>. <br>
Es wurde die Java Version 8 genutzt und die tnm4j Version 11.

## Installation
Zum Installieren bzw. starten des Programmes muss Java 8 auf dem Rechner
installiert sein und dann genügt es die `Scanner.jar` auszuführen.

# SNMP Scanner
##Status
Derzeit muss das Programm noch über den Code geändert werden. Die GUI 
kann deshalb an sich noch nichts ausführen.

### Start Scanning
Um einen Scan, für eine IP auszuführen muss man einfach nur auf die Klasse
``Scanner`` zugreifen und die Methode ``scanIP`` aufrufen. Um ein ganzes
Netzwerk zu scannen, einfach die Methode ``scanNetwork`` in der Klasse
``Scanner`` aufrufen.

### Trap Listener
Für einen Listener wird einfach eine neue Instanz der Klasse ```SNMPListener```
erstellt. Um den Listener dann noch zu starten wird die ``start`` Methode
des Objekts aufgerufen. Der Listener kann dann wieder über die Methode
``stop`` gestoppt werden.
