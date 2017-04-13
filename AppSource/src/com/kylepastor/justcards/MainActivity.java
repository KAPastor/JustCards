package com.kylepastor.justcards;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Typeface;
import android.util.AttributeSet;
import java.util.Random;

public class MainActivity extends Activity
{
    public int[] usedCards = new int[52];
    boolean endDeck=false;
    int count=-1;
    public int[] cardSequence = new int[53];
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.argb(255, 255, 255 , 255));
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        setContentView(R.layout.main);
       
        for (int i=0; i<usedCards.length; i++){ //Initialize the cards
            usedCards[i]=0;
            cardSequence[i]=99;
        }
        ImageView img = (ImageView) findViewById(R.id.playing_cards);
        img.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int arraySum=0;
                for(int j=0;j<usedCards.length;j++){
                    arraySum+=usedCards[j];
                }
                ImageView img = (ImageView) v.findViewById(R.id.playing_cards);
                while(true){
                    Random r = new Random();
                    int rNum=r.nextInt(52);
                
                    if(usedCards[rNum]==0){
                        count++;
                        cardSequence[count]=rNum;
                        String mDrawableName = "c" + Integer.toString(rNum);
                        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                        img.setImageResource(resID);
                        usedCards[rNum]=1;
                        break;
                    }
                    if(arraySum==52){
                        img.setImageResource(R.drawable.resetscreen);
                        if(endDeck){
                            //reset the deck of cards (clear arrays and set to joker)
                            for(int i=0;i<usedCards.length;i++){
                                usedCards[i]=0;
                                cardSequence[i]=99;
                            }
                            img.setImageResource(R.drawable.splashscreen);
                            count=-1;
                            endDeck=false;
                            break;
                        }
                        endDeck=true;
                        break;
                    }
                }
            }
        });
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        View view = this.getWindow().getDecorView();
        ImageView img = (ImageView) view.findViewById(R.id.playing_cards);
        switch (item.getItemId()) {
            case R.id.action_reset:
                //reset the deck of cards (clear arrays and set to joker)
                for(int i=0;i<usedCards.length;i++){
                    usedCards[i]=0;
                    cardSequence[i]=99;
                }
                count=-1;
                img.setImageResource(R.drawable.splashscreen);
                return true;
            case R.id.action_undo:
                if(count>0){
                    count--;
                    int prevCard=cardSequence[count];
                    String cardDrawableName = "c" + Integer.toString(prevCard);
                    int resID = getResources().getIdentifier(cardDrawableName , "drawable", getPackageName());
                    img.setImageResource(resID);
                    usedCards[cardSequence[count+1]]=0; //Revalidate the current card
                    cardSequence[count+1]=99;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

