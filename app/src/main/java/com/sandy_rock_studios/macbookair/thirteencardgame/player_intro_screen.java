package com.sandy_rock_studios.macbookair.thirteencardgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class player_intro_screen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_intro_screen);

        //int nextTurn = MainActivity.getGame().getNextTurn();
        TextView introText = (TextView) findViewById(R.id.playerNameText);
        //String playerIntroduction = "Player " + nextTurn;
        //introText.setText(playerIntroduction);
    }
    public void backToGame(View view){
        Intent intent = new Intent(this, PlayerScreen.class);
        startActivity(intent);
    }
}
