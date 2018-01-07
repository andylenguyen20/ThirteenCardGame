package com.sandy_rock_studios.macbookair.thirteencardgame;

/**
 * Created by macbookair on 6/23/16.
 */
public class Card {
    private String type;
    private String suit;
    private int faceValue;
    private int suitValue;
    public Card(String type, String suit){
        this.type = type;
        this.suit = suit;
        setFaceValue();
        setSuitValue();
    }

    //GETTER METHODS
    public String getType(){
        return type;
    }
    public String getSuit(){
        return suit;
    }
    public int getFaceValue(){
        return faceValue;
    }
    public int getSuitValue(){
        return suitValue;
    }

    //SETTER METHODS
    public void setFaceValue(){
        switch (type){
            case "three":
                faceValue = 3;
                break;
            case "four":
                faceValue = 4;
                break;
            case "five":
                faceValue = 5;
                break;
            case "six":
                faceValue = 6;
                break;
            case "seven":
                faceValue = 7;
                break;
            case "eight":
                faceValue = 8;
                break;
            case "nine":
                faceValue = 9;
                break;
            case "ten":
                faceValue = 10;
                break;
            case "jack":
                faceValue = 11;
                break;
            case "queen":
                faceValue = 12;
                break;
            case "king":
                faceValue = 13;
                break;
            case "ace":
                faceValue = 14;
                break;
            case "two":
                faceValue = 15;
                break;
        }
    }
    public void setSuitValue(){
        switch(suit){
            case "spades":
                suitValue = 1;
                break;
            case "clubs":
                suitValue = 2;
                break;
            case "diamonds":
                suitValue = 3;
                break;
            case "hearts":
                suitValue = 4;
                break;
        }
    }

    //COMPARE METHODS
    public boolean isGreaterBy1(Card other){
        return this.getFaceValue() - other.getFaceValue() == 1;
    }
    public boolean isGreaterThan(Card other){
        return this.getFaceValue() > other.getFaceValue()
                || (this.getFaceValue() == other.getFaceValue()
                && this.getSuitValue() > other.getSuitValue());
    }
    public boolean hasSameFaceValueAs(Card other){
        return this.getFaceValue() == other.getFaceValue();
    }
    public boolean hasSameFaceValueAs(Card other, Card other2){
        return this.getFaceValue() == other.getFaceValue()
                && this.getFaceValue() == other2.getFaceValue();
    }
    public boolean hasSameFaceValueAs(Card other, Card other2, Card other3){
        return this.getFaceValue() == other.getFaceValue()
                && this.getFaceValue() == other2.getFaceValue()
                && this.getFaceValue() == other3.getFaceValue();
    }

    //TO STRING METHOD RETURNS ITS TYPE IN THE FORM OF "type_of_suit"
    public String getPackageName(){
        return type + "_of_" + suit;
    }
}
