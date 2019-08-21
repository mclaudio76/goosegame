package goosegame;

import java.util.Random;

public class Command {
	
	/// Tipologie di comandi riconosciuti
	public static final String END_GAME      = "QUIT";
	public static final String MOVE_PLAYER   = "MOVE";
	public static final String ADD_PLAYER    = "ADD PLAYER";
	
	private String text_command; 
	private String[] command_params;
	
	/***
	 * Costruisce un'instanza di Command a partire dal testo immesso.
	 * Il testo rappresentante il comando viene normalizzato (messo in uppercase,
	 * tolti spazi in eccesso, rimossi i caratteri virgola [separatore dei valori dei dadi]
	 * quindi parserizzato, per l'estrazione dei parametri.
	 * Il processo di parsing esegue anche la fase di validazione e di controllo vera e propria
	 * del comando: se quest'ultimo non è riconosciuto come valido viene sollevata eccezione.
	 * 
	 */
	public Command(String txt_command) throws GooseGameException {
		boolean lastCharIsWhiteSpace = false;
		txt_command = txt_command.replaceAll(",", " ");
		StringBuilder sb 		= new StringBuilder();
		for(Character c : txt_command.toCharArray()) {
			if(Character.isWhitespace(c)) {
				if(!lastCharIsWhiteSpace) {
					sb.append(c);
				}
				lastCharIsWhiteSpace = true;
			}
			else {
				sb.append(c);
				lastCharIsWhiteSpace = false;
			}
		}
		text_command   = sb.toString().trim().toUpperCase();
		command_params = parseCommand();
	}
	
	// Restituisce al chiamante i parametri del comando immesso 
	public String[] getCommandParams() {
		return command_params;
	}
	
	/***
	 * Esegue il parsing del comando, memorizzando i parametri relativi
	 * ed eseguendo una verifica (per la verità, minimale) che essi siano corretti.
	 * Se la verifica non è superata, viene sollevata eccezione con una descrizione dell'errore verificatosi.
	 * I parametri in questa implementazione sono sempre restituiti sotto forma di String, 
	 * è compito del chiamante eventualmente eseguire conversione di tipo.
	 * 
	 * @return
	 * @throws GooseGameException
	 */
	private String[] parseCommand() throws GooseGameException {
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
			// Se vi sono due token soltanto significa che il comando è di tipo MOVE <nomeplayer>, viene 
			// quindi simulato il lancio dei due dadi.
			if(splitCmd.length == 2) {
				Random randomGenerator = new Random();
				int dado1 = randomGenerator.nextInt(6) + 1;
				int dado2 = randomGenerator.nextInt(6) + 1;
				String[] res = new String[] {splitCmd[1], ""+dado1, ""+dado2};
				return res;
			}
			// Se vi sono 4 token allora il comanda immesso è di tipo MOVE <nomeplayer> <dado1> <dado2>, si verifica che 
			// i valori dei "dadi" siano compresi tra 1-6 
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
			// Il numero dei parametri non è nè 2 nè 4 ? Errore.
			throw new GooseGameException("Syntax : MOVE <playername> <dice1>, <dice2> for 'manual' dice roll \n          MOVE <playername> for 'automatic' dice roll \\n ");
		}
		// Se il comando non è riconosciuto si solleva errore
		throw new GooseGameException("Error : "+text_command+" unknown.");
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
