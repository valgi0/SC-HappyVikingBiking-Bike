# SC-HappyVikingBiking-Bike
Progetto per l'esame di Smart City e Tecnologie mobili, AA 2018-2019.

## Guida all'utilizzo

Il jar eseguibile può essere generato via task shadowJar di Gradle
Prima dell'esecuzione del jar, occorre creare un link simbolico alla porta seriale su raspberry con il comando "sudo ln -s /dev/ttyACM0 /dev/ttyS80" 
Sono disponibili diversi paramateri per la configurazione iniziale della rastrelliera, specificabili in fase di avvio del Jar: 

* --remoteaddress:Specifica l'ip remoto del server da contattare
* --remoteport:Specifica la porta remota del server da contattare
* --rackport: Specifica la porta su cui il server della rack è in esecuzione
* --rackaddress: Specifica l'indirizzo IP su cui è in esecuzione il server della rack
* --bikeid: Specifica l'id della bicicletta
* --serialaddress: Specifica il path della porta seriale
* --serialrate: Specifica la frequenza della seriale
