package goosegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/***
 * Classe principale / entry point del programma.
 * Implementa una versione semplificata, puramente text-based, del gioco dell'oca
 * che si attiene alle regole fornite dal testo dell'esercizio.
 */
public class GooseGameApplication {
	
	// Costanti
	private final int THE_BRIDGE	  = 6; // Posizione che corrisponde al "brigde", il giocatore
										   // che finisce in tale posizione 'salta' alla posizione 12 (BRIDGE END)
	
	private final int BRIDGE_END	  = 12; // Il punto in cui il giocatore finisce se entra nella casella BRIDGE
	
	private final int GOAL			  = 63; // Punto di arrivo (la partita finisce)
	private final int START			  = 0;  // Punto di partenza
	
	private final int[] GOOSE_PLACES  = {5,9,14,18,23,27}; // Caselle in cui è presente un oca (raddoppio del lancio)
	
	private boolean gameActive		  = true; 
	
	// Lista dei giocatori. Nei limiti di questo esercizio, non esiste una logica di salvataggio
	// dei profili su supporto persistente (file XML, DBMS, etc)
	
	private ArrayList<Player> players = new ArrayList<>();
	
	
	public GooseGameApplication() { }
	
	/***
	 * Avvia una partita interattiva. Viene letto l'input dell'utente,
	 * che è uno dei comandi previsti dalle specifiche, fintanto che la partita 
	 * non è vinta (un giocatore raggiunge la posizione GOAL) o se un giocatore 
	 * immette il comando QUIT.
	 * 
	 * Nota: la presenza del comando QUIT non è presente nelle specifiche originali, è stata aggiunta
	 * come condizione di comodo.
	 */
	public void interactivePlay() {
		writeOutput("");
		writeOutput("");
		writeOutput(" *** GOOSE GAME ***");
		writeOutput("");
		while(gameActive) {
			try {
				String command = readCommand();
				processCommand(command);
			}
			catch(GooseGameException exp) {
				writeOutput(exp.getMessage());
			}
		}
	}
	
	/****
	 * Avvia una partita "random". Aggiunge 3 giocatori, simulando l'immissione da console
	 * dei rispettivi comandi, e, a turno, li muove simulando il lancio di due dadi.
	 */
	public void playDemoGame() {
		try {
			processCommand("ADD PLAYER PIPPO");
			processCommand("ADD PLAYER PAPERINO");
			processCommand("ADD PLAYER TOPOLINO");
			while(gameActive) {
				for(Player p : players) {
					processCommand("MOVE "+p.getID());
					if(!gameActive) {
						break;
					}
				}
			}
		}
		catch(GooseGameException exp) {
			writeOutput(exp.getMessage());
		}
	}
	
	/***
	 * Costruisce una istanza di Command a partire da un input di tipo String.
	 * Se il comando espresso nella stringa non è riconosciuto, o mancano parametri, 
	 * o questi sono espressi in modo non valido è sollevata eccezione.
	 * 
	 * @param command
	 * @throws GooseGameException
	 * @see Command
	 */
	private void processCommand(String command) throws GooseGameException {
		Command c 		= new Command(command);
		processCommand(c, false);
	}
	
