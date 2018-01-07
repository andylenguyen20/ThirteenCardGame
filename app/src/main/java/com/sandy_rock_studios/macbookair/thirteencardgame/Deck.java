package com.sandy_rock_studios.macbookair.thirteencardgame;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by macbookair on 6/23/16.
 */
public class Deck {
    private ArrayList<Card> deck;
    public Deck(){
        deck = new ArrayList<>();
        createDeck();
    }
    public void createDeck(){
        createCardsOfSuit("spades");
        createCardsOfSuit("clubs");
        createCardsOfSuit("diamonds");
        createCardsOfSuit("hearts");
        shuffle();
    }
    public void createCardsOfSuit(String suit){
        deck.add(new Card("two", suit));
        deck.add(new Card("three", suit));
        deck.add(new Card("four", suit));
        deck.add(new Card("five", suit));
        deck.add(new Card("six", suit));
        deck.add(new Card("seven", suit));
        deck.add(new Card("eight", suit));
        deck.add(new Card("nine", suit));
        deck.add(new Card("ten", suit));
        deck.add(new Card("jack", suit));
        deck.add(new Card("queen", suit));
        deck.add(new Card("king", suit));
        deck.add(new Card("ace", suit));
    }
    public void shuffle(){
        ArrayList<Card> clone = new ArrayList<>();
        for(int control = 0; control < 52; control++){
            clone.add(deck.get(control));
        }
        Random randomizer = new Random();
        for(int control = 0; control < 52; control++){
            int randomInt = randomizer.nextInt(clone.size());
            deck.set(control, clone.remove(randomInt));
        }
    }
    public Card takeFromTop(){
        return deck.remove(deck.size()-1);
    }
    public boolean hasNoMoreCards(){
        return deck.size() == 0;
    }
}
