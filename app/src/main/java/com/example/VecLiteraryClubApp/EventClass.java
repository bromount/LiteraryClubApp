package com.example.VecLiteraryClubApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class EventClass extends Fragment {

    FloatingActionButton Add_Event_Float_Button;
    RecyclerView ongoing_recycler, complete_recycler, upcoming_recycler;
    ArrayList<Event_Data_Structure> Data_ongoing, Data_upcoming, Data_complete, Data_all;
    Event_Data_Adapter Adapter_ongoing, Adapter_upcoming, Adapter_complete;
    View.OnClickListener OnClickEventRecycler;
    Button View_Overview_Button;
    boolean Student;

    String Default[] = new String[24];


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.events_fragment, container, false);

        View_Overview_Button = root.findViewById(R.id.event_overview_button);

        View_Overview_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OverView_Intent = new Intent(getContext(), event_overview.class);
                OverView_Intent.putExtra("Student", Student);
                OverView_Intent.putExtra("UserInformation", getActivity().getIntent().getStringArrayExtra("UserInformation"));
                startActivity(OverView_Intent);
            }
        });

        Student = getActivity().getIntent().getBooleanExtra("Student", true);
        Default[0] = "None";
        for (int i = 1; i <= 23; i++) {
            Default[i] = "-";
        }


        OnClickEventRecycler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Event_Info_Intent = new Intent(getActivity().getBaseContext(), event_information.class);
                Event_Info_Intent.putExtra("Student", Student);
                int Index = Integer.parseInt(((TextView) (view.findViewById(R.id.event_recycler_index))).getText().toString());
                Event_Info_Intent.putExtra("EventInformation", Data_all.get(Index).Values);
                Event_Info_Intent.putExtra("UserInformation", getActivity().getIntent().getStringArrayExtra("UserInformation"));
                startActivity(Event_Info_Intent);
            }
        };

        Data_ongoing = new ArrayList<>();
        Data_upcoming = new ArrayList<>();
        Data_complete = new ArrayList<>();
        Data_all = new ArrayList<>();

        Data_ongoing.add(new Event_Data_Structure(Default));
        Data_upcoming.add(new Event_Data_Structure(Default));
        Data_complete.add(new Event_Data_Structure(Default));

        ongoing_recycler = root.findViewById(R.id.ongoing_recycler);
        complete_recycler = root.findViewById(R.id.complete_recycler);
        upcoming_recycler = root.findViewById(R.id.upcoming_recycler);

        Adapter_ongoing = new Event_Data_Adapter(Data_ongoing, OnClickEventRecycler);
        Adapter_upcoming = new Event_Data_Adapter(Data_upcoming, OnClickEventRecycler);
        Adapter_complete = new Event_Data_Adapter(Data_complete, OnClickEventRecycler);
        ongoing_recycler.setAdapter(Adapter_ongoing);
        upcoming_recycler.setAdapter(Adapter_upcoming);
        complete_recycler.setAdapter(Adapter_complete);

        ongoing_recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
        upcoming_recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));
        complete_recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));

        Add_Event_Float_Button = root.findViewById(R.id.Add_Event_floating_Button);
        if (Student) {
            Add_Event_Float_Button.hide();
        }
        Add_Event_Float_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Add_Event_Intent = new Intent(getActivity().getBaseContext(), add_event.class);
                startActivity(Add_Event_Intent);
            }
        });

        DatabaseReference Events_reference = FirebaseDatabase.getInstance().getReference("EVENT");

        Events_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data_upcoming.clear();
                Data_complete.clear();
                Data_ongoing.clear();
                Data_all.clear();
                for (int i = 0; i <= (int) (dataSnapshot.getChildrenCount() - 1); i++) {
                    String Vals[] = new String[24];
                    for (int j = 0; j <= 23; j++) {
                        Vals[j] = dataSnapshot.child("" + i).child("" + j).getValue().toString();
                    }

                    Data_all.add(new Event_Data_Structure(Vals));

                    if (Vals[5].compareTo("Upcoming") == 0) {
                        Data_upcoming.add(new Event_Data_Structure(Vals));
                    }
                    if (Vals[5].compareTo("Ongoing") == 0) {
                        Data_ongoing.add(new Event_Data_Structure(Vals));
                    }
                    if (Vals[5].compareTo("Complete") == 0) {
                        Data_complete.add(new Event_Data_Structure(Vals));
                    }

                }
                if (Data_ongoing.size() == 0) {
                    Data_ongoing.add(new Event_Data_Structure(Default));
                    Adapter_ongoing = new Event_Data_Adapter(Data_ongoing, null);
                } else {
                    Adapter_ongoing = new Event_Data_Adapter(Data_ongoing, OnClickEventRecycler);

                }
                if (Data_upcoming.size() == 0) {
                    Data_upcoming.add(new Event_Data_Structure(Default));
                    Adapter_upcoming = new Event_Data_Adapter(Data_upcoming, null);

                } else {
                    Adapter_upcoming = new Event_Data_Adapter(Data_upcoming, OnClickEventRecycler);

                }
                if (Data_complete.size() == 0) {

                    Data_complete.add(new Event_Data_Structure(Default));
                    Adapter_complete = new Event_Data_Adapter(Data_complete, null);

                } else {
                    Adapter_complete = new Event_Data_Adapter(Data_complete, OnClickEventRecycler);

                }


                ongoing_recycler.setAdapter(Adapter_ongoing);
                upcoming_recycler.setAdapter(Adapter_upcoming);
                complete_recycler.setAdapter(Adapter_complete);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;

    }

}
