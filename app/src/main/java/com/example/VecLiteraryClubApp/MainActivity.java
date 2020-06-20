package com.example.VecLiteraryClubApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView BotNavBar;
    ValueAnimator BotNavBarColorAnimator;
    NavigationView NavView;
    DrawerLayout Drawer;
    AppBarConfiguration appBarConfiguration;
    FloatingActionButton Help_Float, Menu_Float;
    FirebaseAuth FireAuthentic;
    String Values[];
    DatabaseReference UserRef;
    boolean SignedIn = false;
    boolean Student;


    TextView Nav_View_Name, Nav_View_LCA;
    ImageView Nav_View_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setNavigationBarColor(Color.BLACK);

        Student = getIntent().getBooleanExtra("Student", true);

        BotNavBar = findViewById(R.id.bottom_nav_bar);

        if (Student) {
            BotNavBar.inflateMenu(R.menu.fragment_list_student);
        } else {
            BotNavBar.inflateMenu(R.menu.fragment_list_admin);
        }

        NavView = findViewById(R.id.NavView);
        Drawer = findViewById(R.id.Drawer);
        Help_Float = findViewById(R.id.help_float);
        Menu_Float = findViewById(R.id.menu_float);

        FireAuthentic = FirebaseAuth.getInstance();
        FireAuthentic.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent Sign_Out_Intent = new Intent(MainActivity.this, Login.class);
                    Sign_Out_Intent.putExtra("Student", Student);
                    startActivity(Sign_Out_Intent);
                    finish();
                } else {
                    if (SignedIn) {
                        Toast.makeText(getApplicationContext(), "Unable to sign out", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        final NavController NavCon = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(BotNavBar, NavCon);


        NavCon.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {


                String dest = destination.getLabel().toString();

                int color1 = Color.TRANSPARENT, color2 = 0;
                Drawable background = BotNavBar.getBackground();
                if (background instanceof ColorDrawable) {
                    color1 = ((ColorDrawable) background).getColor();
                }

                if (dest.compareTo("HomeFragment") == 0) {
                    color2 = Color.parseColor("#3b006b");
                } else if (dest.compareTo("EventFragment") == 0) {
                    color2 = Color.parseColor("#001261");
                } else if (dest.compareTo("GalleryFragment") == 0) {
                    color2 = Color.parseColor("#00696b");
                } else if (dest.compareTo("StudentFragment") == 0) {
                    color2 = Color.parseColor("#1b6100");
                }

                BotNavBarColorAnimator = ValueAnimator.ofArgb(color1, color2);
                BotNavBarColorAnimator.setDuration(300);
                BotNavBarColorAnimator.setEvaluator(new ArgbEvaluator());
                BotNavBarColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ColorDrawable cd = new ColorDrawable();
                        cd.setColor((int) valueAnimator.getAnimatedValue());
                        BotNavBar.setBackground(cd);
                        Menu_Float.setBackgroundTintList(ColorStateList.valueOf(cd.getColor()));
                        Help_Float.setBackgroundTintList(ColorStateList.valueOf(cd.getColor()));
                    }
                });
                BotNavBarColorAnimator.start();
            }
        });


        Menu_Float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawer.openDrawer(NavView);
            }
        });

        Help_Float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dest = NavCon.getCurrentDestination().getLabel().toString();

                if (dest.compareTo("HomeFragment") == 0) {

                    Intent AboutIntent = new Intent(MainActivity.this, about_home.class);
                    AboutIntent.putExtra("Student", Student);
                    startActivity(AboutIntent);

                } else if (dest.compareTo("EventFragment") == 0) {

                    Intent AboutIntent = new Intent(MainActivity.this, about_event.class);
                    AboutIntent.putExtra("Student", Student);
                    startActivity(AboutIntent);

                } else if (dest.compareTo("GalleryFragment") == 0) {

                    Intent AboutIntent = new Intent(MainActivity.this, about_gallery.class);
                    AboutIntent.putExtra("Student", Student);
                    startActivity(AboutIntent);

                } else if (dest.compareTo("StudentFragment") == 0) {

                    Intent AboutIntent = new Intent(MainActivity.this, about_student.class);
                    AboutIntent.putExtra("Student", Student);
                    startActivity(AboutIntent);

                }

            }
        });


        NavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                String Lab = menuItem.getTitle().toString();

                if (Lab.compareTo("Profile") == 0) {
                    Intent NavViewIntent = new Intent(MainActivity.this, Profile.class);


                    if (Values[8].compareTo("none") != 0) {

                        NavViewIntent.putExtra("ProfileBytes", getIntent().getByteArrayExtra("ProfileBytes"));
                    }
                    NavViewIntent.putExtra("UserInformation", getIntent().getStringArrayExtra("UserInformation"));
                    NavViewIntent.putExtra("Student", Student);
                    startActivity(NavViewIntent);
                } else if (Lab.compareTo("Sign Out") == 0) {

                    FirebaseAuth.getInstance().signOut();

                } else if (Lab.compareTo("Help") == 0) {
                    Intent NavViewIntent = new Intent(MainActivity.this, about_all.class);
                    NavViewIntent.putExtra("Student", Student);
                    startActivity(NavViewIntent);
                } else if (Lab.compareTo("About") == 0) {
                    Intent NavViewIntent = new Intent(MainActivity.this, about_app_activity.class);
                    NavViewIntent.putExtra("Student", Student);
                    startActivity(NavViewIntent);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Drawer.closeDrawer(NavView);
                    }
                }, 500);

                return false;
            }
        });


        //NavView.inflateHeaderView(R.layout.profile_info_navbar) ;
        View Nav_Header = NavView.getHeaderView(0);
        Nav_View_Name = Nav_Header.findViewById(R.id.nav_view_name);
        Nav_View_LCA = Nav_Header.findViewById(R.id.nav_view_LCA);
        Nav_View_profile = Nav_Header.findViewById(R.id.nav_view_profile);

        Values = getIntent().getStringArrayExtra("UserInformation");

        UserRef = FirebaseDatabase.getInstance().getReference("STUDENT/" + Values[10]);
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    Values[i] = dataSnapshot.child("" + i).getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (Values[8].compareTo("none") != 0) {

            byte ProfileBytes[] = getIntent().getByteArrayExtra("ProfileBytes");
            Nav_View_profile.setImageBitmap(BitmapFactory.decodeByteArray(ProfileBytes, 0, ProfileBytes.length));

        }

        Nav_View_Name.setText("" + Values[0] + " " + Values[1]);
        Nav_View_LCA.setText("" + Values[9].substring(0, 3) + "-" + Values[9].substring(3, 6));


    }


}