	/****
	 * Riceve in ingresso un instanza di Command e un boolean che identifica se si tratta
	 * di una "mossa ripetuta" dovuta al fatto che un giocatore è entrato in una casella
	 * rappresentante l'oca. Tale parametro è utile essenzialmente ai fini di formattazione
	 * dell'output a video.
	 * 
	 *@see Command
	 */
	private void processCommand(Command command, boolean fromGoose) throws GooseGameException {
		
		String params[] = command.getCommandParams();
		// Processa richiesta fine gioco
		if(command.isEndCommand()) {
			gameActive = false; // Forza condizione di stop sul ciclo interattivo principale
			writeOutput("Goodbye.");
		}
		
		// Gestisce il comando ADD PLAYER
		if(command.isAddPlayerCommand()) {
			Player p = new Player(params[0]);
			if(players.contains(p)) {
				throw new GooseGameException(p.toString()+": already existing player");
			}
			players.add(p);
			writeOutput("Players : "+players);
		}
		
		// Gestisce il "movimento" di un giocatore con il comando MOVE <player>, con o senza specifica
		// dei valori delle sorti dei dadi.
		if(command.isMovePlayerCommand()) {
			Player p = new Player(params[0]);
			if(!players.contains(p)) {
				throw new GooseGameException(p.toString()+": unknown player");
			}
			// la classe Command garantisce che ci siano SEMPRE i due valori dei lanci dei dadi,
			// se non vengono specificati li tira a sorte. Garantisce inoltre che siano numerici validi (in range 1-6)
			
			int roll1 = Integer.parseInt(params[1]);
			int roll2 = Integer.parseInt(params[2]);
			
			Player player     		 = players.get(players.indexOf(p));
			
			// Calcolo nuova posizione e memorizzo posizione precedente per 
			// eventualmente arretrare i giocatori già presenti nella casa di destinazione
			// alla posizione di partenza del giocatore che esegue la mossa.
			
			int originalPosition     = player.getGamePosition();
			int destination   		 = originalPosition + roll1+ roll2;
			String origPosDescr		 = getPositionDescription(originalPosition); // Fornisce una descrizione della posizione (se posizione 'speciale')
			
			String rollDescr  = player.getID()+" rolls "+roll1+ ", "+roll2+"; "; // Testo da emettere relativamente al lancio dei dati
			String moveDescr  = player.getID()+" moves from "; // Testo da emettere per descrivere la mossa (spostamento da - a)
			
			String outputText = "";
			
			
			if(fromGoose) {
				// Nel caso sia una ripetizione dello spostamento (perchè il giocatore è finito in una casella rappresentate un oca)
				// modifico la descrizione che appare a video
				rollDescr = player.getID()+" moves again "; 
				moveDescr = "and goes from ";
			}
			// Casi speciali, con ricalcolo della destinazione e/o altre azioni addizionali.
			
			//Se si finisce sulla casella con il PONTE si "salta" alla posizione 12 (Fine del ponte)
			if(destination == THE_BRIDGE) {
				destination = BRIDGE_END; // Posizione 12
				outputText = rollDescr+" "+moveDescr+" "+origPosDescr+" to the bridge. "+player.getID()+" jumps to position 12.";
				
				//writeOutput(rollDescr+" "+moveDescr+" "+sourceName+" to the bridge. "+player.getID()+" jumps to position 12.");
			}
			else
			if(isGoosePlace(destination)) { // Se 
				//writeOutput(rollDescr+" "+moveDescr+" "+sourceName+" to "+destination+" (The Goose).");
				outputText = rollDescr+" "+moveDescr+" "+origPosDescr+" to "+destination+" (The Goose).";
			}
			else
			if(destination == GOAL) {
				//writeOutput(rollDescr+" "+moveDescr+" "+sourceName+" to the GOAL");
				//writeOutput(player.getID()+" WINS !!");
				outputText = rollDescr+" "+moveDescr+" "+origPosDescr+" to the GOAL.\n"+player.getID()+" WINS !!";
				gameActive = false;
			}
			else {
				if(destination > GOAL) {
				   //writeOutput(rollDescr+" "+moveDescr+" "+sourceName+" to 63 ");
				   outputText = rollDescr+" "+moveDescr+" "+origPosDescr+" to 63 ";
				   destination = GOAL - (destination - GOAL);
				   //writeOutput(player.getID()+" bounces ! "+player.getID()+" returns to "+destination);
				   outputText += "\n "+player.getID()+" bounces ! "+player.getID()+" returns to "+destination;
				}
				else {
					//writeOutput(rollDescr+" "+moveDescr+" "+sourceName+" to "+destination);
					outputText = rollDescr+" "+moveDescr+" "+origPosDescr+" to "+destination;
				}
			}
			writeOutput(outputText);
			player.setGamePosition(destination);
			if(isGoosePlace(destination)) {
				// Ripeto lo spostamento.
				processCommand(command, true);
			}
			else {
				// Forzo spostamento degli giocatori che si trovano nella casella di arrivo
				// alla casella di partenza.
				for(Player cp: players) {
					if(!cp.equals(player)) {
						if(cp.getGamePosition() == player.getGamePosition()) {
							writeOutput("On "+player.getGamePosition()+" is "+cp.getID()+", which returns to "+originalPosition);
						}
					}
				}
			}
		}
	}

	/// Stabilisce se una casella rappresenta un'oca.
	private boolean isGoosePlace(int destination) {
		return Arrays.stream(GOOSE_PLACES).anyMatch(x -> x == destination);
	}
	

	private String getPositionDescription(int pos) {
		if(pos == START) {
			return "Start";
		}
		return "" + pos;
	}

	private void writeOutput(String string) {
		System.out.println(string);	
	}

	private Player getPlayer(String ID) {
		Player player     = players.get(players.indexOf(new Player(ID)));
		return player;
	}

	
	private String readCommand() {
		String s = "";
		try(Scanner in = new Scanner(System.in)) {
			System.out.print("[Enter command] >> ");
			s = in.nextLine();
		}
		return s;
	}
	
	
	public static void test1() throws Exception {
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 3, 3 "  );
		app.processCommand(" MOVE PIPPO 1, 1 "  );
	}
	
	public static void test2() throws Exception {
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 6, 4 "  );
		app.processCommand(" MOVE PIPPO 2, 2 "  );
	}
	
	public static void test3() throws Exception {
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 6, 7 "  );
	}
	

	public static void test4() throws Exception {
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD PLAYER PIPPO " );
		Player p = app.getPlayer("PIPPO");
		p.setGamePosition(60);
		app.processCommand(" MOVE PIPPO 2, 1 ");
	}
	
	public static void testPrank() throws Exception {
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD PLAYER PIPPO " );
		app.processCommand(" ADD PLAYER PLUTO " );
		Player p = app.getPlayer("PLUTO");
		p.setGamePosition(10);
		Player p2 = app.getPlayer("PIPPO");
		p2.setGamePosition(7);
		app.processCommand(" MOVE PIPPO 2, 1 ");
	}

	
	public static void main(String...args) throws Exception {
		//testPrank()
		GooseGameApplication app = new GooseGameApplication();
		app.playDemoGame();
		//app.interactivePlay();
	}
	
}
