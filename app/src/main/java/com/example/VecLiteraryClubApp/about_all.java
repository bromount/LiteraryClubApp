package com.example.VecLiteraryClubApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class about_all extends AppCompatActivity {

    CardView Home, Event, Gallery, Student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_all);

        getWindow().setNavigationBarColor(Color.BLACK);

        Home = findViewById(R.id.Home_Banner_All);
        Event = findViewById(R.id.Event_Banner_All);
        Gallery = findViewById(R.id.Gallery_Banner_All);
        Student = findViewById(R.id.Student_Banner_All);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AboutIntent = new Intent(about_all.this, about_home.class);
                startActivity(AboutIntent);
            }
        });


        Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AboutIntent = new Intent(about_all.this, about_event.class);
                startActivity(AboutIntent);
            }
        });


        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AboutIntent = new Intent(about_all.this, about_gallery.class);
                startActivity(AboutIntent);
            }
        });


        Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AboutIntent = new Intent(about_all.this, about_student.class);
                startActivity(AboutIntent);
            }
        });

    }
}
