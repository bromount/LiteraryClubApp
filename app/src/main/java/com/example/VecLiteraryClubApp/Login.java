package com.example.VecLiteraryClubApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Intent I;
    boolean STUDENT = true;
    boolean LoggedIn = false;

    String Values[];
    CardView loading;
    Spinner year_spinner, course_spinner, gender_spinner, section_spinner;
    ArrayAdapter<CharSequence> year_adapter, course_adapter, gender_adapter, section_adapter;
    int Gender_Index, Course_Index, Year_Index, Section_Index;
    Button Sign_In, Sign_Up;
    EditText Sign_In_Email, Sign_In_Password, Sign_Up_Email, Sign_Up_Password, Sign_Up_Fname, Sign_Up_Lname;
    DataSnapshot dataSnapshot_Student_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setNavigationBarColor(Color.BLACK);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loading = findViewById(R.id.sign_loading);


        year_spinner = findViewById(R.id.year_spinner);
        year_adapter = ArrayAdapter.createFromResource(this, R.array.years, R.layout.custom_spinner_login);
        year_adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_login);
        year_spinner.setAdapter(year_adapter);

        course_spinner = findViewById(R.id.course_spinner);
        course_adapter = ArrayAdapter.createFromResource(this, R.array.courses, R.layout.custom_spinner_login);
        course_adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_login);
        course_spinner.setAdapter(course_adapter);

        gender_spinner = findViewById(R.id.gender_spinner);
        gender_adapter = ArrayAdapter.createFromResource(this, R.array.genders, R.layout.custom_spinner_login);
        gender_adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_login);
        gender_spinner.setAdapter(gender_adapter);

        section_spinner = findViewById(R.id.section_spinner);
        section_adapter = ArrayAdapter.createFromResource(this, R.array.sections, R.layout.custom_spinner_login);
        section_adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_login);
        section_spinner.setAdapter(section_adapter);


        Values = new String[14];


        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Year_Index = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        course_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Course_Index = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Gender_Index = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        section_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Section_Index = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Sign_In = findViewById(R.id.sign_in);
        Sign_In_Email = findViewById(R.id.Sign_In_Email);
        Sign_In_Password = findViewById(R.id.Sign_In_Password);

        Sign_Up = findViewById(R.id.sign_up_button);
        Sign_Up_Email = findViewById(R.id.Sign_Up_Email);
        Sign_Up_Password = findViewById(R.id.Sign_Up_Password);
        Sign_Up_Fname = findViewById(R.id.Sign_Up_Fname);
        Sign_Up_Lname = findViewById(R.id.Sign_Up_Lname);


        mAuth = FirebaseAuth.getInstance();


        Sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loading.setVisibility(View.VISIBLE);

                if (Sign_In_Email.length() == 0 || Sign_In_Password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Credentials", Toast.LENGTH_SHORT).show();

                    loading.setVisibility(View.INVISIBLE);

                    return;
                }

                String Email, Password;
                Email = Sign_In_Email.getText().toString();
                Password = Sign_In_Password.getText().toString();

                SignIn(Email, Password);

            }
        });

        Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loading.setVisibility(View.VISIBLE);

                if (Sign_Up_Email.length() == 0 || Sign_Up_Password.length() == 0
                        || Sign_Up_Fname.length() == 0 || Sign_Up_Lname.length() == 0
                ) {
                    Toast.makeText(getApplicationContext(), "Enter Credentials", Toast.LENGTH_SHORT).show();

                    loading.setVisibility(View.INVISIBLE);
                    return;
                }

                String Email, Password, Fname, Lname, Course, Gender, Uid, Section = "Z";
                int Year, LCA;


                switch (Gender_Index) {
                    case 0:
                        Gender = "Male";
                        break;
                    case 1:
                        Gender = "Female";
                        break;
                    case 2:
                        Gender = "Non Binary";
                        break;
                    default:
                        Gender = "Undefined";
                }

                switch (Course_Index) {
                    case 0:
                        Course = "CSE";
                        break;
                    case 1:
                        Course = "ECE";
                        break;
                    case 2:
                        Course = "MECH";
                        break;
                    case 3:
                        Course = "AUTO";
                        break;
                    case 4:
                        Course = "CIVIL";
                        break;
                    default:
                        Course = "Undefined";
                        break;
                }


                switch (Section_Index) {
                    case 0:
                        Section = "A";
                        break;
                    case 1:
                        Section = "B";
                        break;
                    case 2:
                        Section = "C";
                        break;
                    case 3:
                        Section = "D";
                        break;
                    case 4:
                        Section = "E";
                        break;
                    case 5:
                        Section = "F";
                        break;
                    case 6:
                        Section = "G";
                        break;
                    case 7:
                        Section = "H";
                        break;
                    default:
                        Section = "Undefined";
                        break;
                }

                Year = Year_Index + 1;


                Email = Sign_Up_Email.getText().toString();
                Password = Sign_Up_Password.getText().toString();
                Fname = Sign_Up_Fname.getText().toString();
                Lname = Sign_Up_Lname.getText().toString();


                Values[0] = Fname;
                Values[1] = Lname;
                Values[2] = Course;
                Values[3] = Section;
                Values[4] = "" + Year;
                Values[5] = Email;
                Values[6] = Password;
                Values[7] = Gender;
                Values[8] = "none";
                Values[11] = "" + 0;
                Values[12] = "" + 0;
                Values[13] = "Temp";


                SignUp(Email, Password);


            }
        });

    }

    void SignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference StudentRef = database.getReference("STUDENT/" + mAuth.getUid());

                            StudentRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    dataSnapshot_Student_info = dataSnapshot;

                                    database.getReference("ADMINS").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child(FirebaseAuth.getInstance().getUid()).getValue() != null) {
                                                STUDENT = false;
                                            }

                                            startMainActivity(dataSnapshot_Student_info);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_LONG);

                                    loading.setVisibility(View.INVISIBLE);
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            loading.setVisibility(View.INVISIBLE);

                        }


                    }
                });
    }

    void SignUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();


                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference LCARef = database.getReference("LCA");
                            final DatabaseReference StudentRef = database.getReference("STUDENT/" + mAuth.getUid());

                            LCARef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                    int LCA = 0;
                                    boolean available = false;

                                    while (!available) {

                                        available = true;
                                        LCA = StudentData.generateLCA();
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                                            if (LCA == Integer.parseInt(child.getValue().toString())) {
                                                available = false;
                                                break;
                                            }

                                        }
                                    }

                                    Values[9] = "" + LCA;
                                    Values[10] = mAuth.getUid();

                                    StudentRef.setValue(Arrays.asList(Values)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startMainActivity(dataSnapshot);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "User Data Error", Toast.LENGTH_LONG);
                                            mAuth.getCurrentUser().delete();

                                            loading.setVisibility(View.INVISIBLE);
                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_LONG);

                                    loading.setVisibility(View.INVISIBLE);
                                }
                            });


                        } else {

                            Toast.makeText(getApplicationContext(), "Unable to create account", Toast.LENGTH_LONG).show();

                            loading.setVisibility(View.INVISIBLE);

                        }


                    }
                });
    }

    void startMainActivity(DataSnapshot DS) {

        if (LoggedIn) {
            return;
        }

        I = new Intent(Login.this, MainActivity.class);
        I.putExtra("Student", STUDENT);

        int i = 0;
        for (DataSnapshot child : DS.getChildren()) {
            Values[i] = child.getValue().toString();
            i++;
        }

        if (Values[8].compareTo("none") == 0) {

            I.putExtra("UserInformation", Values);
            LoggedIn = true;
            startActivity(I);
            finish();

        } else {

            Toast.makeText(getApplicationContext(), "Profile/" + mAuth.getUid() + "/Profile", Toast.LENGTH_LONG);


            StorageReference mStorageRef;
            mStorageRef = FirebaseStorage.getInstance().getReference("/Profile/" + Values[10] + "/Profile.jpg");
            mStorageRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {

                    I.putExtra("ProfileBytes", bytes);

                    I.putExtra("UserInformation", Values);
                    LoggedIn = true;
                    startActivity(I);
                    finish();

                }
            });
        }
    }
}
