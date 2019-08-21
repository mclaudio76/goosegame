package goosegame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GooseGameTest {

	
	@Test
    public void testAggiuntaGiocatore () throws GooseGameException {
		System.out.println(" Test : --> Aggiunta Giocatore ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD PLAYER PIPPO " );
		assertNotNull(app.getPlayer("PIPPO"));
	}
	
	@Test
	public void testVittoria() throws GooseGameException {
		System.out.println(" Test : --> Vittoria ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD PLAYER PIPPO " );
		app.getPlayer("PIPPO").setGamePosition(60);
		app.processCommand("MOVE PIPPO 1, 2");
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), GooseGameApplication.GOAL);
	}
	
	@Test
	public void testBounce() throws GooseGameException {
		System.out.println(" Test : --> Superamento casella 63 ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD PLAYER PIPPO " );
		app.getPlayer("PIPPO").setGamePosition(60);
		app.processCommand("MOVE PIPPO 3, 2");
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), 61);
	}
	
	
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
	public void testSingleJump () throws GooseGameException {
		System.out.println(" Test : --> Singolo 'salto' per casella 'oca' ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD  PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 1,2 " );
		app.processCommand(" MOVE PIPPO 1,1 " ); // Posizione 5, oca , rilancia 2 --> 7
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), 7);
	}
	
	@Test
    public void testCasellaOcaDoppia () throws GooseGameException {
		System.out.println(" Test : --> Doppio salto per casella 'oca' (ripetuta) ");
		GooseGameApplication app = new GooseGameApplication();
		app.processCommand(" ADD  PLAYER PIPPO " );
		app.processCommand(" MOVE PIPPO 5,5 " ); // Posizione 10
		app.processCommand(" MOVE PIPPO 2,2 " ); // Posizione 14 --> oca --> salta a 18 --> altra oca --> finisce a 22.
		assertEquals(app.getPlayer("PIPPO").getGamePosition(), 22);
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
