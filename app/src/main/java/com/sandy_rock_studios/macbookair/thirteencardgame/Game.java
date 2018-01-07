package com.sandy_rock_studios.macbookair.thirteencardgame;

import java.util.ArrayList;

/**
 * Created by macbookair on 6/23/16.
 */
public class Game {
    private ArrayList<Player> players;
    private boolean alreadyWinner = false, alreadyRunnerUp = false, alreadyThird = false;
    private int currentTurn;
    private int playerName;
    private Player currentPlayer;
    private Deck gameDeck;
    private ArrayList<Card> previousGo = new ArrayList<>();
    private ArrayList<Card> lastValidGo = new ArrayList<>();

    /*
    Methods called at the start of a new game. Players are created and hands are created from a deck
     */
    public Game(){
        players = new ArrayList<>();
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
        players.add(new Player());
        gameDeck = new Deck();
        distributeCards();
        for(int player = 0; player < players.size(); player++){
            if(players.get(player).handContains("three", "spades")){
                currentTurn = player;
            }
        }
        currentPlayer = players.get(currentTurn);
        currentPlayer.setFreeGo(true);
        transferInfoToPlayerScreen();
    }
    public void distributeCards(){
        players.get(0).receiveHand(gameDeck);
        players.get(1).receiveHand(gameDeck);
        players.get(2).receiveHand(gameDeck);
        players.get(3).receiveHand(gameDeck);
    }
    /*
    Methods called at the start of a new game. Players are created and hands are created from a deck
     */

    /*
    Two methods. One progresses game to next turn. Other sends current player information to the player screen.
     */
    public void nextTurn(){
        if(currentPlayer.getNoMoreCards()){
            players.remove(currentTurn);
        }
        if(currentTurn >= players.size()-1){
            currentTurn = 0;
        }else{
            currentTurn++;
        }
        currentPlayer = players.get(currentTurn);
        transferInfoToPlayerScreen();
    }
    public void transferInfoToPlayerScreen(){
        PlayerScreen.setCurrentPlayer(currentPlayer);
        PlayerScreen.setCurrentHand(insertionSort(currentPlayer.getHand()));
        PlayerScreen.setFreeGo(currentPlayer.getFreeGo());
        PlayerScreen.setNoMoreCards(currentPlayer.getNoMoreCards());
        PlayerScreen.setSkipped(currentPlayer.getSkipped());
        PlayerScreen.setLastValidGo(lastValidGo);
    }
    /*
    Two methods. One progresses game to next turn. Other sends current player information to the player screen.
     */

    /*
    Sets the previous go. This method is called by the PlayerScreen class.
    If the previous go is a valid go (not a skip), the last valid go
    is the previous go.
     */
    public void setPreviousGo(ArrayList<Card> go){
        previousGo = go;
        if(previousGo.size() > 0){
            setLastValidGo(previousGo);
        }
    }
    public void setLastValidGo(ArrayList<Card> go){
        lastValidGo = go;
    }
    /*
    Sets the previous go. This method is called by the PlayerScreen class.
    If the previous go is a valid go (not a skip), the last valid go
    is the previous go.
     */

    //Method used to SORT LISTS
    public static ArrayList<Card> insertionSort(ArrayList<Card> list){
        Card temp;
        for(int i = 1; i < list.size(); i++){
            for(int j = i; j > 0; j--){
                if(list.get(j-1).isGreaterThan(list.get(j))){
                    temp = list.get(j);
                    list.set(j, list.get(j-1));
                    list.set(j-1, temp);
                }
            }
        }
        return list;
    }

    //GETTER methods
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public int getNextTurn(){
        if(playerName == 4){
            playerName = 1;
            return playerName;
        }else{
            return ++playerName;
        }
    }

}
