package com.dragon.mathias.learnapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dragon.mathias.learnapp.classes.Character;
import com.dragon.mathias.learnapp.classes.Quest;
import com.dragon.mathias.learnapp.manager.CharacterManager;
import com.dragon.mathias.learnapp.manager.QuestManager;

import java.util.ArrayList;

//activity for the quest overview
public class QuestOverviewActivity extends AppCompatActivity {

    private Character character;
    private CharacterManager cManager;
    private QuestManager qManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_overview);

        //init manager and load character data
        cManager = new CharacterManager(getApplicationContext());
        qManager = new QuestManager(getApplicationContext());
        character = cManager.loadCharacterData();
        final ArrayList<Quest> quests = character.getQuests();

        //listview with custom adapter
        final ListView questView = findViewById(R.id.questList);
        questView.setAdapter(new ArrayAdapter<Quest>(
                this, android.R.layout.simple_list_item_1, quests) {
            @NonNull
            public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

                Context context = this.getContext();
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View gridView;

                if (convertView == null) {
                    gridView = new View(context);
                    gridView = inflater.inflate(R.layout.quest_list_item, null);

                    Quest q = quests.get(position);
                    //layout for each quest
                    TextView nameTextView = gridView
                            .findViewById(R.id.quest_item_name);
                    nameTextView.setText(q.getName());
                    String str = q.toString();
                    nameTextView.setTag(str);

                    TextView descriptionTextView = gridView
                            .findViewById(R.id.quest_item_desription);
                    descriptionTextView.setText(q.getDescription());

                    TextView progressTextView = gridView
                            .findViewById(R.id.quest_item_progress);
                    progressTextView.setText(String.format(getString(R.string.progress), q.getProcess(), q.getRequirement()));

                    Button rerollBottin = gridView
                            .findViewById(R.id.button_reroll);
                    rerollBottin.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            quests.remove(position);
                            character.setQuests(quests);
                            qManager.addQuest(character);
                            cManager.saveCharacterData(character);
                            refreshView();
                        }
                    });

                } else {
                    gridView = convertView;
                }

                return gridView;
            }
        });
    }

    private void refreshView(){
        this.recreate();
    }

}
