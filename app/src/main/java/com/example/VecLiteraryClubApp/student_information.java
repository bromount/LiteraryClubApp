package com.example.VecLiteraryClubApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class student_information extends AppCompatActivity {


    TextView Fname, Lname, Name, LCA1, LCA2, Year, Course_Section, Submissions, Registrations, Gender, Email;
    CardView Edit_Profile;
    String UID;
    ImageView ProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);

        getWindow().setNavigationBarColor(Color.BLACK);

        Fname = findViewById(R.id.student_information_Fname);
        Lname = findViewById(R.id.student_information_Lname);
        Name = findViewById(R.id.student_information_Name);
        LCA1 = findViewById(R.id.student_information_LCA_1);
        LCA2 = findViewById(R.id.student_information_LCA_2);
        Year = findViewById(R.id.student_information_Year);
        Course_Section = findViewById(R.id.student_information_Course_Section);
        Submissions = findViewById(R.id.student_information_Submissions);
        Registrations = findViewById(R.id.student_information_Registrations);
        Gender = findViewById(R.id.student_information_Gender);
        Email = findViewById(R.id.student_information_Email);
        Edit_Profile = findViewById(R.id.student_information_Edit_Profile);
        ProfilePic = findViewById(R.id.student_information_Profile);

        String Values[] = getIntent().getStringArrayExtra("UserInformation");
        UID = Values[10];


        byte ProfileBytes[] = getIntent().getByteArrayExtra("ProfileBytes");

        if (Values[8].compareTo("none") != 0 && ProfileBytes.length > 1) {

            ProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(ProfileBytes, 0, ProfileBytes.length));

        }


        Fname.setText(Values[0]);
        Lname.setText(Values[1]);
        Name.setText(Values[0] + " " + Values[1]);
        LCA1.setText("" + Values[9].substring(0, 3) + "-" + Values[9].substring(3, 6));
        LCA2.setText("" + Values[9].substring(0, 3) + "-" + Values[9].substring(3, 6));
        switch (Integer.parseInt(Values[4])) {
            case 1:
                Year.setText("1st Year");
                break;
            case 2:
                Year.setText("2nd Year");
                break;
            case 3:
                Year.setText("3rd Year");
                break;
            case 4:
                Year.setText("4th Year");
                break;
            default:
                Year.setText("39th Year");
                break;
        }
        Gender.setText(Values[7]);
        Registrations.setText(Values[11]);
        Submissions.setText(Values[12]);
        Email.setText(Values[5]);
        Course_Section.setText(Values[2] + " / " + Values[3]);

//        if( Name.length() > 14 ){
        Name.setSelected(true);
        //      }
        //  if( Fname.length() > 9 ){
        Fname.setSelected(true);
        //    }
        // if( Lname.length() > 9 ){
        Lname.setSelected(true);
        //}
        //  if( Email.length() > 9 ){
        Email.setSelected(true);
        //}


    }

}
