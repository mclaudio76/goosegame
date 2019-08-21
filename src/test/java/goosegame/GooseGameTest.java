package goosegame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GooseGameTest {

	
	@Test
    public void testControlloGiocatoreUnivoco () throws GooseGameException {
		System.out.println(" Test : --> Giocatore univoco ");
		Assertions.assertThrows(GooseGameException.class, () -> {
			GooseGameApplication app = new GooseGameApplication();
			app.processCommand(" ADD PLAYER PIPPO " );
			app.processCommand(" ADD PLAYER PIPPO " );      
		});
	}
    
    @Test
	public void testMossaSemplice () throws GooseGameException {
		System.out.println(" Test : --> Mossa semplice ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD  PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 1,2 " );
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), 3);
	}
	
	@Test
	public void testBridge () throws GooseGameException {
		System.out.println(" Test : --> Bridge ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD  PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 2,2 " ); // Posizione 4
		app.processCommand(" MOVE PIPPO 1,1 " ); // Bridge --> jump to 12
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), 12);
	}
	
	@Test
	public void testCasellaOca () throws GooseGameException {
		System.out.println(" Test : --> Oca (singola) ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD  PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 3,2 " ); // Posizione 5, oca , rilancia --> 10
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), 10);
	}
	
	@Test
    public void testCasellaOcaDoppia () throws GooseGameException {
		System.out.println(" Test : --> Oca (doppia) ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD  PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 3,1 " ); // Posizione 4
		app.processCommand(" MOVE PIPPO 3,2 " ); // Posizione 9 --> oca --> salta 14 (altra oca) --> 19.
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), 19);
	}

	@Test
    public void testSpostaGiocatoriSuCasellaInizioMossa () throws GooseGameException {
		System.out.println(" Test : --> Spostamento giocatori forzato ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD  PLAYER PIPPO " );
		app.processCommand(" ADD  PLAYER PLUTO " );
		app.processCommand(" MOVE PIPPO 1,1 " );  // Posizione 2
		app.processCommand(" MOVE PLUTO 1, 1 " ); // Posizione 2 --> Pippo ritorna alla partenza
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), 0);
	}

	
}
