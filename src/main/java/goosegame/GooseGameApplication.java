package goosegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GooseGameApplication {
	
	private final int THE_BRIDGE	  = 6;
	private final int GOAL			  = 63;
	private final int START			  = 0;
	private final int[] GOOSE_PLACES  = {5,9,14,18,23,27};
	
	private boolean run 			  = true;
	private ArrayList<Player> players = new ArrayList<>();
	
	
	public GooseGameApplication() {
		
	}
	
	public void run() {
		writeOutput("");
		writeOutput("");
		writeOutput(" *** GOOSE GAME ***");
		writeOutput("");
		while(run) {
			try {
				String command = readCommand();
				processCommand(command);
			}
			catch(GooseGameException exp) {
				writeOutput(exp.getMessage());
			}
		}
	}
	
	public void playGame() {
		try {
			processCommand("ADD PLAYER PIPPO");
			processCommand("ADD PLAYER PAPERINO");
			processCommand("ADD PLAYER TOPOLINO");
			while(run) {
				for(Player p : players) {
					processCommand("MOVE "+p.getID());
					if(!run) {
						break;
					}
				}
			}
		}
		catch(GooseGameException exp) {
			writeOutput(exp.getMessage());
		}
	}
	
	private void processCommand(String command) throws GooseGameException {
		processCommand(command, false);
	}
	
	private void processCommand(String command, boolean fromGoose) throws GooseGameException {
		Command c 		= new Command(command);
		String params[] = c.getCommandParams();
		
		if(c.isEndCommand()) {
			run = false;
			writeOutput("Goodbye.");
		}
		
		if(c.isAddPlayerCommand()) {
			Player p = new Player(params[0]);
			if(players.contains(p)) {
				throw new GooseGameException(p.toString()+": already existing player");
			}
			players.add(p);
			writeOutput("Players : "+players);
		}
		

		if(c.isMovePlayerCommand()) {
			Player p = new Player(params[0]);
			if(!players.contains(p)) {
				throw new GooseGameException(p.toString()+": unknown player");
			}
			int roll1 = Integer.parseInt(params[1]);
			int roll2 = Integer.parseInt(params[2]);
			
			Player player     = players.get(players.indexOf(p));
			int sourcePos     = player.getGamePosition();
			int destination   = player.getGamePosition() + roll1+ roll2;
			String sourceName = getNameForPosition(sourcePos);
			String rollDescr  = player.getID()+" rolls "+roll1+ ", "+roll2+"; ";
			String descrDesc  = player.getID()+" moves from ";
			if(fromGoose) {
				rollDescr = player.getID()+" moves again ";
				descrDesc = "and goes from ";
			}
			if(destination == THE_BRIDGE) {
				destination = 12;
				writeOutput(rollDescr+" "+descrDesc+" "+sourceName+" to the bridge. "+player.getID()+" jumps to position 12.");
			}
			else
			if(isGoosePlace(destination)) {
				writeOutput(rollDescr+" "+descrDesc+" "+sourceName+" to "+destination+" (The Goose).");
			}
			else
			if(destination == GOAL) {
				writeOutput(rollDescr+" "+descrDesc+" "+sourceName+" to the GOAL");
				writeOutput(player.getID()+" WINS !!");
				run = false;
			}
			else {
				
				if(destination > GOAL) {
				   writeOutput(rollDescr+" "+descrDesc+" "+sourceName+" to 63 ");
				   destination = GOAL - (destination - GOAL);
				   writeOutput(player.getID()+" bounces ! "+player.getID()+" returns to "+destination);	
				}
				else {
					writeOutput(rollDescr+" "+descrDesc+" "+sourceName+" to "+destination);
				}
			}
			player.setGamePosition(destination);
			if(isGoosePlace(destination)) {
				processCommand(command, true);
			}
			else {
				// Prank phase
				for(Player cp: players) {
					if(!cp.equals(player)) {
						if(cp.getGamePosition() == player.getGamePosition()) {
							writeOutput("On "+player.getGamePosition()+" is "+cp.getID()+", which returns to "+sourcePos);
						}
					}
				}
			}
		}
	}

	
	
	private boolean isGoosePlace(int destination) {
		return Arrays.stream(GOOSE_PLACES).anyMatch(x -> x == destination);
	}
	

	private String getNameForPosition(int pos) {
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
		Scanner in = new Scanner(System.in);
		System.out.print("[Your command] >> ");
		String s = in.nextLine();
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
		app.processCommand(" ADD PLAYER PIPPO " );
		Player p = app.getPlayer("PIPPO");
		p.setGamePosition(10);
		Player p2 = app.getPlayer("PIPPO");
		p2.setGamePosition(7);
		app.processCommand(" MOVE PIPPO 2, 1 ");
	}

	
	public static void main(String...args) throws Exception {
		//testPrank()
		GooseGameApplication app = new GooseGameApplication();
		//app.playGame();
		app.run();
	}
	
}
