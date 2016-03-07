package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.lang.reflect.GenericArrayType;

import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.*;
import comp3004.ivanhoe.Card.CardColour;

public class RulesEngineTest {
	RulesEngine rules;
	
	@Before
	public void setUp() throws Exception {
		rules = RulesEngine.testRuleEngine(3);
		rules.registerThread(1);
		rules.registerThread(2);
		rules.registerThread(3);
		//rules.registerThread(4);
		//rules.registerThread(5);
	}
	
	@Test
	public void tournamentSetup() {
		assertFalse(rules.isTournamentRunning());
		rules.initTournament();
		assertEquals(8, rules.getPlayerById(1).getHandSize());
		assertEquals(8, rules.getPlayerById(2).getHandSize());
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		assertEquals(CardColour.Blue, rules.getPlayerById(1).getDisplay().getColour());
		
	}
		
	@Test
	public void validatePlay(){
		tournamentSetup();
		rules.getPlayerById(1).addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(rules.validatePlay("Blue 4", (long) 1));
	}
	
	@Test
	public void playCard(){
		tournamentSetup();
		Player p = rules.getPlayerById(2);

		p.addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(rules.playCard(p.getHandSize()-1, p.getID()));
	}
	 
	@Test
	public void playTurn(){
		tournamentSetup();
		Player p, p2;
		p = rules.getPlayerList().get(0);
		p2 = rules.getPlayerList().get(1);
		//System.out.println(p.getid());
		rules.startTurn(p.getID()); //draw card
		assertEquals(p.getHandSize(), 9);
		assertTrue(rules.playCard(0, p.getID()));
		assertEquals(p.getDisplay().calculatePoints(), 2);
		assertTrue(rules.endTurn(p.getID()));
		
		//turn 2 - testing end turn checks after plays have already been made
		p = rules.getPlayerList().get(0);
		//System.out.println(p.getid());
		assertEquals(p, p2);
		assertTrue(rules.playCard(0, p.getID()));
		assertFalse(rules.endTurn(p.getID()));
		assertTrue(rules.playCard(0, p.getID()));
		assertTrue(rules.endTurn(p.getID()));
		
	}
	
	@Test
	public void withdrawTest() {
		tournamentSetup();
		Player p1, p2, p3;
		p1 = rules.getPlayerList().get(0);
		p2 = rules.getPlayerList().get(1);
		p3 = rules.getPlayerList().get(2);
		
		//withdraw should not set a high score
		rules.startTurn(rules.getPlayerList().get(0).getID());
		rules.playCard(0, rules.getPlayerList().get(0).getID());
		assertEquals(rules.withdrawPlayer(p1.getID()), null);
		assertEquals(rules.getHighestScore(),0);
		
		//withdrawn player should be skipped over
		assertEquals(rules.getPlayerList().get(0), p2);
		rules.startTurn(rules.getPlayerList().get(0).getID());
		rules.playCard(0, rules.getPlayerList().get(0).getID());
		rules.endTurn(rules.getPlayerList().get(0).getID());
		assertEquals(rules.getPlayerList().get(0), p3);
		rules.startTurn(rules.getPlayerList().get(0).getID());
		rules.playCard(0, rules.getPlayerList().get(0).getID());
		rules.playCard(0, rules.getPlayerList().get(0).getID());
		rules.endTurn(rules.getPlayerList().get(0).getID());
		assertEquals(rules.getPlayerList().get(0), p1);
		assertFalse(rules.startTurn(rules.getPlayerList().get(0).getID()));
		
		//withdrawing should return the winner
		assertEquals(rules.getPlayerList().get(0), p2);
		rules.startTurn(rules.getPlayerList().get(0).getID());
		assertEquals(p3.getID(), (long) rules.withdrawPlayer(p2.getID()));
		
		//withdrawing with maiden should return own ID, so player can lose point
		rules.initTournament();
		rules.initializeTournamentColour(rules.getPlayerList().get(0).getID(), CardColour.Blue);
		assertEquals(rules.getPlayerList().get(0), p3);
		rules.startTurn(p3.getID());
		p3.addCard(new SupporterCard(6));
		rules.playCard(p3.getHandSize()-1, p3.getID());
		assertEquals(rules.withdrawPlayer(p3.getID()),Long.valueOf(-1));
	}
	
	@Test
	public void tournamentTest(){
		rules.chooseFirstTournament();
		rules.initializeTournamentColour(rules.getPlayerList().get(0).getID(), CardColour.Purple);
		rules.dealHand();
		Player p;
		int turn = 0;
		
		while(turn < 20){
			p = rules.getPlayerList().get(0);
			rules.startTurn(p.getID());
			
			do{
				rules.playCard(0, p.getID());
				if(p.getHandSize() == 0){
					Long a = rules.withdrawPlayer(p.getID());
					if(a != null){
						rules.getPlayerById(a).recieveToken(rules.getTournamentColour());
					}
				}
			}while(p.getPlaying() && (!rules.endTurn(p.getID())));
			
			rules.endTurn(p.getID());
			
			turn++;
		}
	}
	
}
