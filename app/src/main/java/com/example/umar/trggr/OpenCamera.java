package com.example.umar.trggr;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class OpenCamera extends AppCompatActivity {

    Button button;
    ImageView imageView;
    static final int CAM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_camera);

        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.image_view);

        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getFile();
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_intent,CAM_REQUEST);

    }

    public void whenClicked(View v){

    }

    private File getFile(){
        File folder = new File("sdcard/camera_app");

        if(!folder.exists()){
            folder.mkdir();
        }
        File image_file = new File(folder,"cam_image.jpg");
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String path = "sdcard/camera_app/cam_image.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));




        //send right back to Main Activity, can change
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
}
