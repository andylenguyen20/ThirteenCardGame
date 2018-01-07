package com.sandy_rock_studios.macbookair.thirteencardgame;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PlayerScreen extends AppCompatActivity {
    private static Player currentPlayer;
    private static ArrayList<Card> currentHand, lastValidGo, currentGo;
    private static boolean noMoreCards, skipped, freeGo;

    private LinearLayout currentHandLayout, currentGoLayout, lastValidGoLayout;
    private LinearLayout.LayoutParams params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_screen);
        currentGo = new ArrayList<>();
        currentHandLayout = (LinearLayout) findViewById(R.id.current_hand);
        currentGoLayout = (LinearLayout) findViewById(R.id.current_go);
        lastValidGoLayout = (LinearLayout) findViewById(R.id.last_valid_go);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,10,-45,10);
        for(Card card : lastValidGo){
            createLastValidGoDisplay(card.getPackageName());
        }
        for(Card card : currentHand){
            createCardButton(card.getPackageName());
        }
    }

    /*
    USED TO CRETE THE IMAGES/BUTTONS OF THE CARDS
     */
    public void createCardButton(final String imageName){
        try {
            InputStream stream = getAssets().open(imageName + ".png");
            Drawable d = Drawable.createFromStream(stream, null);
            final ImageButton imageButton = new ImageButton(this);
            imageButton.setImageDrawable(d);
            imageButton.setLayoutParams(params);
            imageButton.setPadding(0,0,0,0);
            currentHandLayout.addView(imageButton);

            final ImageButton otherImageButton = new ImageButton(this);
            otherImageButton.setImageDrawable(d);
            otherImageButton.setLayoutParams(params);
            otherImageButton.setPadding(0,0,0,0);
            currentGoLayout.addView(otherImageButton);
            otherImageButton.setVisibility(View.GONE);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        imageButton.setVisibility(View.GONE);
                        otherImageButton.setVisibility(View.VISIBLE);
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                currentGo.add(currentHand.remove(getLocationOfCardInCurrentHand(imageName)));
                            }
                        };
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(0);
                            }
                        };
                        Thread playerThread = new Thread(r);
                        playerThread.start();
                    }

            });
            otherImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    otherImageButton.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            currentHand.add(currentGo.remove(getLocationOfCardInCurrentGo(imageName)));
                        }
                    };
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                        }
                    };
                    Thread playerThread = new Thread(r);
                    playerThread.start();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createLastValidGoDisplay(String imageName){
        try {
            InputStream stream = getAssets().open(imageName + ".png");
            Drawable d = Drawable.createFromStream(stream, null);
            final ImageView image = new ImageView(this);
            image.setImageDrawable(d);
            image.setLayoutParams(params);
            image.setPadding(0,0,0,0);
            lastValidGoLayout.addView(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    USED TO CRETE THE IMAGES/BUTTONS OF THE CARDS
     */

    /*
    Called when "Submit" button is clicked. Turn is analyzed to see if it is a valid move.
    If the move passes, finishTurn method is called.
     */
    public void attemptToFinishTurn(View view){
        if(skipped){return;}

        currentGo = Game.insertionSort(currentGo);

        if(freeGo && isValidPattern()){
            setAllPlayersSkippedToFalse();
            finishTurn(view);
        }

        if(currentGo.size()==lastValidGo.size()) {
            if (inParameterIsSingle(lastValidGo) && isHigherSingleThan(lastValidGo)) {
                finishTurn(view);
            } else if (inParameterIsDouble(lastValidGo) && isHigherDoubleThan(lastValidGo)) {
                finishTurn(view);
            } else if (inParameterIsTriple(lastValidGo) && isHigherTripleThan(lastValidGo)) {
                finishTurn(view);
            } else if (inParameterIsQuadruple(lastValidGo) && isHigherQuadrupleThan(lastValidGo)) {
                finishTurn(view);
            } else if (inParameterIsStraight(lastValidGo) && isHigherStraightThan(lastValidGo)) {
                finishTurn(view);
            }
        }else if(inParameterIsSingleTwo(lastValidGo) && isTwoKiller()){
            finishTurn(view);
        }else if (inParameterIsDoubleTwo(lastValidGo) && isDoubleTwoKiller()) {
            finishTurn(view);
        }else if(inParameterIsTripleTwo(lastValidGo) && isTripleTwoKiller()){
            finishTurn(view);
        }else if(inParameterIsQuadrupleTwo(lastValidGo) && isQuadrupleTwoKiller()){
            finishTurn(view);
        }
    }
    public void finishTurn(View view){
        setAllPlayersFreeGoToFalse();
        freeGo = true;
        skipped = false;
        if(currentHand.size()==0){
            noMoreCards = true;
        }
        adjustInfoAfterMove();

        Intent intent = new Intent(this, player_intro_screen.class);
        startActivity(intent);
        //MainActivity.getGame().nextTurn();
    }
    /*
    Called when "Submit" button is clicked. Turn is analyzed to see if it is a valid move.
    If the move passes, nextTurn method is called.
     */

    /*
    Called when the "Skip" button is clicked. Game moves on to the next player.
     */
    public void skip(View view){
        if(freeGo){return;}
        freeGo = false;
        skipped = true;
        noMoreCards = false;
        currentGo = new ArrayList<>(); //must send in blank go by setting current go to an empty list of cards
        adjustInfoAfterMove();

        Intent intent = new Intent(this, player_intro_screen.class);
        startActivity(intent);
        //MainActivity.getGame().nextTurn();
    }
    /*
    Called when the "Skip" button is clicked. Game moves on to the next player.
    */

    /*
    Setter methods to transfer information over to the player screen
     */
    public static void setCurrentPlayer(Player player){
        currentPlayer = player;
    }
    public static void setCurrentHand(ArrayList<Card> hand){
        currentHand = hand;
    }
    public static void setLastValidGo(ArrayList<Card> lastGo){
        lastValidGo = lastGo;
    }
    public static void setNoMoreCards(boolean hasNoMoreCards){
        noMoreCards = hasNoMoreCards;
    }
    public static void setSkipped(boolean hasSkipped){
        skipped = hasSkipped;
    }
    public static void setFreeGo(boolean hasFreeGo){
        freeGo = hasFreeGo;
    }
    public void setAllPlayersSkippedToFalse(){
        //ArrayList<Player> players = MainActivity.getGame().getPlayers();
        //for(Player player : players){
        //    player.setSkipped(false);
        //}
    }
    public void setAllPlayersFreeGoToFalse(){
        //ArrayList<Player> players = MainActivity.getGame().getPlayers();
        //for(Player player : players){
        //    player.setFreeGo(false);
        //}
    }
    /*
    Setter methods to transfer information over to the player screen
     */

    /*
    Transfer Information method to adjust player and game info after turn
     */
    public void adjustInfoAfterMove(){
        currentPlayer.setHand(currentHand);
        currentPlayer.setFreeGo(freeGo);
        currentPlayer.setSkipped(skipped);
        currentPlayer.setNoMoreCards(noMoreCards);
        //MainActivity.getGame().setPreviousGo(currentGo);
    }
    /*
    Transfer Information method to transfer player info after turn
     */

    /*
    GET LOCATION METHODS
     */
    public int getLocationOfCardInCurrentGo(String imageName){
        int index = imageName.indexOf("_of_");
        String type = imageName.substring(0, index);
        String suit = imageName.substring(index + 4);
        for(int control = 0; control < currentGo.size(); control++){
            if(type.equals(currentGo.get(control).getType())
                    && suit.equals(currentGo.get(control).getSuit())){
                return control;
            }
        }
        return -1;
    }
    public int getLocationOfCardInCurrentHand(String imageName){
        int index = imageName.indexOf("_of_");
        String type = imageName.substring(0, index);
        String suit = imageName.substring(index + 4);
        for(int control = 0; control < currentHand.size(); control++){
            if(type.equals(currentHand.get(control).getType())
                    && suit.equals(currentHand.get(control).getSuit())){
                return control;
            }
        }
        return -1;
    }
    /*
    GET LOCATION METHODS
     */


    /*Boolean methods to verify if a go is valid
     *Lists must be sorted before calling any of these methods*/

    //Singles
    public boolean inParameterIsSingle(ArrayList<Card> list){
        return list.size()==1;
    }
    public boolean isHigherSingleThan(ArrayList<Card> lastValidGo){
        return inParameterIsSingle(currentGo) && currentGo.get(0).isGreaterThan(lastValidGo.get(0));
    }

    //Doubles
    public boolean inParameterIsDouble(ArrayList<Card> list){
        return list.size()==2 && list.get(0).hasSameFaceValueAs(list.get(1));
    }
    public boolean isHigherDoubleThan(ArrayList<Card> lastValidGo){
        return inParameterIsDouble(currentGo) && currentGo.get(1).isGreaterThan(lastValidGo.get(1));
    }

    //Triples
    public boolean inParameterIsTriple(ArrayList<Card> list){
        return list.size()==3 && list.get(0).hasSameFaceValueAs(list.get(1),list.get(2));
    }
    public boolean isHigherTripleThan(ArrayList<Card> lastValidGo){
        return inParameterIsTriple(currentGo) && currentGo.get(0).isGreaterThan(lastValidGo.get(0));
    }

    //Quadruples
    public boolean inParameterIsQuadruple(ArrayList<Card> list){
        return list.size()==4 && list.get(0).hasSameFaceValueAs(list.get(1), list.get(2), list.get(3));
    }
    public boolean isHigherQuadrupleThan(ArrayList<Card> lastValidGo){
        return inParameterIsQuadruple(currentGo) && currentGo.get(0).isGreaterThan(lastValidGo.get(0));
    }

    //Straights
    public boolean inParameterIsStraight(ArrayList<Card> list){
        if(list.size() < 3) return false;
        for(int control = list.size()-1; control > 0; control--){
            if(!list.get(control).isGreaterBy1(list.get(control-1))) return  false;
        }
        return true;
    }
    public boolean isHigherStraightThan(ArrayList<Card> lastValidGo){
        return inParameterIsStraight(currentGo) && currentGo.size() == lastValidGo.size()
                && currentGo.get(currentGo.size()-1).isGreaterThan(lastValidGo.get(lastValidGo.size()-1));
    }

    //Two-Killer
    public boolean inParameterIsSingleTwo(ArrayList<Card> list){
        return list.size()==1 && list.get(0).getType().equals("two");
    }
    public boolean isTwoKiller(){
        if(inParameterIsQuadruple(currentGo)){
            return true;
        }else if(currentGo.size()==6){
            ArrayList<Card> list1 = new ArrayList<>();
            ArrayList<Card> list2 = new ArrayList<>();
            list1.add(currentGo.get(0));
            list1.add(currentGo.get(2));
            list1.add(currentGo.get(4));
            list2.add(currentGo.get(1));
            list2.add(currentGo.get(3));
            list2.add(currentGo.get(5));
            return inParameterIsStraight(list1) && inParameterIsStraight(list2)
                    && currentGo.get(0).hasSameFaceValueAs(currentGo.get(1));
        }
        return false;
    }

    //Double Two-Killer
    public boolean inParameterIsDoubleTwo(ArrayList<Card> list){
        return list.size()==2 && list.get(0).getType().equals("two")
                && list.get(1).getType().equals("two");
    }
    public boolean isDoubleTwoKiller(){
        if(currentGo.size()==8){
            ArrayList<Card> list1 = new ArrayList<>();
            ArrayList<Card> list2 = new ArrayList<>();
            ArrayList<Card> firstQuadCheck = new ArrayList<>();
            ArrayList<Card> secondQuadCheck = new ArrayList<>();
            list1.add(currentGo.get(0));
            list1.add(currentGo.get(2));
            list1.add(currentGo.get(4));
            list1.add(currentGo.get(6));
            list2.add(currentGo.get(1));
            list2.add(currentGo.get(3));
            list2.add(currentGo.get(5));
            list2.add(currentGo.get(7));
            firstQuadCheck.add(currentGo.get(0));
            firstQuadCheck.add(currentGo.get(1));
            firstQuadCheck.add(currentGo.get(2));
            firstQuadCheck.add(currentGo.get(3));
            secondQuadCheck.add(currentGo.get(4));
            secondQuadCheck.add(currentGo.get(5));
            secondQuadCheck.add(currentGo.get(6));
            secondQuadCheck.add(currentGo.get(7));
            return (inParameterIsStraight(list1) && inParameterIsStraight(list2) && list1.get(0).hasSameFaceValueAs(list2.get(0)))
                    || (inParameterIsQuadruple(firstQuadCheck) && inParameterIsQuadruple(secondQuadCheck) && secondQuadCheck.get(0).isGreaterBy1(firstQuadCheck.get(0)));
        }
        return false;
    }

    //Triple Two-Killer
    public boolean inParameterIsTripleTwo(ArrayList<Card> list){
        return list.size()==3 && list.get(0).getType().equals("two")
                && list.get(1).getType().equals("two")
                && list.get(2).getType().equals("two");
    }
    public boolean isTripleTwoKiller(){
        if(currentGo.size()==10){
            ArrayList<Card> list1 = new ArrayList<>();
            ArrayList<Card> list2 = new ArrayList<>();
            list1.add(currentGo.get(0));
            list1.add(currentGo.get(2));
            list1.add(currentGo.get(4));
            list1.add(currentGo.get(6));
            list1.add(currentGo.get(8));
            list2.add(currentGo.get(1));
            list2.add(currentGo.get(3));
            list2.add(currentGo.get(5));
            list2.add(currentGo.get(7));
            list2.add(currentGo.get(9));
            return inParameterIsStraight(list1) && inParameterIsStraight(list2)
                    && currentGo.get(0).hasSameFaceValueAs(currentGo.get(1));
        }else if(currentGo.size()==12){
            ArrayList<Card> firstQuadCheck = new ArrayList<>();
            ArrayList<Card> secondQuadCheck = new ArrayList<>();
            ArrayList<Card> thirdQuadCheck = new ArrayList<>();
            ArrayList<Card> straightCheck = new ArrayList<>();
            firstQuadCheck.add(currentGo.get(0));
            firstQuadCheck.add(currentGo.get(1));
            firstQuadCheck.add(currentGo.get(2));
            firstQuadCheck.add(currentGo.get(3));
            secondQuadCheck.add(currentGo.get(4));
            secondQuadCheck.add(currentGo.get(5));
            secondQuadCheck.add(currentGo.get(6));
            secondQuadCheck.add(currentGo.get(7));
            thirdQuadCheck.add(currentGo.get(8));
            thirdQuadCheck.add(currentGo.get(9));
            thirdQuadCheck.add(currentGo.get(10));
            thirdQuadCheck.add(currentGo.get(11));
            straightCheck.add(currentGo.get(0));
            straightCheck.add(currentGo.get(4));
            straightCheck.add(currentGo.get(8));
            return inParameterIsQuadruple(firstQuadCheck) && inParameterIsQuadruple(secondQuadCheck)
                    && inParameterIsQuadruple(thirdQuadCheck) && inParameterIsStraight(straightCheck);
        }
        return false;
    }

    //Quadruple Two-Killer
    public boolean inParameterIsQuadrupleTwo(ArrayList<Card> list){
        return list.size()==4 && list.get(0).getType().equals("two")
                && list.get(1).getType().equals("two") && list.get(2).getType().equals("two")
                && list.get(3).getType().equals("two");
    }
    public boolean isQuadrupleTwoKiller(){
        if(currentGo.size()==12){
            ArrayList<Card> list1 = new ArrayList<>();
            ArrayList<Card> list2 = new ArrayList<>();
            list1.add(currentGo.get(0));
            list1.add(currentGo.get(2));
            list1.add(currentGo.get(4));
            list1.add(currentGo.get(6));
            list1.add(currentGo.get(8));
            list1.add(currentGo.get(10));
            list2.add(currentGo.get(1));
            list2.add(currentGo.get(3));
            list2.add(currentGo.get(5));
            list2.add(currentGo.get(7));
            list2.add(currentGo.get(9));
            list2.add(currentGo.get(11));
            return inParameterIsStraight(list1) && inParameterIsStraight(list2)
                    && currentGo.get(0).hasSameFaceValueAs(currentGo.get(1));

        }
        return false;
    }
    //Valid Pattern If Everybody Else Skipped or First Go
    public boolean isValidPattern(){
        return inParameterIsSingle(currentGo) || inParameterIsDouble(currentGo)
                || inParameterIsTriple(currentGo) || inParameterIsQuadruple(currentGo)
                || inParameterIsStraight(currentGo) || isTwoKiller() || isDoubleTwoKiller();
    }

    /*Boolean methods to verify if a go is valid
     *Lists must be sorted before calling any of these methods*/

}
