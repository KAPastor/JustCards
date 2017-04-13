package com.kylepastor.justcards;

//import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
//import android.support.v7.app.ActionBarActivity;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech t1;
    Boolean SpeakCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SpeakCards = settings.getBoolean("voice_on_switch", false);


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
//        ActionBar bar = getSupportActionBar();
//        ColorDrawable colorDrawable = new  ColorDrawable(Color.parseColor("#360C85"));
//        bar.setBackgroundDrawable(colorDrawable);




        setContentView(R.layout.activity_main);

        ImageView img = (ImageView) findViewById(R.id.playing_cards);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageView img = (ImageView) v.findViewById(R.id.playing_cards);
                int resID = getResources().getIdentifier("logo" , "drawable", getPackageName());
                img.setImageResource(resID);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SpeakCards = settings.getBoolean("voice_on_switch", false);
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//        Boolean jokers = settings.getBoolean("include_jokers_switch", false);
//        String jokers_status="Jokers are off.";
//        if (jokers){
//            jokers_status = "Jokers are on.";
//        }
//        Toast.makeText(this, jokers_status, Toast.LENGTH_SHORT).show();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:

                Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
                if (SpeakCards){
                    t1.speak("Ace of Diamonds", TextToSpeech.QUEUE_FLUSH, null);
                }

                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "The government is poo.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingsActivity.class);

                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }
}
