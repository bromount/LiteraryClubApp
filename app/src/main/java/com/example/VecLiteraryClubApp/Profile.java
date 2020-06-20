package com.example.VecLiteraryClubApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Application;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static java.lang.Integer.parseInt;

public class Profile extends AppCompatActivity {

    TextView Fname, Lname, Name, LCA1, LCA2, Year, Course_Section, Submissions, Registrations, Gender, Email;
    CardView Edit_Profile, Loading_Profile;
    Uri ImageFile;
    private StorageReference mStorageRef;
    String UID;
    ImageView ProfilePic;


    final int PICK_FILE_REQUEST = 39;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setNavigationBarColor(Color.BLACK);

        Fname = findViewById(R.id.profile_Fname);
        Lname = findViewById(R.id.profile_Lname);
        Name = findViewById(R.id.profile_Name);
        LCA1 = findViewById(R.id.profile_LCA_1);
        LCA2 = findViewById(R.id.profile_LCA_2);
        Year = findViewById(R.id.profile_Year);
        Course_Section = findViewById(R.id.profile_Course_Section);
        Submissions = findViewById(R.id.profile_Submissions);
        Registrations = findViewById(R.id.profile_Registrations);
        Gender = findViewById(R.id.profile_Gender);
        Email = findViewById(R.id.profile_Email);
        Loading_Profile = findViewById(R.id.profile_loading);
        Edit_Profile = findViewById(R.id.profile_Edit_Profile);
        ProfilePic = findViewById(R.id.profile_Profile);

        String Values[] = getIntent().getStringArrayExtra("UserInformation");
        UID = Values[10];


        if (Values[8].compareTo("none") != 0) {
            byte ProfileBytes[] = getIntent().getByteArrayExtra("ProfileBytes");
            ProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(ProfileBytes, 0, ProfileBytes.length));
        }


        Fname.setText(Values[0]);
        Lname.setText(Values[1]);
        Name.setText(Values[0] + " " + Values[1]);
        LCA1.setText("" + Values[9].substring(0, 3) + "-" + Values[9].substring(3, 6));
        LCA2.setText("" + Values[9].substring(0, 3) + "-" + Values[9].substring(3, 6));
        switch (Values[4]) {
            case "1":
                Year.setText("1st Year");
                break;
            case "2":
                Year.setText("2nd Year");
                break;
            case "3":
                Year.setText("3rd Year");
                break;
            case "4":
                Year.setText("4th Year");
                break;
            default:
                Year.setText("-");
                break;
        }
        Gender.setText(Values[7]);
        Registrations.setText(Values[11]);
        Submissions.setText(Values[12]);
        Email.setText(Values[5]);
        Course_Section.setText(Values[2] + " / " + Values[3]);

        Name.setSelected(true);
        Fname.setSelected(true);
        Lname.setSelected(true);
        Email.setSelected(true);

        Edit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loading_Profile.setVisibility(View.VISIBLE);
                Intent intent = new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && data != null) {
            if (resultCode == RESULT_OK) {
                Uri file = data.getData();
                ImageFile = file;
                try {
                    InputStream is = getContentResolver().openInputStream(file);
                    Bitmap Profile_unscaled = BitmapFactory.decodeStream(is);
                    final Bitmap Profile_Scaled = Bitmap.createScaledBitmap(Profile_unscaled, 360, 360, true);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Profile_Scaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    mStorageRef = FirebaseStorage.getInstance().getReference("Profile/" + UID + "/Profile.jpg");
                    mStorageRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            FirebaseDatabase DataRef = FirebaseDatabase.getInstance();
                            DataRef.getReference("STUDENT/" + UID + "/8").setValue("Profile");

                            ProfilePic.setImageBitmap(Profile_Scaled);

                            Intent restart = new Intent(getApplicationContext(), SplashScreen.class);
                            startActivity(restart);
                            finish();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "Failed to upload profile picture", Toast.LENGTH_LONG).show();

                        }
                    });
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Failed to get file", Toast.LENGTH_LONG).show();
                }
                Loading_Profile.setVisibility(View.INVISIBLE);
            }
        } else {
            Loading_Profile.setVisibility(View.INVISIBLE);
        }


    }
}
