package goosegame;

/**
 * Eccezione custom, di servizio.
 * Rappresenta una eccezione gestita dal programma.
 */

public class GooseGameException extends Exception {
	
	private static final long serialVersionUID = -6472201840021233887L;

	public GooseGameException(String mex) {
		super(mex);
	}
}
