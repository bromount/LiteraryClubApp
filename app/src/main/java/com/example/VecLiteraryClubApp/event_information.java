package com.example.VecLiteraryClubApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class event_information extends AppCompatActivity {

    boolean Student, Registered = false, Attended = false;
    CardView Admin_Actions, Student_Actions_1, Student_Actions_2;
    Button Attend_Event, Register_Venue, Register_Form, View_Stats;
    String EventInformation[], UserInformation[];
    byte[] ImageBytes;
    int width = 0, height = 0;
    DatabaseReference UserRef;


    View.OnClickListener UpdateStateAndReg, RegisterListener, UnregisterListener, AttendListener, AlreadyattendedListener;

    TextView Venue_Venue, Venue_Description, Venue_Reg, Venue_Sch;
    ImageView Venue_Poster;
    TextView Form_Description, Form_Sch;
    TextView Event_Name, Event_Type, Event_State;

    RadioButton OngoingRadio, UpcomingRadio, CompleteRadio;
    CheckBox RegCheckBox;

    CheckBox first, second, third, fourth, CSE, EEE, EIE, ECE, AUTO, CIVIL, MECH, IT, Male, Female, NB;

    LinearLayout venue_layout, form_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information);

        getWindow().setNavigationBarColor(Color.BLACK);

        Student = getIntent().getBooleanExtra("Student", true);
        EventInformation = getIntent().getStringArrayExtra("EventInformation");
        UserInformation = getIntent().getStringArrayExtra("UserInformation");

        View_Stats = findViewById(R.id.event_information_stats);
        View_Stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference("EVENT").child("" + EventInformation[22]);
                EventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ArrayList<String> RegisteredId = new ArrayList<>();
                        ArrayList<String> AttendedId = new ArrayList<>();

                        for (DataSnapshot Child : dataSnapshot.child("R").getChildren()) {
                            RegisteredId.add(Child.getKey());
                        }
                        for (DataSnapshot Child : dataSnapshot.child("A").getChildren()) {
                            AttendedId.add(Child.getKey());
                        }


                        Intent Stats_Intent = new Intent(getApplicationContext(), Event_Statistics.class);
                        Stats_Intent.putExtra("Index", EventInformation[22]);
                        Stats_Intent.putExtra("RegUsers", RegisteredId);
                        Stats_Intent.putExtra("AttendUsers", AttendedId);
                        Stats_Intent.putExtra("Type", EventInformation[21]);
                        startActivity(Stats_Intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        UserRef = FirebaseDatabase.getInstance().getReference("STUDENT/" + UserInformation[10]);

        Form_Description = findViewById(R.id.event_information_form_description);
        Form_Sch = findViewById(R.id.event_information_form_schedule);

        Venue_Venue = findViewById(R.id.event_information_venue_venue);
        Venue_Description = findViewById(R.id.event_information_venue_description);
        Venue_Poster = findViewById(R.id.event_information_venue_poster);
        Venue_Reg = findViewById(R.id.event_information_venue_register);
        Venue_Sch = findViewById(R.id.event_information_venue_schedule);

        Event_Name = findViewById(R.id.event_information_name);
        Event_Name.setSelected(true);
        Event_State = findViewById(R.id.event_information_state);
        Event_Type = findViewById(R.id.event_information_type);

        RegCheckBox = findViewById(R.id.event_information_reg_enabled);
        UpcomingRadio = findViewById(R.id.event_information_state_upcoming);
        OngoingRadio = findViewById(R.id.event_information_state_ongoing);
        CompleteRadio = findViewById(R.id.event_information_state_complete);

        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);

        CSE = findViewById(R.id.CSE);
        ECE = findViewById(R.id.ECE);
        EEE = findViewById(R.id.EEE);
        EIE = findViewById(R.id.EIE);
        AUTO = findViewById(R.id.AUTO);
        MECH = findViewById(R.id.MECH);
        CIVIL = findViewById(R.id.CIVIL);
        IT = findViewById(R.id.IT);

        Male = findViewById(R.id.male);
        Female = findViewById(R.id.female);
        NB = findViewById(R.id.non_binary);


        first.setClickable(false);
        second.setClickable(false);
        third.setClickable(false);
        fourth.setClickable(false);

        CSE.setClickable(false);
        ECE.setClickable(false);
        EEE.setClickable(false);
        EIE.setClickable(false);
        AUTO.setClickable(false);
        MECH.setClickable(false);
        CIVIL.setClickable(false);
        IT.setClickable(false);

        Male.setClickable(false);
        Female.setClickable(false);
        NB.setClickable(false);

        if (EventInformation[6].compareTo("0") == 0) {
            first.setChecked(false);
        }
        if (EventInformation[7].compareTo("0") == 0) {
            second.setChecked(false);
        }
        if (EventInformation[8].compareTo("0") == 0) {
            third.setChecked(false);
        }
        if (EventInformation[9].compareTo("0") == 0) {
            fourth.setChecked(false);
        }
        if (EventInformation[10].compareTo("0") == 0) {
            CSE.setChecked(false);
        }
        if (EventInformation[11].compareTo("0") == 0) {
            ECE.setChecked(false);
        }
        if (EventInformation[12].compareTo("0") == 0) {
            EEE.setChecked(false);
        }
        if (EventInformation[13].compareTo("0") == 0) {
            EIE.setChecked(false);
        }
        if (EventInformation[14].compareTo("0") == 0) {
            AUTO.setChecked(false);
        }
        if (EventInformation[15].compareTo("0") == 0) {
            MECH.setChecked(false);
        }
        if (EventInformation[16].compareTo("0") == 0) {
            CIVIL.setChecked(false);
        }
        if (EventInformation[17].compareTo("0") == 0) {
            IT.setChecked(false);
        }
        if (EventInformation[18].compareTo("0") == 0) {
            Male.setChecked(false);
        }
        if (EventInformation[19].compareTo("0") == 0) {
            Female.setChecked(false);
        }
        if (EventInformation[20].compareTo("0") == 0) {
            NB.setChecked(false);
        }


        Attend_Event = findViewById(R.id.event_information_attend_1);
        Register_Venue = findViewById(R.id.event_information_register_2);
        Register_Form = findViewById(R.id.event_information_register_1);

        venue_layout = findViewById(R.id.event_information_venue_layout);
        form_layout = findViewById(R.id.event_information_form_layout);

        Admin_Actions = findViewById(R.id.event_information_action_admin);
        Student_Actions_1 = findViewById(R.id.event_information_action_stud_1);
        Student_Actions_2 = findViewById(R.id.event_information_action_stud_2);

        Admin_Actions.setVisibility(View.INVISIBLE);
        Student_Actions_1.setVisibility(View.INVISIBLE);
        Student_Actions_2.setVisibility(View.INVISIBLE);

        if (Student) {
            if (EventInformation[21].compareTo("Form") == 0) {
                Student_Actions_1.setVisibility(View.VISIBLE);
            } else if (EventInformation[21].compareTo("Venue") == 0) {
                Student_Actions_2.setVisibility(View.VISIBLE);
            }
        } else {
            Admin_Actions.setVisibility(View.VISIBLE);
        }

        venue_layout.setVisibility(View.INVISIBLE);
        form_layout.setVisibility(View.INVISIBLE);


        if (EventInformation[21].compareTo("Form") == 0) {
            form_layout.setVisibility(View.VISIBLE);
        } else if (EventInformation[21].compareTo("Venue") == 0) {
            venue_layout.setVisibility(View.VISIBLE);
        }


        Event_Name.setText(EventInformation[0]);
        Event_State.setText(EventInformation[5]);
        Event_Type.setText(EventInformation[21]);

        if (EventInformation[21].compareTo("Venue") == 0) {

            Venue_Venue.setText(EventInformation[1]);
            Venue_Sch.setText(EventInformation[4]);
            Venue_Reg.setText(EventInformation[3]);
            Venue_Description.setText(EventInformation[2]);

            StorageReference PosterRef = FirebaseStorage.getInstance().getReference("EVENT/POSTERS/" + EventInformation[22]);
            PosterRef.getBytes(10240000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {

                    Bitmap Poster = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Venue_Poster.setImageBitmap(Poster);
                    ImageBytes = bytes.clone();
                    width = 0;
                    height = 0;
                    Bitmap Image = BitmapFactory.decodeByteArray(ImageBytes, 0, ImageBytes.length);
                    if (bytes.length > 350000) {
                        if (Image.getWidth() > Image.getHeight()) {
                            Image = Bitmap.createScaledBitmap(Image, 720, (int) (((float) Image.getHeight() * 720) / ((float) Image.getWidth())), true);
                            width = Image.getWidth();
                            height = Image.getHeight();

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            int i = 100;
                            while (ImageBytes.length > 350000) {
                                Image.compress(Bitmap.CompressFormat.JPEG, i, stream);
                                ImageBytes = stream.toByteArray();
                                i -= 10;
                            }
                        } else {
                            Image = Bitmap.createScaledBitmap(Image, (int) (((float) Image.getWidth() * 720) / ((float) Image.getHeight())), 720, true);
                            width = Image.getWidth();
                            height = Image.getHeight();

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            int i = 100;
                            while (ImageBytes.length > 350000) {
                                Image.compress(Bitmap.CompressFormat.JPEG, i, stream);
                                ImageBytes = stream.toByteArray();
                                i -= 10;
                            }
                        }
                    }
                    Venue_Poster.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent View_Image_Intent = new Intent(getApplicationContext(), VIew_Image.class);
                            View_Image_Intent.putExtra("ImageBytes", ImageBytes);
                            Toast.makeText(getApplicationContext(), "" + ImageBytes.length, Toast.LENGTH_LONG).show();
                            Log.d("Trans", "" + ImageBytes.length + " w - " + width + " h - " + height);
                            startActivity(View_Image_Intent);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed To Load Poster", Toast.LENGTH_LONG).show();
                }
            });

        } else if (EventInformation[21].compareTo("Form") == 0) {

            Form_Description.setText(EventInformation[2]);
            Form_Sch.setText(EventInformation[4]);

        }


        UpdateStateAndReg = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String State, Registration;
                if (OngoingRadio.isChecked()) {
                    State = "Ongoing";
                } else if (UpcomingRadio.isChecked()) {
                    State = "Upcoming";
                } else {
                    RegCheckBox.setChecked(false);
                    State = "Complete";
                }

                if (RegCheckBox.isChecked()) {
                    Registration = "1";
                } else {
                    Registration = "0";
                }

                DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference("EVENT/" + EventInformation[22]);
                EventRef.child("5").setValue(State).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Unable To Update State", Toast.LENGTH_LONG).show();
                    }
                });
                EventRef.child("23").setValue(Registration).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Unable To Update Registration", Toast.LENGTH_LONG).show();
                    }
                });

            }
        };

        if (EventInformation[23].compareTo("1") == 0) {
            RegCheckBox.setChecked(true);
        } else {
            RegCheckBox.setChecked(false);
        }

        //OngoingRadio.setChecked(false);
        //UpcomingRadio.setChecked(false);
        //CompleteRadio.setChecked(false);

        if (EventInformation[5].compareTo("Ongoing") == 0) {
            OngoingRadio.setChecked(true);
        } else if (EventInformation[5].compareTo("Upcoming") == 0) {
            UpcomingRadio.setChecked(true);
        } else {
            CompleteRadio.setChecked(true);
        }

        UpcomingRadio.setOnClickListener(UpdateStateAndReg);
        OngoingRadio.setOnClickListener(UpdateStateAndReg);
        CompleteRadio.setOnClickListener(UpdateStateAndReg);
        RegCheckBox.setOnClickListener(UpdateStateAndReg);

        final DatabaseReference EventReference = FirebaseDatabase.getInstance().getReference("EVENT/" + EventInformation[22]);
        final DatabaseReference StudentReference = FirebaseDatabase.getInstance().getReference("STUDENT/" + FirebaseAuth.getInstance().getUid());

        AttendListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Registered) {
                    Toast.makeText(getApplicationContext(), "Not Registered", Toast.LENGTH_LONG).show();
                } else if (EventInformation[5].compareTo("Ongoing") == 0) {
                    EventReference.child("A/" + FirebaseAuth.getInstance().getUid()).setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            int Att = Integer.parseInt(UserInformation[12]);
                            Att += 1;
                            UserRef.child("12").setValue("" + Att);
                            UserInformation[12] = "" + Att;
                            Intent Form_Intent = new Intent(getApplicationContext(), form_answer.class);
                            Form_Intent.putExtra("URL", EventInformation[1]);
                            startActivity(Form_Intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Unable To Attend", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Event Not Live", Toast.LENGTH_LONG).show();
                }

            }
        };

        AlreadyattendedListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Already Attended", Toast.LENGTH_LONG).show();

            }
        };

        UnregisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Attended) {
                    Toast.makeText(getApplicationContext(), "Already Attended", Toast.LENGTH_LONG).show();
                } else if (EventInformation[23].compareTo("1") == 0) {
                    EventReference.child("R/" + FirebaseAuth.getInstance().getUid()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            int Regs = Integer.parseInt(UserInformation[11]);
                            Regs -= 1;
                            UserRef.child("11").setValue("" + Regs);
                            UserInformation[11] = "" + Regs;
                            Toast.makeText(getApplicationContext(), "Un-Registered", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Unable to Un-Register", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Registrations Closed", Toast.LENGTH_LONG).show();
                }
            }
        };

        RegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EventInformation[23].compareTo("1") == 0) {
                    EventReference.child("R/" + FirebaseAuth.getInstance().getUid()).setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
                            int Regs = Integer.parseInt(UserInformation[11]);
                            Regs += 1;
                            UserRef.child("11").setValue("" + Regs);
                            UserInformation[11] = "" + Regs;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Unable to Register", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Registrations Closed", Toast.LENGTH_LONG).show();
                }
            }
        };

        EventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("R/" + FirebaseAuth.getInstance().getUid().toString()).getValue() == null) {
                    Registered = false;
                    Register_Form.setBackgroundColor(Color.rgb(57, 192, 57));
                    Register_Venue.setBackgroundColor(Color.rgb(57, 192, 57));
                    Register_Venue.setText("REGISTER");
                    Register_Form.setText("REGISTER");
                    Register_Venue.setOnClickListener(RegisterListener);
                    Register_Form.setOnClickListener(RegisterListener);
                } else {
                    Registered = true;
                    Register_Form.setBackgroundColor(Color.rgb(192, 57, 57));
                    Register_Venue.setBackgroundColor(Color.rgb(192, 57, 57));
                    Register_Venue.setText("UN-REGISTER");
                    Register_Form.setText("UN-REGISTER");
                    Register_Venue.setOnClickListener(UnregisterListener);
                    Register_Form.setOnClickListener(UnregisterListener);
                }

                if (dataSnapshot.child("A/" + FirebaseAuth.getInstance().getUid().toString()).getValue() == null) {
                    Attended = false;
                    Attend_Event.setBackgroundColor(Color.rgb(57, 192, 57));
                    Attend_Event.setText("GO TO FORM");
                    Attend_Event.setOnClickListener(AttendListener);
                } else {
                    Attended = true;
                    Attend_Event.setBackgroundColor(Color.rgb(192, 57, 57));
                    Attend_Event.setText("ALREADY ATTENDED");
                    Attend_Event.setOnClickListener(AlreadyattendedListener);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
