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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class add_event extends AppCompatActivity {

    final int PICK_FILE_REQUEST = 39;

    Spinner Event_Types;
    ArrayAdapter Event_Types_Adapter;
    View venue_layout, form_layout, submit_layout, construction_loli_layout;
    CardView add_event_loading;

    EditText venue_name, venue_description, venue_venue, venue_schedule, venue_registration;
    CheckBox venue_first, venue_second, venue_third, venue_fourth, venue_CSE, venue_EEE, venue_EIE, venue_ECE, venue_AUTO, venue_CIVIL, venue_MECH, venue_IT, venue_Male, venue_Female, venue_NB;
    CheckBox submit_first, submit_second, submit_third, submit_fourth, submit_CSE, submit_EEE, submit_EIE, submit_ECE, submit_AUTO, submit_CIVIL, submit_IT, submit_MECH, submit_Male, submit_Female, submit_NB;
    EditText submit_name, submit_description, submit_schedule, submit_registration;
    CheckBox form_first, form_second, form_third, form_fourth, form_CSE, form_EEE, form_EIE, form_IT, form_ECE, form_AUTO, form_CIVIL, form_MECH, form_Male, form_Female, form_NB;
    EditText form_name, form_description, form_URL, form_schedule, form_registration;

    WebView form_webview;
    WebViewClientCustom custom_client;

    boolean Upload_started = false;

    Button submit_venue, submit_submit, submit_form, load_form;

    ImageView venue_poster;
    byte PosterArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getWindow().setNavigationBarColor(Color.BLACK);

        PosterArray = new byte[1];

        venue_poster = findViewById(R.id.venue_poster);
        form_webview = findViewById(R.id.form_webview);
        add_event_loading = findViewById(R.id.add_event_loading);
        form_URL = findViewById(R.id.form_URL);
        load_form = findViewById(R.id.form_reload_button);


        custom_client = new WebViewClientCustom();

        form_webview.canGoBackOrForward(0);
        form_webview.setVerticalScrollBarEnabled(true);
        form_webview.getSettings().setJavaScriptEnabled(true);
        form_webview.setWebViewClient(custom_client);


        form_webview.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSeLQR8H89nmZF7k3a1XqE5m1Rgkn5I_X-haJLcSH-Yr6xbTIQ/viewform");

        load_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (form_URL.length() != 0) {
                    form_webview.loadUrl(form_URL.getText().toString());
                }
            }
        });

        venue_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);


            }
        });

        venue_layout = findViewById(R.id.event_venue_layout);
        form_layout = findViewById(R.id.event_form_layout);
        submit_layout = findViewById(R.id.event_submit_layout);
        construction_loli_layout = findViewById(R.id.event_construction_loli);

        submit_venue = findViewById(R.id.venue_submit_button);
        submit_form = findViewById(R.id.form_submit_button);

        Event_Types = findViewById(R.id.Event_Types);
        Event_Types_Adapter = ArrayAdapter.createFromResource(this, R.array.event_types, R.layout.custom_spinner_events);
        Event_Types_Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_events);
        Event_Types.setAdapter(Event_Types_Adapter);

        Event_Types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                venue_layout.setVisibility(View.INVISIBLE);
                form_layout.setVisibility(View.INVISIBLE);
                submit_layout.setVisibility(View.INVISIBLE);
                construction_loli_layout.setVisibility(View.INVISIBLE);
                switch (i) {
                    case 0:
                        venue_layout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        construction_loli_layout.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        form_layout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit_venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleVenue();
            }
        });

        submit_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleForm();
            }
        });

    }

    void handleVenue() {

        add_event_loading.setVisibility(View.VISIBLE);

        venue_name = findViewById(R.id.venue_name);
        venue_description = findViewById(R.id.venue_description);
        venue_venue = findViewById(R.id.venue_venue);
        venue_registration = findViewById(R.id.venue_registration);
        venue_schedule = findViewById(R.id.venue_schedule);
        venue_first = findViewById(R.id.venue_first);
        venue_second = findViewById(R.id.venue_second);
        venue_third = findViewById(R.id.venue_third);
        venue_fourth = findViewById(R.id.venue_fourth);

        venue_CSE = findViewById(R.id.venue_CSE);
        venue_ECE = findViewById(R.id.venue_ECE);
        venue_EEE = findViewById(R.id.venue_EEE);
        venue_EIE = findViewById(R.id.venue_EIE);
        venue_AUTO = findViewById(R.id.venue_AUTO);
        venue_MECH = findViewById(R.id.venue_MECH);
        venue_CIVIL = findViewById(R.id.venue_CIVIL);
        venue_IT = findViewById(R.id.venue_IT);

        venue_Male = findViewById(R.id.venue_male);
        venue_Female = findViewById(R.id.venue_female);
        venue_NB = findViewById(R.id.venue_non_binary);

        if (venue_name.length() == 0 || venue_description.length() == 0
                || venue_venue.length() == 0 || venue_registration.length() == 0 ||
                venue_schedule.length() == 0 || PosterArray.length == 1) {

            Toast.makeText(getApplicationContext(), "Enter All Values", Toast.LENGTH_SHORT).show();
            add_event_loading.setVisibility(View.INVISIBLE);
            return;

        }

        final String Values[] = new String[24];
        Values[0] = venue_name.getText().toString();
        Values[1] = venue_venue.getText().toString();
        Values[2] = venue_description.getText().toString();
        Values[3] = venue_registration.getText().toString();
        Values[4] = venue_schedule.getText().toString();
        Values[5] = "Upcoming";
        Values[21] = "Venue";
        Values[23] = "1";

        for (int i = 6; i < 21; i++) {
            Values[i] = "0";
        }

        if (venue_first.isChecked()) {
            Values[6] = "" + 1;
        }
        if (venue_second.isChecked()) {
            Values[7] = "" + 1;
        }
        if (venue_third.isChecked()) {
            Values[8] = "" + 1;
        }
        if (venue_fourth.isChecked()) {
            Values[9] = "" + 1;
        }
        if (venue_CSE.isChecked()) {
            Values[10] = "" + 1;
        }
        if (venue_ECE.isChecked()) {
            Values[11] = "" + 1;
        }
        if (venue_EEE.isChecked()) {
            Values[12] = "" + 1;
        }
        if (venue_IT.isChecked()) {
            Values[13] = "" + 1;
        }
        if (venue_MECH.isChecked()) {
            Values[14] = "" + 1;
        }
        if (venue_AUTO.isChecked()) {
            Values[15] = "" + 1;
        }
        if (venue_EIE.isChecked()) {
            Values[16] = "" + 1;
        }
        if (venue_CIVIL.isChecked()) {
            Values[17] = "" + 1;
        }
        if (venue_Male.isChecked()) {
            Values[18] = "" + 1;
        }
        if (venue_Female.isChecked()) {
            Values[19] = "" + 1;
        }
        if (venue_NB.isChecked()) {
            Values[20] = "" + 1;
        }

        final DatabaseReference EventRoot = FirebaseDatabase.getInstance().getReference("EVENT");

        EventRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (Upload_started) {
                    return;
                }
                Upload_started = true;

                final long Event_ID = dataSnapshot.getChildrenCount();

                Values[22] = "" + Event_ID;

                final DatabaseReference Event_Location = EventRoot.child("" + Event_ID);
                Event_Location.setValue(Arrays.asList(Values)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        StorageReference mStorageRef;
                        mStorageRef = FirebaseStorage.getInstance().getReference("EVENT/POSTERS/" + Event_ID);
                        mStorageRef.putBytes(PosterArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                add_event_loading.setVisibility(View.INVISIBLE);
                                Event_Location.removeValue();
                                Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                add_event_loading.setVisibility(View.INVISIBLE);
            }
        });


    }


    void handleForm() {


        add_event_loading.setVisibility(View.VISIBLE);

        form_name = findViewById(R.id.form_name);
        form_description = findViewById(R.id.form_description);
        form_schedule = findViewById(R.id.form_schedule);
        form_first = findViewById(R.id.form_first);
        form_second = findViewById(R.id.form_second);
        form_third = findViewById(R.id.form_third);
        form_fourth = findViewById(R.id.form_fourth);

        form_CSE = findViewById(R.id.form_CSE);
        form_ECE = findViewById(R.id.form_ECE);
        form_EEE = findViewById(R.id.form_EEE);
        form_EIE = findViewById(R.id.form_EIE);
        form_AUTO = findViewById(R.id.form_AUTO);
        form_MECH = findViewById(R.id.form_MECH);
        form_CIVIL = findViewById(R.id.form_CIVIL);
        form_IT = findViewById(R.id.form_IT);

        form_Male = findViewById(R.id.form_male);
        form_Female = findViewById(R.id.form_female);
        form_NB = findViewById(R.id.form_non_binary);

        if (form_name.length() == 0 || form_description.length() == 0
                || form_URL.length() == 0 || form_schedule.length() == 0) {

            Toast.makeText(getApplicationContext(), "Enter All Values", Toast.LENGTH_SHORT).show();
            add_event_loading.setVisibility(View.INVISIBLE);
            return;

        }

        final String Values[] = new String[24];
        Values[0] = form_name.getText().toString();
        Values[1] = form_URL.getText().toString();
        Values[2] = form_description.getText().toString();
        Values[3] = " "; //form_registration.getText().toString();
        Values[4] = form_schedule.getText().toString();
        Values[5] = "Upcoming";
        Values[21] = "Form";
        Values[23] = "1";

        for (int i = 6; i < 21; i++) {
            Values[i] = "0";
        }

        if (form_first.isChecked()) {
            Values[6] = "" + 1;
        }
        if (form_second.isChecked()) {
            Values[7] = "" + 1;
        }
        if (form_third.isChecked()) {
            Values[8] = "" + 1;
        }
        if (form_fourth.isChecked()) {
            Values[9] = "" + 1;
        }
        if (form_CSE.isChecked()) {
            Values[10] = "" + 1;
        }
        if (form_ECE.isChecked()) {
            Values[11] = "" + 1;
        }
        if (form_EEE.isChecked()) {
            Values[12] = "" + 1;
        }
        if (form_IT.isChecked()) {
            Values[13] = "" + 1;
        }
        if (form_MECH.isChecked()) {
            Values[14] = "" + 1;
        }
        if (form_AUTO.isChecked()) {
            Values[15] = "" + 1;
        }
        if (form_EIE.isChecked()) {
            Values[16] = "" + 1;
        }
        if (form_CIVIL.isChecked()) {
            Values[17] = "" + 1;
        }
        if (form_Male.isChecked()) {
            Values[18] = "" + 1;
        }
        if (form_Female.isChecked()) {
            Values[19] = "" + 1;
        }
        if (form_NB.isChecked()) {
            Values[20] = "" + 1;
        }

        final DatabaseReference EventRoot = FirebaseDatabase.getInstance().getReference("EVENT");

        EventRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (Upload_started) {
                    return;
                }
                Upload_started = true;

                final long Event_ID = dataSnapshot.getChildrenCount();

                Values[22] = "" + Event_ID;

                final DatabaseReference Event_Location = EventRoot.child("" + Event_ID);
                Event_Location.setValue(Arrays.asList(Values)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                add_event_loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    void handleSubmit() {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && data != null) {
            if (resultCode == RESULT_OK) {
                Uri file = data.getData();
                try {

                    InputStream is = getContentResolver().openInputStream(file);
                    Bitmap Poster_Bitmap = BitmapFactory.decodeStream(is);

                    venue_poster.setImageBitmap(Poster_Bitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Poster_Bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    PosterArray = stream.toByteArray();

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Failed to get file", Toast.LENGTH_LONG).show();
                    add_event_loading.setVisibility(View.INVISIBLE);
                }
            }

        } else {
            add_event_loading.setVisibility(View.INVISIBLE);
        }
    }
}

