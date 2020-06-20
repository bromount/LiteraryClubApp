package com.example.VecLiteraryClubApp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;

public class VIew_Image extends AppCompatActivity {

    PhotoView View_Image_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__image);

        getWindow().setNavigationBarColor(Color.BLACK);

        View_Image_View = findViewById(R.id.view_image_view);
        byte[] Image_bytes = getIntent().getByteArrayExtra("ImageBytes");
        Bitmap Image = BitmapFactory.decodeByteArray(Image_bytes, 0, Image_bytes.length);


        View_Image_View.setImageBitmap(Image);

    }
}
