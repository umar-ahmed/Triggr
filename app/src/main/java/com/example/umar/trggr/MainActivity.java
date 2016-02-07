package com.example.umar.trggr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

public class MainActivity extends AppCompatActivity {
    public TextView showBalance;
    public double currentBalance = 0;
    boolean done = false;
    public double aps = 0; //aps = amount per save

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Start service to listen for shaking
        startService(new Intent(MainActivity.this, Background_service.class));

        int cnt = getNumDots();
        //cnt = 0;
        if(cnt == 0){
            try {
                FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
                fos.write((".").getBytes());
                fos.close();
            }
            catch(Exception e){
            }
            this.setContentView(R.layout.login);
        }
        else if(cnt == 1){
            try {
                FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
                fos.write(("..").getBytes());
                fos.close();
            }
            catch(Exception e){
            }

            this.setContentView(R.layout.instructions);
        }

        else if(cnt == 2){
            try {
                FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
                fos.write(("...").getBytes());
                fos.close();
            }
            catch(Exception e){
            }

            this.setContentView(R.layout.s1);
        }

        else if(cnt == 3){
            try {
                FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
                fos.write(("....").getBytes());
                fos.close();
            }
            catch(Exception e){
            }
            this.setContentView(R.layout.s2);

        }

        else if(cnt == 4){
            try {
                FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
                fos.write((".....").getBytes());
                fos.close();
            }
            catch(Exception e){
            }
            this.setContentView(R.layout.s3);
        }

