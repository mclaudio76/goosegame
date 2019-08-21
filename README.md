# Gioco dell'oca

#### Requisiti:
Java JDK 1.8+ , Maven 3.0+


#### Per compilare il programma

mvn compile

#### Esecuzione dei test

mvn test

#### Esecuzione partita interattiva

mvn exec:java


#### Esecuzione partita demo (il programma gioca da solo, usando 3 utenti [PIPPO,PLUTO,PAPERINO])

mvn exec:java -Dexec.args="DEMO"


### Note implementative

Rispetto alle specifiche si è aggiunto un ulteriore comando , QUIT, per forzare l'uscita da un gioco
in corso.

Ho cercato di mantenere il più possibile aderente alle specifiche l'output testuale che viene emesso,
in alcuni casi ho preferito spezzare l'output su più righe (soprattutto per rendere più semplice
la parte di concatenazione dei messaggi a video).









