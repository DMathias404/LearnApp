package com.dragon.mathias.learnapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dragon.mathias.learnapp.classes.Character;
import com.dragon.mathias.learnapp.manager.CharacterManager;
import com.dragon.mathias.learnapp.manager.QuestManager;

//Activity for the main menu, after app startup
public class MainActivity extends AppCompatActivity {

    private Character character;
    private CharacterManager cManager;
    private QuestManager qManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //loading data
        cManager  = new CharacterManager(this.getApplicationContext());
        qManager  = new QuestManager(this.getApplicationContext());

        character = cManager.loadCharacterData();
        qManager.checkQuestUpdate(character);
        cManager.saveCharacterData(character);

        //menu-buttons + intents
        Button startButton = findViewById(R.id.homeButtonStart);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent gotoLessons = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(gotoLessons);
            }
        });

        Button avatarButton = findViewById(R.id.homeButtonAvatar);
        avatarButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent gotoAvatar = new Intent(MainActivity.this, AvatarActivity.class);
                startActivity(gotoAvatar);
            }
        });

        Button questsButton = findViewById(R.id.homeButtonQuests);
        questsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent gotoQuests = new Intent(MainActivity.this, QuestOverviewActivity.class);
                startActivity(gotoQuests);
            }
        });

        Button aboutButton = findViewById(R.id.homeButtonAbout);
        aboutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent gotoAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(gotoAbout);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
