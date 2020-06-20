package com.example.VecLiteraryClubApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;


import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GalleryClass extends Fragment {

    boolean Student;

    int width = 0, height = 0;
    FloatingActionButton Add_Gallery_Float_Button;

    RecyclerView feed_recycler;
    Feed_Data_Adapter feed_recycler_adapter;

    HashMap<Integer, Task<byte[]>> ImageTasks;
    HashMap<String, Task<byte[]>> ProfileTasks;

    ArrayList<Feed_Data_Structure> Data;
    DatabaseReference databaseReference;
    ImageView backgroundview;
    Point a;
    int SCREEN_HEIGHT;
    String[] Values;
    ArrayList<Boolean> Likes;
    View.OnClickListener onCLickWebview, onCLickImageView, onClickStar;

    int prev_pos = 0, prev_range = 0;
    float change = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.gallery_fragment, container, false);

        Student = getActivity().getIntent().getBooleanExtra("Student", true);
        Values = getActivity().getIntent().getStringArrayExtra("UserInformation");

        onCLickWebview = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView view1 = (TextView) view;
                Intent Form_Intent = new Intent(getActivity().getBaseContext(), form_answer.class);
                Form_Intent.putExtra("URL", view1.getText().toString());
                startActivity(Form_Intent);
            }
        };

        onCLickImageView = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView Index_View = view.findViewById(R.id.Image_Index_TextView);
                int position = Integer.parseInt(Index_View.getText().toString());
                byte[] Image = feed_recycler_adapter.Data.get(position).Image;
                Bitmap Temp_Image = BitmapFactory.decodeByteArray(Image, 0, Image.length);
                Intent View_Image_Intent = new Intent(getActivity().getApplicationContext(), VIew_Image.class);

                if (Image.length > 350000) {
                    if (Temp_Image.getWidth() > Temp_Image.getHeight()) {
                        Temp_Image = Bitmap.createScaledBitmap(Temp_Image, 720, (int) (((float) Temp_Image.getHeight() * 720) / ((float) Temp_Image.getWidth())), true);
                        width = Temp_Image.getWidth();
                        height = Temp_Image.getHeight();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        int i = 100;
                        while (Image.length > 350000) {
                            Temp_Image.compress(Bitmap.CompressFormat.JPEG, i, stream);
                            Image = stream.toByteArray();
                            i -= 10;
                        }
                    } else {
                        Temp_Image = Bitmap.createScaledBitmap(Temp_Image, (int) (((float) Temp_Image.getWidth() * 720) / ((float) Temp_Image.getHeight())), 720, true);
                        width = Temp_Image.getWidth();
                        height = Temp_Image.getHeight();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        int i = 100;
                        while (Image.length > 350000) {
                            Temp_Image.compress(Bitmap.CompressFormat.JPEG, i, stream);
                            Image = stream.toByteArray();
                            i -= 10;
                        }
                    }
                }

                View_Image_Intent.putExtra("ImageBytes", Image);
                startActivity(View_Image_Intent);
            }
        };

        onClickStar = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton ImgButton = (ImageButton) view;
                ImgButton.setSelected(!ImgButton.isSelected());
                DatabaseReference FeedRef = FirebaseDatabase.getInstance().getReference("FEED").child("" + ImgButton.getTag());
                if (ImgButton.isSelected()) {
                    FeedRef.child("LIKES").child(Values[10]).setValue("1");
                    ImgButton.setImageResource(R.drawable.star_selected);
                } else {
                    FeedRef.child("LIKES").child(Values[10]).setValue(null);
                    ImgButton.setImageResource(R.drawable.star_unselected);
                }
            }
        };


        feed_recycler = root.findViewById(R.id.gallery_recycler);
        LinearLayoutManager LayMan = new LinearLayoutManager(getContext());
        LayMan.setReverseLayout(true);
        LayMan.setStackFromEnd(true);
        feed_recycler.setLayoutManager(LayMan);
        ImageTasks = new HashMap<>();
        ProfileTasks = new HashMap<>();

        prev_range = feed_recycler.computeVerticalScrollRange();

        backgroundview = root.findViewById(R.id.FeedBackgroundImage);

        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_crop_tile);


        a = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(a);
        SCREEN_HEIGHT = a.y;

        backgroundview.getLayoutParams().height = img.getHeight() * 2;
        backgroundview.requestLayout();
        Log.d("Trans", " " + img.getHeight());
        SCREEN_HEIGHT = img.getHeight();


        backgroundview.setTranslationY(-1);


        feed_recycler.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                /*
                if( feed_recycler.computeVerticalScrollRange() == prev_range ){
                    change = feed_recycler.computeVerticalScrollOffset() - prev_pos ;
                    prev_pos = feed_recycler.computeVerticalScrollOffset() ;
                }
                else{
                    if( prev_range != 0 ){
                    change = feed_recycler.computeVerticalScrollOffset() - prev_pos ;
                    }
                    prev_pos = feed_recycler.computeVerticalScrollOffset() ;
                    prev_range = feed_recycler.computeVerticalScrollRange() ;
                    backgroundview.getLayoutParams().height = feed_recycler.computeVerticalScrollRange() * 3 ;
                    backgroundview.requestLayout();
                }
                 */

                if (feed_recycler.computeVerticalScrollRange() == prev_range) {
                    change = feed_recycler.computeVerticalScrollOffset() - prev_pos;
                    prev_pos = feed_recycler.computeVerticalScrollOffset();
                } else {
                    prev_pos = feed_recycler.computeVerticalScrollOffset();
                    prev_range = feed_recycler.computeVerticalScrollRange();
                }
                float trans = (backgroundview.getTranslationY() - change);

                if (trans > 0) {
                    trans = (trans - SCREEN_HEIGHT);
                } else if (trans < -1 * SCREEN_HEIGHT) {
                    trans = (trans + SCREEN_HEIGHT);
                }

                backgroundview.setTranslationY(trans);

                prev_pos = feed_recycler.computeVerticalScrollOffset();

                //Log.d( "Trans" , " " + backgroundview.getTranslationY() + " " + a.y ) ;


            }
        });

        Add_Gallery_Float_Button = root.findViewById(R.id.Add_Gallery_floating_Button);
        if (Student) {
            Add_Gallery_Float_Button.hide();
        }
        Add_Gallery_Float_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Add_Gallery_Intent = new Intent(getActivity().getBaseContext(), add_gallery.class);
                Add_Gallery_Intent.putExtra("UserInformation", getActivity().getIntent().getStringArrayExtra("UserInformation"));
                startActivity(Add_Gallery_Intent);
            }
        });

        Data = new ArrayList<>();
        Data.add(new Feed_Data_Structure(null));
        feed_recycler_adapter = new Feed_Data_Adapter(Data, onCLickWebview, onCLickImageView, onClickStar);
        feed_recycler.setAdapter(feed_recycler_adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("FEED");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean first = true;
                if (Data.size() > 1) {
                    first = false;
                }
                Data.clear();
                Data.add(new Feed_Data_Structure(null));

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ArrayList<String> Vals = new ArrayList<>();
                    for (int i = 0; i <= 3; i++) {
                        Vals.add(child.child("" + i).getValue().toString());
                    }
                    long stars = child.child("LIKES").getChildrenCount();
                    Vals.add("" + stars);
                    if (child.child("LIKES").hasChild(Values[10])) {
                        Vals.add("1");
                    } else {
                        Vals.add("0");
                    }
                    Vals.add(child.child("" + 5).getValue().toString());
                    Data.add(new Feed_Data_Structure(Vals));
                }
                Data.add(new Feed_Data_Structure(null));

                for (int index = 1; index < Data.size() - 1; index++) {
                    if (!ProfileTasks.containsKey(Data.get(index).Values[1])) {
                        Log.d("Trans", "Key -------- > " + Data.get(index).Values[5]);
                        ProfileTasks.put(Data.get(index).Values[1], FirebaseStorage.getInstance().getReference("Profile/" + Data.get(index).Values[6] + "/" + "Profile.jpg").getBytes(1024 * 1024));
                        ProfileTasks.get(Data.get(index).Values[1]).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                for (String Key : ProfileTasks.keySet()) {
                                    if (ProfileTasks.get(Key).isSuccessful()) {
                                        feed_recycler_adapter.AdminProfiles.put(Key, BitmapFactory.decodeByteArray(ProfileTasks.get(Key).getResult(), 0, ProfileTasks.get(Key).getResult().length));
                                    }
                                }
                                feed_recycler_adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

                for (int index = 1; index < Data.size() - 1; index++) {

                    if (Integer.parseInt(Data.get(index).Values[0]) == 1 && Data.get(index).Image.length < 5) {
                        if (!ImageTasks.containsKey(index)) {
                            ImageTasks.put(index, FirebaseStorage.getInstance().getReference("FEED/").child("" + (index - 1)).getBytes(1024 * 1024));
                            ImageTasks.get(index).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {

                                    for (int Key : ImageTasks.keySet()) {
                                        if (ImageTasks.get(Key).isSuccessful()) {
                                            if (ImageTasks.get(Key).getResult().length != Data.get(Key).Image.length) {
                                                Data.get(Key).Image = ImageTasks.get(Key).getResult().clone();
                                            }
                                        }
                                    }
                                    feed_recycler_adapter.count = Data.size();
                                    feed_recycler_adapter.Data = Data;
                                    feed_recycler_adapter.notifyDataSetChanged();
                                }
                            });
                        }

                    }

                }

                //Feed_Data_Adapter FDA = new Feed_Data_Adapter(Data, onCLickWebview, onCLickImageView, onClickStar);
                feed_recycler_adapter.count = Data.size();
                feed_recycler_adapter.Data = Data;
                for (int Key : ImageTasks.keySet()) {
                    if (ImageTasks.get(Key).isSuccessful()) {
                        if (ImageTasks.get(Key).getResult().length != Data.get(Key).Image.length) {
                            Data.get(Key).Image = ImageTasks.get(Key).getResult().clone();
                        }
                    }
                }
                feed_recycler_adapter.notifyDataSetChanged();
                if (first) {
                    feed_recycler.scrollToPosition(Data.size() - 1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
    /*
        Feed_Data_Adapter FDA = new Feed_Data_Adapter(Data, onCLickWebview, onCLickImageView, onClickStar);
        feed_recycler_adapter.count = Data.size();
        feed_recycler_adapter.Data = Data;
        for( int Key : ImageTasks.keySet() ){
            if( ImageTasks.get( Key ).isSuccessful() ) {
                if (ImageTasks.get(Key).getResult().length != Data.get(Key).Image.length) {
                    Data.get(Key).Image = ImageTasks.get(Key).getResult().clone();
                }
            }
        }
        feed_recycler_adapter.notifyDataSetChanged();
    */
    }

}
