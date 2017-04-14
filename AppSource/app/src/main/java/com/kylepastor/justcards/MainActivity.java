package com.kylepastor.justcards;

//import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
//import android.support.v7.app.ActionBarActivity;
import android.speech.tts.TextToSpeech;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // Set up the global properties of this activity
    TextToSpeech card_voice;    // Text to speech object

    // Setting up a mapping from card number to card name (which can be parsed for text)
    Map<Integer, String> card_map = new HashMap<Integer, String>();

    // Add the boolean setting properties that can be changed by the user
    Boolean speak_cards;    // Let's us know if the user wants to hear the card spoken
    Boolean keep_drawn;     // Keep cards drawn (always sample from 52/54)
    Boolean include_jokers; // Include jokers in the deck

    // End of deck property
    Boolean endofdeck = false;
    // Add a growing list of cards used in the hand
    // Index corresponds to the card in the mapping below
    public Boolean[] drawn_cards = new Boolean[54];

    // Method to populate the card mapping
    private void populateCardMap(){
        // Aces ------
        card_map.put(0, "ace_of_clubs");
        card_map.put(1, "ace_of_diamonds");
        card_map.put(2, "ace_of_hearts");
        card_map.put(3, "ace_of_spades");
        // 2 ------
        card_map.put(4, "two_of_clubs");
        card_map.put(5, "two_of_diamonds");
        card_map.put(6, "two_of_hearts");
        card_map.put(7, "two_of_spades");
        // 3 ------
        card_map.put(8, "three_of_clubs");
        card_map.put(9, "three_of_diamonds");
        card_map.put(10, "three_of_hearts");
        card_map.put(11, "three_of_spades");
        // 4 ------
        card_map.put(12, "four_of_clubs");
        card_map.put(13, "four_of_diamonds");
        card_map.put(14, "four_of_hearts");
        card_map.put(15, "four_of_spades");
        // 5 ------
        card_map.put(16, "five_of_clubs");
        card_map.put(17, "five_of_diamonds");
        card_map.put(18, "five_of_hearts");
        card_map.put(19, "five_of_spades");
        // 6 ------
        card_map.put(20, "six_of_clubs");
        card_map.put(21, "six_of_diamonds");
        card_map.put(22, "six_of_hearts");
        card_map.put(23, "six_of_spades");
        // 7 ------
        card_map.put(24, "seven_of_clubs");
        card_map.put(25, "seven_of_diamonds");
        card_map.put(26, "seven_of_hearts");
        card_map.put(27, "seven_of_spades");
        // 8 ------
        card_map.put(28, "eight_of_clubs");
        card_map.put(29, "eight_of_diamonds");
        card_map.put(30, "eight_of_hearts");
        card_map.put(31, "eight_of_spades");
        // 9 ------
        card_map.put(32, "nine_of_clubs");
        card_map.put(33, "nine_of_diamonds");
        card_map.put(34, "nine_of_hearts");
        card_map.put(35, "nine_of_spades");
        // 10 ------
        card_map.put(36, "ten_of_clubs");
        card_map.put(37, "ten_of_diamonds");
        card_map.put(38, "ten_of_hearts");
        card_map.put(39, "ten_of_spades");
        // jack ------
        card_map.put(40, "jack_of_clubs");
        card_map.put(41, "jack_of_diamonds");
        card_map.put(42, "jack_of_hearts");
        card_map.put(43, "jack_of_spades");
        // queen ------
        card_map.put(44, "queen_of_clubs");
        card_map.put(45, "queen_of_diamonds");
        card_map.put(46, "queen_of_hearts");
        card_map.put(47, "queen_of_spades");
        //  king ------
        card_map.put(48, "king_of_clubs");
        card_map.put(49, "king_of_diamonds");
        card_map.put(50, "king_of_hearts");
        card_map.put(51, "king_of_spades");
        // Jokers ------
        card_map.put(52, "black_joker");
        card_map.put(53, "red_joker");


    }

    private void refreshDrawnArray(){
        // Initialize the boolean drawn card array
        for (int i=0; i<drawn_cards.length; i++){
            drawn_cards[i]=false;
        }
    }

    private Boolean countDrawnCards() {
        // Tells us if we have reached the end of the deck
        Integer count_drawn = 0;
        for (int i = 0; i < drawn_cards.length; i++) {
            if (drawn_cards[i]) { // If it has been drawn add
                count_drawn++;
            }
        }
        // Check to see if we are at the end
        Boolean complete = false;
        if (include_jokers){
            if (count_drawn == 54) {
                complete = true;
            }
        }else{
            if (count_drawn == 52) {
                complete = true;
            }
        }
        return complete;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When we create the activity we are going to first find the shared preferences from the settings
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        // Collect and assign the boolean settings
        speak_cards = settings.getBoolean("voice_on_switch", false);
        keep_drawn = settings.getBoolean("keep_drawn_switch", false);
        include_jokers = settings.getBoolean("include_jokers_switch", false);

        // Now we have the long annoying job of mapping the card names to numeric ID's
        // Let's call a function to do this and operate directly on the card_map object
        populateCardMap();
        refreshDrawnArray();

        // Set up the text to speech object here for the UK dialect
        card_voice=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    card_voice.setLanguage(Locale.UK);
                }
            }
        });

        // Set the content of the view based on the main activity xml
        setContentView(R.layout.activity_main);

        // Now we have the meat of the code.  Here we are going to do the card change based on the
        // settings on image tap

        // Grab the image view
        ImageView shown_card = (ImageView) findViewById(R.id.playing_cards);

        // Now we are going to set a click event listener to see when someone taps the card
        shown_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // On click we need to do a few things.  First of which is to grab the image view
                ImageView card = (ImageView) v.findViewById(R.id.playing_cards);

                String drawn_card_name = "";

                // Now we need to check the settings on keep drawn
                if (keep_drawn){
                    // The logic here is simple.  We just take a random sample and update
                    Random r = new Random();

                    // Now we need to determine if we have included jokers
                    Integer num_cards = 52;
                    if(include_jokers){num_cards = 53;}

                    // Take the sample and get the card name and ID
                    Integer random_card_number=r.nextInt(num_cards);
                    drawn_card_name = card_map.get(random_card_number);

                    // Take the drawn card and change the showning card th this
                    Integer resID = getResources().getIdentifier(drawn_card_name, "drawable", getPackageName());
                    card.setImageResource(resID);

                }else{
                    // Here we need to make a random sample but make sure it has not already been drawn
                    Boolean end_of_deck = countDrawnCards();
                    // If we are at ropes end we will show the splash and let the user know the deck is over so
                    // you have to tap again
                    if (end_of_deck){

                        // First we need to show the end screen
                        Integer resID = getResources().getIdentifier("resetscreen", "drawable", getPackageName());

                        // Check to see if we are already showing the end screen
                        Object current_card_resID = card.getTag();
                        if (endofdeck){ // Then we are already showing the reset screen so restart
                            // We perform our resets here.  Refresh the used cards
                            refreshDrawnArray();
                            // Put the splash screen back up
                            Integer resID_splash = getResources().getIdentifier("splashscreen", "drawable", getPackageName());
                            card.setImageResource(resID_splash);
                            if (speak_cards){card_voice.speak("You are now on the start screen. Please tap to draw a card.", TextToSpeech.QUEUE_FLUSH, null);}
                            endofdeck = false;
                            return;
                        }

                        // Since this is the first time we have been here we will shown the reset screen and speak
                        card.setImageResource(resID);
                        if (speak_cards){card_voice.speak("You have reached the end of the deck. Please tap to go to the start screen.", TextToSpeech.QUEUE_FLUSH, null);}
                        endofdeck = true;
                        return;
                    }

                    // If we get here then we need to draw from the unused set of cards
                    while(true){
                        // Pick a rando and compare against the used card list
                        Random r = new Random();
                        // Now we need to determine if we have included jokers
                        Integer num_cards = 52;
                        if(include_jokers){num_cards = 54;}
                        Integer random_card_number=r.nextInt(num_cards);
                        Log.d("Main0",Integer.toString(random_card_number));

                        if (!drawn_cards[random_card_number]) { // if it is NOT a drawn card...
                            // Take the sample and get the card name and ID
                            drawn_card_name = card_map.get(random_card_number);

                            // Take the drawn card and change the showning card th this
                            Integer resID = getResources().getIdentifier(drawn_card_name, "drawable", getPackageName());
                            card.setImageResource(resID);
                            card.setTag(resID);

                            // Put this card in the used set
                            drawn_cards[random_card_number] = true;
                            break;
                        }
                    }

                }

                // Now we will say the card out loud if the accessibility option is set
                if (speak_cards){ // We will call the card speak functionality here
                    String modified_card_name = drawn_card_name.replace('_', ' ');
                    // Apply this string to the voice function
                    card_voice.speak(modified_card_name, TextToSpeech.QUEUE_FLUSH, null);
                }



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
        // When we resume the activity we are going to first find the shared preferences from the settings
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        // Collect and assign the boolean settings
        speak_cards = settings.getBoolean("voice_on_switch", false);
        Boolean keep_drawn_tmp = settings.getBoolean("keep_drawn_switch", false);
        Boolean include_jokers_tmp = settings.getBoolean("include_jokers_switch", false);

        // If either keep_drawn or include_jokers is changed we need to do a reset on the used_cards
        // and put back the splash
        if (keep_drawn_tmp!=keep_drawn || include_jokers_tmp!=include_jokers){
            // We perform our resets here.  Refresh the used cards
            refreshDrawnArray();
            // Put the splash screen back up
            ImageView card = (ImageView) findViewById(R.id.playing_cards);
            Integer resID = getResources().getIdentifier("splashscreen", "drawable", getPackageName());
            card.setImageResource(resID);

            // Update the preferences for real
            keep_drawn = keep_drawn_tmp;
            include_jokers = include_jokers_tmp;
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // We perform our resets here.  Refresh the used cards
                refreshDrawnArray();
                // Put the splash screen back up
                ImageView card = (ImageView) findViewById(R.id.playing_cards);
                Integer resID_splash = getResources().getIdentifier("splashscreen", "drawable", getPackageName());
                card.setImageResource(resID_splash);

                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }
}
