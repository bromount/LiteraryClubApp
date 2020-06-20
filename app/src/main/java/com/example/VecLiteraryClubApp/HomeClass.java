package com.example.VecLiteraryClubApp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeClass extends Fragment {

    TextView Fname, Lname, LCAID, Registrations, Submissions, Home_UserName, Home_TextView;
    TextView UpComing_Name, Ongoing_Name, UpComing_Desc, Ongoing_Desc, UpComing_Title, Ongoing_Title;
    DatabaseReference TextReference, EventReference;
    String Values[];
    View.OnClickListener OnClickUpcoming, OnClickOngoing;
    boolean Student;
    String[] OngoingInformation, UpcomingInformation;
    boolean loadedOngoing = false, loadedUpcoming = false;
    CardView Upcoming_Card, Ongoing_Card;
    Button Upcoming_Button, Ongoing_Button;
    StorageReference Home_text_profile_storage;
    ImageView Home_Text_Profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_fragment, container, false);

        Fname = root.findViewById(R.id.student_information_Fname);
        Lname = root.findViewById(R.id.student_information_Lname);
        LCAID = root.findViewById(R.id.student_information_LCAID);
        Registrations = root.findViewById(R.id.student_information_Registrations);
        Submissions = root.findViewById(R.id.student_information_Submissions);

        Upcoming_Card = root.findViewById(R.id.Home_Upcoming_Event_Card);
        Ongoing_Card = root.findViewById(R.id.Home_Ongoing_Event_Card);

        UpComing_Title = Upcoming_Card.findViewById(R.id.Home_Event_Title);
        UpComing_Name = Upcoming_Card.findViewById(R.id.Home_Event_Name);
        UpComing_Desc = Upcoming_Card.findViewById(R.id.Home_Event_Desc);
        Upcoming_Button = Upcoming_Card.findViewById(R.id.Home_Event_Button);

        Ongoing_Title = Ongoing_Card.findViewById(R.id.Home_Event_Title);
        Ongoing_Name = Ongoing_Card.findViewById(R.id.Home_Event_Name);
        Ongoing_Desc = Ongoing_Card.findViewById(R.id.Home_Event_Desc);
        Ongoing_Button = Ongoing_Card.findViewById(R.id.Home_Event_Button);

        Home_Text_Profile = root.findViewById(R.id.home_profile);

        Ongoing_Title.setText("ONGOING EVENT");
        UpComing_Title.setText("UPCOMING EVENT");

        Home_UserName = root.findViewById(R.id.home_username);
        Home_TextView = root.findViewById(R.id.home_textview);

        Values = getActivity().getIntent().getStringArrayExtra("UserInformation");
        Registrations.setText(Values[11]);
        Submissions.setText(Values[12]);


        Fname.setText(Values[0]);
        Lname.setText(Values[1]);
        LCAID.setText(Values[9].substring(0, 3) + "-" + Values[9].substring(3, 6));


        TextReference = FirebaseDatabase.getInstance().getReference("FEED");
        TextReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = (int) dataSnapshot.getChildrenCount() - 1; i >= 0; i--) {
                    if (Integer.parseInt(dataSnapshot.child("" + i).child("" + 0).getValue().toString()) == 0) {
                        Home_UserName.setText(dataSnapshot.child("" + i).child("" + 1).getValue().toString());

                        Home_text_profile_storage = FirebaseStorage.getInstance().getReference("Profile/" + dataSnapshot.child("" + i).child("" + 5).getValue().toString() + "/Profile.jpg");
                        Home_text_profile_storage.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Home_Text_Profile.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            }
                        });


                        Home_TextView.setText(dataSnapshot.child("" + i).child("" + 2).getValue().toString());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Student = getActivity().getIntent().getBooleanExtra("Student", true);
        OnClickOngoing = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Event_Info_Intent = new Intent(getActivity().getBaseContext(), event_information.class);
                Event_Info_Intent.putExtra("Student", Student);
                Event_Info_Intent.putExtra("EventInformation", OngoingInformation);
                Event_Info_Intent.putExtra("UserInformation", getActivity().getIntent().getStringArrayExtra("UserInformation"));
                startActivity(Event_Info_Intent);
            }
        };

        OnClickUpcoming = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Event_Info_Intent = new Intent(getActivity().getBaseContext(), event_information.class);
                Event_Info_Intent.putExtra("Student", Student);
                Event_Info_Intent.putExtra("EventInformation", UpcomingInformation);
                Event_Info_Intent.putExtra("UserInformation", getActivity().getIntent().getStringArrayExtra("UserInformation"));
                startActivity(Event_Info_Intent);
            }
        };

        EventReference = FirebaseDatabase.getInstance().getReference("EVENT");
        EventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadedUpcoming = false;
                loadedOngoing = false;

                for (int index = (int) dataSnapshot.getChildrenCount(); index >= 0; index--) {
                    if (dataSnapshot.hasChild("" + index)) {
                        DataSnapshot child = dataSnapshot.child("" + index);
                        if (child.child("" + 5).getValue().toString().compareTo("Upcoming") == 0 && !loadedUpcoming) {
                            loadedUpcoming = true;
                            UpcomingInformation = new String[24];
                            for (int j = 0; j <= 23; j++) {
                                UpcomingInformation[j] = child.child("" + j).getValue().toString();
                            }
                            UpComing_Name.setText(UpcomingInformation[0]);
                            UpComing_Name.setSelected(true);
                            UpComing_Desc.setText(UpcomingInformation[2]);
                            Upcoming_Button.setOnClickListener(OnClickUpcoming);
                        } else if (child.child("" + 5).getValue().toString().compareTo("Ongoing") == 0 && !loadedOngoing) {
                            loadedOngoing = true;
                            OngoingInformation = new String[24];
                            for (int j = 0; j <= 23; j++) {
                                OngoingInformation[j] = child.child("" + j).getValue().toString();
                            }
                            Ongoing_Name.setText(OngoingInformation[0]);
                            Ongoing_Name.setSelected(true);
                            Ongoing_Desc.setText(OngoingInformation[2]);
                            Ongoing_Button.setOnClickListener(OnClickOngoing);
                        }
                    }
                    if (loadedOngoing && loadedUpcoming) {
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;

    }

    @Override
    public void onStart() {

        Values = getActivity().getIntent().getStringArrayExtra("UserInformation");
        Registrations.setText(Values[11]);
        Submissions.setText(Values[12]);
        super.onStart();

    }
}
