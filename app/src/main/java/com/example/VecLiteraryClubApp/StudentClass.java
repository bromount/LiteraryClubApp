package com.example.VecLiteraryClubApp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class StudentClass extends Fragment {

    RecyclerView Recycler1, Recycler2, Recycler3, Recycler4;
    StudentRecyclerAdapter Adapter1, Adapter2, Adapter3, Adapter4;
    ArrayList<StudentRecyclerDataStructure> Data1, Data2, Data3, Data4, DataDefault;
    ArrayList<Task<byte[]>> TaskList1, TaskList2, TaskList3, TaskList4;
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView Total, First, Second, Third, Fourth;

    View.OnClickListener StudentRecyclerClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.student_fragment, container, false);

        Total = root.findViewById(R.id.total_student_count);
        First = root.findViewById(R.id.first_student_count);
        Second = root.findViewById(R.id.second_student_count);
        Third = root.findViewById(R.id.third_student_count);
        Fourth = root.findViewById(R.id.fourth_student_count);

        StudentRecyclerClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Values_to_Intent[] = new String[14];
                byte Profile_to_Intent[] = new byte[1];

                String YCS = ((TextView) view.findViewById(R.id.recycler_YCS)).getText().toString();
                String LCA = ((TextView) view.findViewById(R.id.recycler_LCA)).getText().toString();


                switch (YCS.charAt(0)) {
                    case '1':
                        for (int i = 0; i < Data1.size(); i++) {
                            if (Data1.get(i).LCA.compareTo(LCA) == 0) {
                                Values_to_Intent = Data1.get(i).Vals;
                                Profile_to_Intent = Data1.get(i).ProfileArray;
                            }
                        }
                        break;
                    case '2':
                        for (int i = 0; i < Data2.size(); i++) {
                            if (Data2.get(i).LCA.compareTo(LCA) == 0) {
                                Values_to_Intent = Data2.get(i).Vals;
                                Profile_to_Intent = Data2.get(i).ProfileArray;
                            }
                        }
                        break;
                    case '3':
                        for (int i = 0; i < Data3.size(); i++) {
                            if (Data3.get(i).LCA.compareTo(LCA) == 0) {
                                Values_to_Intent = Data3.get(i).Vals;
                                Profile_to_Intent = Data3.get(i).ProfileArray;
                            }
                        }
                        break;
                    case '4':
                        for (int i = 0; i < Data4.size(); i++) {
                            if (Data4.get(i).LCA.compareTo(LCA) == 0) {
                                Values_to_Intent = Data4.get(i).Vals;
                                Profile_to_Intent = Data4.get(i).ProfileArray;
                            }
                        }
                        break;
                }

                Intent StudentInfoIntent = new Intent(getActivity().getBaseContext(), student_information.class);

                StudentInfoIntent.putExtra("ProfileBytes", Profile_to_Intent);
                StudentInfoIntent.putExtra("UserInformation", Values_to_Intent);

                startActivity(StudentInfoIntent);

            }
        };

        Recycler1 = root.findViewById(R.id.firstRecycler);
        Recycler2 = root.findViewById(R.id.secondRecycler);
        Recycler3 = root.findViewById(R.id.thirdRecycler);
        Recycler4 = root.findViewById(R.id.fourthRecycler);

        Recycler1.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
        Recycler2.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
        Recycler3.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
        Recycler4.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));

        Data1 = new ArrayList<>();
        Data2 = new ArrayList<>();
        Data3 = new ArrayList<>();
        Data4 = new ArrayList<>();

        DataDefault = new ArrayList<>();

        TaskList1 = new ArrayList<>();
        TaskList2 = new ArrayList<>();
        TaskList3 = new ArrayList<>();
        TaskList4 = new ArrayList<>();

        String TempValues[] = new String[14];
        for (int i = 0; i < 14; i++) {
            TempValues[i] = "none";
        }

        DataDefault.add(new StudentRecyclerDataStructure("None", " - ", " - ", TempValues));


        Adapter1 = new StudentRecyclerAdapter(DataDefault, StudentRecyclerClick);
        Adapter2 = new StudentRecyclerAdapter(DataDefault, StudentRecyclerClick);
        Adapter3 = new StudentRecyclerAdapter(DataDefault, StudentRecyclerClick);
        Adapter4 = new StudentRecyclerAdapter(DataDefault, StudentRecyclerClick);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("STUDENT");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Data1.clear();
                Data2.clear();
                Data3.clear();
                Data4.clear();
                TaskList1.clear();
                TaskList2.clear();
                TaskList3.clear();
                TaskList4.clear();

                int i1 = 0, i2 = 0, i3 = 0, i4 = 0;
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

                    String year = (Child.child("4").getValue().toString());
                    switch (year) {
                        case "1":
                            Data1.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                            TaskList1.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                            TaskList1.get(i1).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    for (int i = 0; i < TaskList1.size(); i++) {
                                        if (TaskList1.get(i).isSuccessful() && Adapter1.Data.get(i).ProfileArray.length <= 1) {
                                            Adapter1.Data.get(i).ProfileArray = TaskList1.get(i).getResult();
                                            Adapter1.notifyItemChanged(i);
                                        }
                                    }
                                }
                            });
                            i1 += 1;
                            break;
                        case "2":
                            Data2.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                            TaskList2.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                            TaskList2.get(i2).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    for (int i = 0; i < TaskList2.size(); i++) {
                                        if (TaskList2.get(i).isSuccessful() && Adapter2.Data.get(i).ProfileArray.length <= 1) {
                                            Adapter2.Data.get(i).ProfileArray = TaskList2.get(i).getResult();
                                            Adapter2.notifyItemChanged(i);
                                        }
                                    }
                                }
                            });
                            i2 += 1;
                            break;
                        case "3":
                            Data3.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                            TaskList3.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                            TaskList3.get(i3).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    for (int i = 0; i < TaskList3.size(); i++) {
                                        if (TaskList3.get(i).isSuccessful() && Adapter3.Data.get(i).ProfileArray.length <= 1) {
                                            Adapter3.Data.get(i).ProfileArray = TaskList3.get(i).getResult();
                                            Adapter3.notifyItemChanged(i);
                                        }
                                    }
                                }
                            });
                            i3 += 1;
                            break;
                        case "4":
                            Data4.add(new StudentRecyclerDataStructure(Name, LCA, YCS, Values));
                            TaskList4.add(FirebaseStorage.getInstance().getReference(ProfileURL).getBytes(1024 * 1024));
                            TaskList4.get(i4).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    for (int i = 0; i < TaskList4.size(); i++) {
                                        if (TaskList4.get(i).isSuccessful() && Adapter4.Data.get(i).ProfileArray.length <= 1) {
                                            Adapter4.Data.get(i).ProfileArray = TaskList4.get(i).getResult();
                                            Adapter4.notifyItemChanged(i);
                                        }
                                    }
                                }
                            });
                            i4 += 1;
                            break;
                    }


                    int x1 = 0, x2 = 0, x3 = 0, x4 = 0;
                    if (Data1.size() >= 1) {
                        Adapter1 = new StudentRecyclerAdapter(Data1, StudentRecyclerClick);
                        x1 = Data1.size();
                    }
                    if (Data2.size() >= 1) {
                        Adapter2 = new StudentRecyclerAdapter(Data2, StudentRecyclerClick);
                        x2 = Data2.size();
                    }
                    if (Data3.size() >= 1) {
                        Adapter3 = new StudentRecyclerAdapter(Data3, StudentRecyclerClick);
                        x3 = Data3.size();
                    }
                    if (Data4.size() >= 1) {
                        Adapter4 = new StudentRecyclerAdapter(Data4, StudentRecyclerClick);
                        x4 = Data4.size();
                    }

                    Total.setText("" + (x1 + x2 + x3 + x4));
                    First.setText("" + x1);
                    Second.setText("" + x2);
                    Third.setText("" + x3);
                    Fourth.setText("" + x4);

                    Recycler1.setAdapter(Adapter1);
                    Recycler2.setAdapter(Adapter2);
                    Recycler3.setAdapter(Adapter3);
                    Recycler4.setAdapter(Adapter4);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;

    }

}
