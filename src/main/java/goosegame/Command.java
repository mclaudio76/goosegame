package goosegame;

import java.util.Random;

public class Command {
	
	public static final String END_GAME      = "QUIT";
	public static final String MOVE_PLAYER   = "MOVE";
	public static final String ADD_PLAYER    = "ADD PLAYER";
	
	private String text_command;
	
	public Command(String txt_command) {
		// normalizes input, setting it to uppercase and removing unnecessary blank spaces
		boolean lastCharIsWhiteSpace = false;
		StringBuilder sb 		= new StringBuilder();
		for(Character c : txt_command.toCharArray()) {
			if(Character.isWhitespace(c)) {
				if(!lastCharIsWhiteSpace) {
					sb.append(c);
				}
				lastCharIsWhiteSpace = true;
			}
			else {
				if(c != ',') {
					sb.append(c);
				}
				lastCharIsWhiteSpace = false;
			}
		}
		text_command = sb.toString().trim().toUpperCase();
	}
	
	
	public String[] getCommandParams() throws GooseGameException {
		String[] splitCmd = text_command.split(" ");
		if(isEndCommand()) {
			return new String[0];
		}
		if(isAddPlayerCommand()) {
			if(splitCmd.length != 3) {
				throw new GooseGameException("Syntax : ADD PLAYER <playername> ");
			}
			String[] res = new String[] {splitCmd[2]};
			return res;
		}
		if(isMovePlayerCommand()) {
			if(splitCmd.length == 2) {
				// Random roll dice: 
				Random randomGenerator = new Random();
				int dado1 = randomGenerator.nextInt(6) + 1;
				int dado2 = randomGenerator.nextInt(6) + 1;
				String[] res = new String[] {splitCmd[1], ""+dado1, ""+dado2};
				return res;
			}
			if(splitCmd.length == 4) {
				if(!validateDiceNumber(splitCmd[2])) {
					throw new GooseGameException("Syntax : ["+splitCmd[2]+"] is not a valid dice number.");					
				}
				if(!validateDiceNumber(splitCmd[3])) {
					throw new GooseGameException("Syntax : ["+splitCmd[3]+"] is not a valid dice number.");					
				}
				String[] res = new String[] {splitCmd[1], splitCmd[2], splitCmd[3]};
				return res;
			}
			throw new GooseGameException("Syntax : MOVE <playername> <dice1>, <dice2> for 'manual' dice roll \n          MOVE <playername> for 'automatic' dice roll \\n ");
		}
		return null;
	}
	
	private boolean validateDiceNumber(String num) {
		try {
			int value = Integer.parseInt(num);
			return value >= 1 && value <= 6;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public String toString() {
		return text_command;
	}


	public boolean isEndCommand() {
		return text_command.startsWith(END_GAME);
	}
	
	public boolean isAddPlayerCommand() {
		return text_command.startsWith(ADD_PLAYER);
	}
	
	public boolean isMovePlayerCommand() {
		return text_command.startsWith(MOVE_PLAYER);
	}
	
	
}
