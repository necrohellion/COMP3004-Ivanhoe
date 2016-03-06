package comp3004.ivanhoe;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.Card.CardType;

public class Player {
	private Hand hand;
	private PointsBoard display;
	private HashMap<CardColour, Integer> tokens;
	private long id;
	private boolean isPlaying = false;
	
	public Player(){
		tokens = new HashMap<CardColour,Integer>();
		display = new PointsBoard();
		hand = new Hand();
		
		tokens.put(CardColour.Purple, 0);
		tokens.put(CardColour.Green, 0);
		tokens.put(CardColour.Red, 0);
		tokens.put(CardColour.Blue, 0);
		tokens.put(CardColour.Yellow, 0);
	}
	
	public Hand getHand(){
		return hand;
	}
	
	public boolean getPlaying(){
		return isPlaying;
	}
	
	public Card getCardByName(String s){
		return hand.getCardByName(s);
	}
	
	public void removeCard(String s) {
		hand.remove(s);
	}
	
	public void setPlaying(boolean s){
		isPlaying = s;
	}
	
	/**
	 * Returns the number of tokens in the players possession
	 * @return int
	 */
	public int getTokenCount(){
		int temp = 0;
		Collection<Integer> values = tokens.values();
		for (Iterator<Integer> it = values.iterator(); it.hasNext();) {
			temp += it.next();
		}
		return temp;
	}
	
	/**
	 * Checks if the card is in the players hand
	 * @param s name of card
	 * @return boolean
	 */
	public boolean hasInHand(String s){
		return hand.contains(s);
	}
	
	/**
	 * Draws a card  and adds it to hand
	 * @param c Card
	 */
	public void addCard(Card c){
		hand.add(c);
	}
	
	/**
	 * Adds a Color card to the display
	 * @param cardname name of card
	 */
	public void playColourCard(String cardname){
		Card c = hand.getCardByName(cardname);
		if(c.cardType == CardType.Colour || c.cardType == CardType.Supporter){
			display.addCard(c);
		}
	}
	
	/**
	 * Plays the Action card to the discard pile and causes the effect
	 * @param cardname
	 * @param id
	 */
	public void playActionCard(String cardname){
		Card card = hand.getCardByName(cardname);
		display.addCard(card);
		display.remove(card.getCardName());
	}
	
	public int getHandSize(){
		return hand.getNumCards();
	}
	
	public PointsBoard getDisplay(){
		return display;
	}
	
	/**
	 * Returns the thread ID that the player class is associated with
	 * @return long ID
	 */
	public long getID(){
		return id;
	}
	
	public void setid(long i){
		id = i;
	}
	
	/**
	 * Adds a token to the players collection
	 * @param colour Colour of the token
	 */
	public void recieveToken(CardColour colour){
		if(tokens.containsKey(colour)){
			tokens.put(colour, 1);
		}
	}
	/**
	 * Remove a token from the player
	 * @param colour
	 */
	public void removeToken(CardColour colour){
		if(tokens.containsKey(colour)){
			tokens.put(colour, 0);
		}
	}
}
