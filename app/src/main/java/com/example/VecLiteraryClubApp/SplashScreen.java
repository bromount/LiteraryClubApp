package com.example.VecLiteraryClubApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SplashScreen extends AppCompatActivity {

    ImageView SplashLogo;
    TextView SplashText;
    boolean Student = true;
    boolean LoggedIn = false;

    DataSnapshot dataSnapshot_Student_info;

    private FirebaseAuth mAuth;
    Intent I;
    String Values[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        getWindow().setNavigationBarColor(Color.BLACK);
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SplashText = findViewById(R.id.SplashText);
        SplashLogo = findViewById(R.id.SplashLogo);

        AnimationSet SplashLogoAnimations = new AnimationSet(false);

        RotateAnimation LogoRotate = new RotateAnimation(-360, 0, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        LogoRotate.setDuration(4000);
        LogoRotate.setRepeatCount(0);
        LogoRotate.setInterpolator(new DecelerateInterpolator());
        SplashLogoAnimations.addAnimation(LogoRotate);

        AlphaAnimation LogoAlpha = new AlphaAnimation(0, 1);
        LogoAlpha.setDuration(2500);
        LogoAlpha.setStartOffset(1000);
        LogoAlpha.setRepeatCount(0);
        LogoAlpha.setInterpolator(new DecelerateInterpolator());
        SplashLogoAnimations.addAnimation(LogoAlpha);

        SplashLogo.setAnimation(SplashLogoAnimations);
        SplashText.setAnimation(LogoAlpha);

        SplashLogoAnimations.setFillAfter(true);
        SplashLogoAnimations.start();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference StudentRef = database.getReference("STUDENT/" + mAuth.getUid());

            StudentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    if (LoggedIn) {
                        return;
                    }


                    try {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                I = new Intent(SplashScreen.this, MainActivity.class);

                                Values = new String[14];
                                Values[0] = "-----";
                                Values[1] = "-----";
                                Values[2] = "-----";
                                Values[3] = "-----";
                                Values[4] = "8";
                                Values[5] = "-----";
                                Values[6] = "-----";
                                Values[7] = "-----";
                                Values[8] = "-----";
                                Values[9] = "-----";
                                Values[10] = "-----";
                                Values[11] = "0";
                                Values[12] = "0";
                                Values[13] = "-----";

                                int i = 0;
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    Values[i] = child.getValue().toString();
                                    i++;
                                }


                                dataSnapshot_Student_info = dataSnapshot;

                                database.getReference("ADMINS").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.child(FirebaseAuth.getInstance().getUid()).getValue() != null) {
                                            Student = false;
                                        }


                                        if (Values[8].compareTo("none") == 0) {

                                            I.putExtra("UserInformation", Values);
                                            I.putExtra("Student", Student);
                                            LoggedIn = true;
                                            startActivity(I);
                                            finish();

                                        } else {

                                            StorageReference mStorageRef;
                                            mStorageRef = FirebaseStorage.getInstance().getReference("/Profile/" + mAuth.getUid() + "/Profile.jpg");

                                            mStorageRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {

                                                    I.putExtra("ProfileBytes", bytes);
                                                    I.putExtra("Student", Student);
                                                    I.putExtra("UserInformation", Values);
                                                    LoggedIn = true;
                                                    startActivity(I);
                                                    finish();

                                                }
                                            });


                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }, 3000);

                    } catch (Exception e) {
                        if (mAuth.getCurrentUser() != null) {
                            mAuth.signOut();
                        }
                        Intent Restart = new Intent(getApplicationContext(), SplashScreen.class);
                        Restart.putExtra("Student", Student);
                        LoggedIn = true;
                        startActivity(Restart);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_LONG);
                }
            });


        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (LoggedIn) {
                        return;
                    }
                    LoggedIn = true;
                    Intent I = new Intent(SplashScreen.this, Login.class);
                    I.putExtra("Student", Student);
                    startActivity(I);

                    finish();

                }
            }, 5000);

        }
    }

}
