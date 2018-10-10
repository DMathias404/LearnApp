package com.dragon.mathias.learnapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dragon.mathias.learnapp.classes.Character;
import com.dragon.mathias.learnapp.classes.EquipmentItem;
import com.dragon.mathias.learnapp.classes.LevelData;
import com.dragon.mathias.learnapp.manager.CharacterManager;
import com.dragon.mathias.learnapp.manager.EquipmentManager;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//activity for avatar-overview
public class AvatarActivity extends AppCompatActivity {

    private Character character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        //loading character-data with manager
        final CharacterManager cmanager = new CharacterManager(getApplicationContext());
        character = cmanager.loadCharacterData();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ProgressBar xpBar = findViewById(R.id.progressExp);
            xpBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorXPbar)));
        }

        //find layout-element
        TextView levelText = findViewById(R.id.textLevel);
        TextView expText = findViewById(R.id.textExp);
        ProgressBar expProgress = findViewById(R.id.progressExp);

        levelText.setText(String.format(getString(R.string.level), character.getLevel()));
        expText.setText(String.valueOf((character.getRequiredExp() - character.getExperience())));
        expProgress.setMax(character.getRequiredExp());
        expProgress.setProgress(character.getExperience());

        //get all settings from file
        EquipmentManager eManager = new EquipmentManager(this.getApplicationContext());
        final ArrayList<EquipmentItem> equipmentList = eManager.getEquipmentList();

        //get position of saved setting
        String eName = character.getEquipment().getName();
        int pos = -1;
        for(EquipmentItem e : equipmentList){
            if(e.getName().equals(eName)){
                pos = equipmentList.indexOf(e);
            }
        }

        //Spinner with all settings
        Spinner itemSpinner = findViewById(R.id.itemSpinner);
        //custom adapter for two-lined spinner-items
        itemSpinner.setAdapter(new ArrayAdapter<EquipmentItem>(this, android.R.layout.simple_list_item_1, equipmentList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = inflater.inflate(R.layout.equipment_spinner_item, parent, false);
                TextView labelName = row.findViewById(R.id.equipment_name);
                labelName.setText(equipmentList.get(position).getName());
                TextView labelDescription = row.findViewById(R.id.equipment_description);
                labelDescription.setText(equipmentList.get(position).getDescription());

                return row;
            }

            @NonNull
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = inflater.inflate(R.layout.equipment_spinner_item, parent, false);
                TextView labelName = row.findViewById(R.id.equipment_name);
                labelName.setText(equipmentList.get(position).getName());
                TextView labelDescription = row.findViewById(R.id.equipment_description);
                labelDescription.setText(equipmentList.get(position).getDescription());

                return row;
            }
        });
        //onSelectListener
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EquipmentItem item = equipmentList.get(i);
                character.setEquipment(item);
                cmanager.saveCharacterData(character);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //set position corresponding to saved setting
        itemSpinner.setSelection(pos);

    }
}
