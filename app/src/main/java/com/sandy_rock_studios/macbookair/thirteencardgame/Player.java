package com.sandy_rock_studios.macbookair.thirteencardgame;

import java.util.ArrayList;

/**
 * Created by macbookair on 6/23/16.
 */
public class Player {
    private ArrayList<Card> hand = new ArrayList<>();
    private boolean freeGo, skipped = false, noMoreCards= false;

    /*
    Creates a new player
     */
    public Player(){
    }
    /*
    Create a new player
     */

    /*
    Creates a new hand
     */
    public void receiveHand(Deck fromDeck){
        for(int cardCount = 0; cardCount < 13; cardCount++){
            hand.add(fromDeck.takeFromTop());
        }
    }
    /*
    Creates a new hand
     */

    /*
    Setter methods
     */
    public void setHand(ArrayList<Card> adjustedHand){
        hand = adjustedHand;
    }
    public void setFreeGo(boolean hasFreeGo){
        freeGo = hasFreeGo;
    }
    public void setSkipped(boolean hasSkipped){
        skipped = hasSkipped;
    }
    public void setNoMoreCards(boolean hasNoMoreCards){
        noMoreCards = hasNoMoreCards;
    }
    /*
    Setter methods
    */

    /*
    Getter methods
     */
    public ArrayList<Card> getHand(){
        return hand;
    }
    public boolean getFreeGo(){
        return freeGo;
    }
    public boolean getSkipped(){
        return skipped;
    }
    public boolean getNoMoreCards(){
        return noMoreCards;
    }
    /*
    Getter methods
     */


    /*
    Other basic player information
     */
    public boolean handContains(String type, String suit){
        for(Card card : hand){
            if(card.getType().equals(type) && card.getSuit().equals(suit)){
                return true;
            }
        }
        return false;
    }
    /*
    Other basic player information
    */
}