        else if(cnt == 5){
            try {
                FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
                fos.write(("......").getBytes());
                fos.close();
            }
            catch(Exception e){
            }
            this.setContentView(R.layout.s4);
        }
        else {
            setContentView(R.layout.activity_main);
            showBalance = (TextView) findViewById(R.id.textView);
            showBalance.setText(dollar("$" + String.valueOf(getTotalTimes() * getAps())));
            startNotification();

            int listImages[] = new int[]{R.drawable.two, R.drawable.one,
                    R.drawable.three, R.drawable.four, R.drawable.five};

            ArrayList<Card> cards = new ArrayList<Card>();

           /* for (int i = 0; i<5; i++) {
                Card card = new Card(this);
                CardHeader header = new CardHeader(this);
                header.setTitle("Angry bird: " + i);
                card.setTitle("sample title");
                card.addCardHeader(header);
                CardThumbnail thumb = new CardThumbnail(this);
                thumb.setDrawableResource(listImages[i]);
                card.addCardThumbnail(thumb);
                cards.add(card);
            }*/


            Card card = new Card(this);
                CardHeader header = new CardHeader(this);
                header.setTitle("Shannon Baker: $15");
                card.setTitle("Perfect Attendance for a week!");
                card.addCardHeader(header);
                CardThumbnail thumb = new CardThumbnail(this);
                thumb.setDrawableResource(listImages[1]);
                card.addCardThumbnail(thumb);
                cards.add(card);

            card = new Card(this);
                 header = new CardHeader(this);
                header.setTitle("Russel Heritage: $20");
                card.setTitle("10 Selfies in ten days!");
                card.addCardHeader(header);
                 thumb = new CardThumbnail(this);
                thumb.setDrawableResource(listImages[0]);
                card.addCardThumbnail(thumb);
                cards.add(card);



             card = new Card(this);
                 header = new CardHeader(this);
                header.setTitle("Melody Li: $5");
                card.setTitle("Discovered every place in her house!");
                card.addCardHeader(header);
                 thumb = new CardThumbnail(this);
                thumb.setDrawableResource(listImages[2]);
                card.addCardThumbnail(thumb);
                cards.add(card);



             card = new Card(this);
                 header = new CardHeader(this);
                header.setTitle("Bisma Imran: $10");
                card.setTitle("Brought lunch from home all week!");
                card.addCardHeader(header);
                 thumb = new CardThumbnail(this);
                thumb.setDrawableResource(listImages[3]);
                card.addCardThumbnail(thumb);
                cards.add(card);



             card = new Card(this);
                 header = new CardHeader(this);
                header.setTitle("Adit Patel: $7");
                card.setTitle("Went to practice every day this month!");
                card.addCardHeader(header);
                 thumb = new CardThumbnail(this);
                thumb.setDrawableResource(listImages[4]);
                card.addCardThumbnail(thumb);
                cards.add(card);


            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

            CardListView listView = (CardListView) this.findViewById(R.id.myList);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }
            //showBalance.setText("$124.00");
        }

    }

    public void opencam(View view){
        Intent intent = new Intent(this, OpenCamera.class);
        startActivity(intent);
    }

    //on recognition of nfc event
    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        //appendTotal();

        //initialize gesture detector, for swipe
        //gestureDetector = new GestureDetector(this, new SwipeGestureDetector());

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);


            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            String amount = new String(message.getRecords()[0].getPayload());

            //showBalance.setText("125");
            //showBalance.setVisibility(View.INVISIBLE); //REMOVE FOR FRIENDSHIP

            appendTotal();
            //showBalance.setText("1225");

            /*Intent intentt = new Intent(this, OpenCamera.class);
            startActivity(intentt);*/
        }

    }



    //HELPER FUNCTIONS
    //************************************************************************************************************************
    //retrieve the total amount of times action done
    private int getTotalTimes(){
        try{
            int cnt = 0;
            String filename = "Times.txt";
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            while(r.readLine() != null){
                cnt++;
            }
            return cnt;
        }
        catch(Exception e){
            return 1;
        }
    }

    //retrieve the amount saved per action
    private double getAps(){
        try{
            String ret = " ";
            String filename = "APS.txt";
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            ret = r.readLine();
            r.close();
            inputStream.close();
            return Double.parseDouble(ret);
        }
        catch(Exception e){
            return 1.0;
        }
    }

    //append to total number of moves made.
    //in the near future, space-separated with timestamp?
    private void appendTotal(){
        try {
            FileOutputStream fos = openFileOutput("Times.txt", Context.MODE_APPEND);
            fos.write(("1" + "\n").getBytes());
            fos.close();

            showBalance.setText(dollar("$" + String.valueOf(getTotalTimes() * getAps())));
        }
        catch(Exception e){
        }
        return;
    }

    public static String dollar(String a){
        String b = a;
        int cnt = 0;
        if(a.charAt(a.length()-1) == '.')
            b = a + "00";
        else if(a.charAt(a.length()-2) == '.')
            b = a + "0";
        return b;
    }


    //NOTIFICATION!
    public void startNotification(){
        String titlee = "Hey Good Looking";
        String contente = "Whats cooking?";

        File f = getBaseContext().getFileStreamPath("Done.txt");
        if(!f.exists()) return;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                        .setContentTitle(titlee)
                        .setContentText(contente)
                        .setAutoCancel(true)
                        .setVibrate(new long[100])
                        .setPriority(Notification.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= 21) mBuilder.setVibrate(new long[0]);

        Intent resultIntent = new Intent(this, OpenCamera.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.addAction(0, "Take a Selfie!", resultPendingIntent);
        NotificationManager mNotificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());

        try {
            FileOutputStream fos = openFileOutput("Done.txt", Context.MODE_APPEND);
            fos.write(("yio").getBytes());
            fos.close();
        }
        catch(Exception e){
        }
    }

    private int getNumDots(){
        try {
            String ret = "";
            String filename = "Dots.txt";
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            ret = r.readLine();
            r.close();
            inputStream.close();
            return ret.length();
            //YOOOOOOOOOOOOOOOOOOOOO ERASWE THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //return Math.max(ret.length(),6);
        }catch(Exception e){
            return 0;
        }
    }


    public void savelogin(View view){
        //make toast to say confirmed

        this.setContentView(R.layout.instructions);
/*
        FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
        fos.write((".....").getBytes());
        fos.close();*/

        try {
            FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
            fos.write(("......").getBytes());
            fos.close();
        }
        catch(Exception e){
        }
    }

    public void Understood(View view){
        //make toast to say confirmed

        this.setContentView(R.layout.s1);

        try {
            FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
            fos.write(("......").getBytes());
            fos.close();
        }
        catch(Exception e){
        }
    }
    public void tos2(View view){
        //make toast to say confirmed

        this.setContentView(R.layout.s2);

        try {
            FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
            fos.write(("......").getBytes());
            fos.close();
        }
        catch(Exception e){
        }
    }
    public void tos3(View view){
        //make toast to say confirmed

        this.setContentView(R.layout.s3);

        try {
            FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
            fos.write(("......").getBytes());
            fos.close();
        }
        catch(Exception e){
        }
    }
    public void tos4(View view){
        //make toast to say confirmed

        this.setContentView(R.layout.s4);

        try {
            FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
            fos.write(("......").getBytes());
            fos.close();
        }
        catch(Exception e){
        }
    }

    public void toMain(View view){
        //make toast to say confirmed

        try {
            FileOutputStream fos = openFileOutput("Dots.txt", Context.MODE_PRIVATE);
            fos.write(("......").getBytes());
            fos.close();
        }
        catch(Exception e){
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
