package com.example.VecLiteraryClubApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class event_overview extends AppCompatActivity {

    View.OnClickListener OnClickEventRecycler;
    ArrayList<Event_Data_Structure> Data_all;
    Event_Data_Adapter Adapter_Overview;
    RecyclerView overview_recycler;
    String Default[] = new String[24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_overview);
        getWindow().setNavigationBarColor(Color.BLACK);

        Default[0] = "None";
        for (int i = 1; i <= 23; i++) {
            Default[i] = "-";
        }


        OnClickEventRecycler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Event_Info_Intent = new Intent(getApplicationContext(), event_information.class);
                Event_Info_Intent.putExtra("Student", getIntent().getBooleanExtra("Student", true));
                int Index = Integer.parseInt(((TextView) (view.findViewById(R.id.event_recycler_index))).getText().toString());
                Event_Info_Intent.putExtra("EventInformation", Data_all.get(Index).Values);
                Event_Info_Intent.putExtra("UserInformation", getIntent().getStringArrayExtra("UserInformation"));
                startActivity(Event_Info_Intent);
            }
        };


        Data_all = new ArrayList<>();

        Data_all.add(new Event_Data_Structure(Default));

        overview_recycler = findViewById(R.id.overview_recycler);

        Adapter_Overview = new Event_Data_Adapter(Data_all, OnClickEventRecycler);

        overview_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, true));

        overview_recycler.setAdapter(Adapter_Overview);


        DatabaseReference Events_reference = FirebaseDatabase.getInstance().getReference("EVENT");

        Events_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Data_all.clear();
                for (int i = 0; i <= (int) (dataSnapshot.getChildrenCount() - 1); i++) {
                    String Vals[] = new String[24];
                    for (int j = 0; j <= 23; j++) {
                        Vals[j] = dataSnapshot.child("" + i).child("" + j).getValue().toString();
                    }

                    Data_all.add(new Event_Data_Structure(Vals));

                }
                if (Data_all.size() == 0) {
                    Data_all.add(new Event_Data_Structure(Default));
                    Adapter_Overview = new Event_Data_Adapter(Data_all, null);
                } else {
                    Adapter_Overview = new Event_Data_Adapter(Data_all, OnClickEventRecycler);

                }


                overview_recycler.setAdapter(Adapter_Overview);
                overview_recycler.scrollToPosition(Adapter_Overview.count - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
