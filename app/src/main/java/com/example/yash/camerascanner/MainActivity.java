package com.example.yash.camerascanner;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static Button bn;
    private Uri imageUri; // Used for full resolution Images
    private static ImageView img;
    private static int request_code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img_view);
        bn = (Button) findViewById(R.id.clickButton);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Check if camera activity is available or not
                if (i.resolveActivity(getPackageManager()) != null){

                    // SD card path
                    File file = Environment.getExternalStorageDirectory();

                    // Creates a new folder in sd card
                    File dir = new File(file.getAbsolutePath()+"/camScanner/");
                    dir.mkdir();

                    // Create object for the image
                    File file1 = new File(dir, "test.jpg");
                    imageUri = Uri.fromFile(file1);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                    startActivityForResult(i, request_code);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                }
            }
        });
    }

    // Method for handling the result by camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code){
            if (resultCode == RESULT_OK){
                /*Bundle bundle = new Bundle();
                bundle = data.getExtras();*/ // Causes crashing of application
                Bitmap bmpImage = null;
                try {
                    bmpImage = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    img.setImageBitmap(bmpImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OutputStream outputStream;

                // SD card path
                File file = Environment.getExternalStorageDirectory();

                // Creates a new folder in sd card
                // Path to create new image file
                File dir = new File(file.getAbsolutePath()+"/camScanner/");

                // Create object for the image
                File file1 = new File(dir, "test.jpg");
                try {
                    outputStream = new FileOutputStream(file1);
                    // Compress into image format
                    if (bmpImage != null) {
                        bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    }
                    outputStream.flush();
                    outputStream.close();
                    String url = MediaStore.Images.Media.insertImage(
                            getContentResolver(), bmpImage, "test.jpg", null
                            );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void grabImage(ImageView imageView, Bitmap bp){
        this.getContentResolver().notifyChange(imageUri, null);
        ContentResolver cr = this.getContentResolver();
        try{
            // Setting the image bitmap on image view
            imageView.setImageBitmap(bp);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
