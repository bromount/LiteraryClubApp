package com.example.VecLiteraryClubApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Event_Statistics extends AppCompatActivity {

    String Index;
    RecyclerView Attended, NotAttended, Registered, NotRegistered;
    View.OnClickListener StudentRecyclerClick;
    StudentRecyclerAdapter AttendedAdapter, NotAttendedAdapter, RegisteredAdapter, NotRegisteredAdapter;
    ArrayList<StudentRecyclerDataStructure> DataAttended, DataNotAttended, DataRegistered, DataNotRegistered, DataDefault;
    boolean Register = false;
    CardView RegCard, AttendCard;

    ArrayList<Task<byte[]>> TaskList1, TaskList2, TaskList3, TaskList4;
    ArrayList<String> RegisteredID, AttendedID;

    DatabaseReference DatabaseRef;
    StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__statistics);

        getWindow().setNavigationBarColor(Color.BLACK);

        DataDefault = new ArrayList<>();
        DataAttended = new ArrayList<>();
        DataNotAttended = new ArrayList<>();
        DataNotRegistered = new ArrayList<>();
        DataRegistered = new ArrayList<>();
        TaskList1 = new ArrayList<>();
        TaskList2 = new ArrayList<>();
        TaskList3 = new ArrayList<>();
        TaskList4 = new ArrayList<>();
        RegisteredID = new ArrayList<>();
        AttendedID = new ArrayList<>();

        RegCard = findViewById(R.id.register_card);
        AttendCard = findViewById(R.id.attend_card);

        if ((getIntent().getStringExtra("Type").compareTo("Form") == 0)) {
            AttendCard.setVisibility(View.VISIBLE);
            RegCard.setVisibility(View.INVISIBLE);
            Register = false;
        } else {
            AttendCard.setVisibility(View.INVISIBLE);
            RegCard.setVisibility(View.VISIBLE);
            Register = true;
        }

        RegisteredID = getIntent().getStringArrayListExtra("RegUsers");
        AttendedID = getIntent().getStringArrayListExtra("AttendUsers");

        StudentRecyclerClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Values_to_Intent[] = new String[14];
                byte Profile_to_Intent[] = new byte[1];

                String YCS = ((TextView) view.findViewById(R.id.recycler_YCS)).getText().toString();
                String LCA = ((TextView) view.findViewById(R.id.recycler_LCA)).getText().toString();

                for (int i = 0; i < DataAttended.size(); i++) {

                    if (DataAttended.get(i).LCA.compareTo(LCA) == 0) {
                        Values_to_Intent = DataAttended.get(i).Vals;
                        Profile_to_Intent = DataAttended.get(i).ProfileArray;
                    }

                }

                for (int i = 0; i < DataNotAttended.size(); i++) {
                    if (DataNotAttended.get(i).LCA.compareTo(LCA) == 0) {
                        Values_to_Intent = DataNotAttended.get(i).Vals;
                        Profile_to_Intent = DataNotAttended.get(i).ProfileArray;
                    }
                }

                for (int i = 0; i < DataRegistered.size(); i++) {
                    if (DataRegistered.get(i).LCA.compareTo(LCA) == 0) {
                        Values_to_Intent = DataRegistered.get(i).Vals;
                        Profile_to_Intent = DataRegistered.get(i).ProfileArray;
                    }
                }

                for (int i = 0; i < DataNotRegistered.size(); i++) {
                    if (DataNotRegistered.get(i).LCA.compareTo(LCA) == 0) {
                        Values_to_Intent = DataNotRegistered.get(i).Vals;
                        Profile_to_Intent = DataNotRegistered.get(i).ProfileArray;
                    }
                }

                Intent StudentInfoIntent = new Intent(getApplicationContext(), student_information.class);

                StudentInfoIntent.putExtra("ProfileBytes", Profile_to_Intent);
                StudentInfoIntent.putExtra("UserInformation", Values_to_Intent);

                startActivity(StudentInfoIntent);

            }
        };

        String TempValues[] = new String[14];
        for (int i = 0; i < 14; i++) {
            TempValues[i] = "none";
        }

        DataDefault.add(new StudentRecyclerDataStructure("None", " - ", " - ", TempValues));


        Index = getIntent().getStringExtra("Index");
        Attended = findViewById(R.id.attended_recycler);
        NotAttended = findViewById(R.id.notattended_recycler);
        Registered = findViewById(R.id.registered_recycler);
        NotRegistered = findViewById(R.id.notregistered_recycler);

        Attended.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        NotAttended.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Registered.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        NotRegistered.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DatabaseRef = FirebaseDatabase.getInstance().getReference("STUDENT");
        DatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                TaskList2.clear();
                TaskList1.clear();
                DataAttended.clear();
                DataNotAttended.clear();

                for (DataSnapshot Child : dataSnapshot.getChildren()) {

                    String Name = Child.child("0").getValue().toString() + " " + Child.child("1").getValue().toString();
                    String LCA = Child.child("9").getValue().toString();
                    LCA = LCA.substring(0, 3) + "-" + LCA.substring(3, 6);
                    String YCS = Child.child("4").getValue().toString() + " / " + Child.child("2").getValue().toString() + " / " + Child.child("3").getValue().toString();
                    byte profile[] = new byte[1];
                    String ProfileURL = "/Profile/" + Child.child("10").getValue().toString() + "/Profile.jpg";

                    String Values[] = new String[14];
                    for (int i = 0; i < 14; i++) {
                        Values[i] = Child.child("" + i).getValue().toString();
                    }

                    if (Child.child("4").getValue().toString().compareTo("-") == 0) {
                        continue;
                    }

                    if (AttendedID.contains(Child.getKey())) {
                        if (!Register) {
                            DataAttended.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                            TaskList1.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                            TaskList1.get(TaskList1.size() - 1).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    for (int i = 0; i < TaskList1.size(); i++) {
                                        if (TaskList1.get(i).isSuccessful() && AttendedAdapter.Data.get(i).ProfileArray.length <= 1) {
                                            AttendedAdapter.Data.get(i).ProfileArray = TaskList1.get(i).getResult();
                                            AttendedAdapter.notifyItemChanged(i);
                                        }
                                    }
                                }
                            });
                        } else {

                            DataRegistered.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                            TaskList3.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                            TaskList3.get(TaskList3.size() - 1).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    for (int i = 0; i < TaskList3.size(); i++) {
                                        if (TaskList3.get(i).isSuccessful() && RegisteredAdapter.Data.get(i).ProfileArray.length <= 1) {
                                            RegisteredAdapter.Data.get(i).ProfileArray = TaskList3.get(i).getResult();
                                            RegisteredAdapter.notifyItemChanged(i);
                                        }
                                    }
                                }
                            });
                        }
                    } else if (RegisteredID.contains(Child.getKey())) {
                        if (!Register) {
                            DataNotAttended.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                            TaskList2.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                            TaskList2.get(TaskList2.size() - 1).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    for (int i = 0; i < TaskList2.size(); i++) {
                                        if (TaskList2.get(i).isSuccessful() && NotAttendedAdapter.Data.get(i).ProfileArray.length <= 1) {
                                            NotAttendedAdapter.Data.get(i).ProfileArray = TaskList2.get(i).getResult();
                                            NotAttendedAdapter.notifyItemChanged(i);
                                        }
                                    }
                                }
                            });
                        } else {
                            DataRegistered.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                            TaskList3.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                            TaskList3.get(TaskList3.size() - 1).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    for (int i = 0; i < TaskList3.size(); i++) {
                                        if (TaskList3.get(i).isSuccessful() && RegisteredAdapter.Data.get(i).ProfileArray.length <= 1) {
                                            RegisteredAdapter.Data.get(i).ProfileArray = TaskList3.get(i).getResult();
                                            RegisteredAdapter.notifyItemChanged(i);
                                        }
                                    }
                                }
                            });
                        }

                    } else if (Register) {

                        DataNotRegistered.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                        TaskList4.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                        TaskList4.get(TaskList4.size() - 1).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                for (int i = 0; i < TaskList4.size(); i++) {
                                    if (TaskList4.get(i).isSuccessful() && NotRegisteredAdapter.Data.get(i).ProfileArray.length <= 1) {
                                        NotRegisteredAdapter.Data.get(i).ProfileArray = TaskList4.get(i).getResult();
                                        NotRegisteredAdapter.notifyItemChanged(i);
                                    }
                                }
                            }
                        });

                    }

                    if (!Register) {
                        if (DataAttended.size() >= 1) {
                            AttendedAdapter = new StudentRecyclerAdapter(DataAttended, StudentRecyclerClick);
                        } else {
                            AttendedAdapter = new StudentRecyclerAdapter(DataDefault, null);
                        }
                        if (DataNotAttended.size() >= 1) {
                            NotAttendedAdapter = new StudentRecyclerAdapter(DataNotAttended, StudentRecyclerClick);
                        } else {
                            NotAttendedAdapter = new StudentRecyclerAdapter(DataDefault, null);
                        }

                        Attended.setAdapter(AttendedAdapter);
                        NotAttended.setAdapter(NotAttendedAdapter);

                    } else {
                        if (DataRegistered.size() >= 1) {
                            RegisteredAdapter = new StudentRecyclerAdapter(DataRegistered, StudentRecyclerClick);
                        } else {
                            RegisteredAdapter = new StudentRecyclerAdapter(DataDefault, null);
                        }
                        if (DataNotRegistered.size() >= 1) {
                            NotRegisteredAdapter = new StudentRecyclerAdapter(DataNotRegistered, StudentRecyclerClick);
                        } else {
                            NotRegisteredAdapter = new StudentRecyclerAdapter(DataDefault, null);
                        }

                        Registered.setAdapter(RegisteredAdapter);
                        NotRegistered.setAdapter(NotRegisteredAdapter);

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
