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
import android.renderscript.Sampler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class add_gallery extends AppCompatActivity {

    int PICK_FILE_REQUEST = 39;

    Spinner Post_Types;
    ArrayAdapter Post_Types_Adapter;

    Button add_image_button, add_webpage_button, add_text_button, load_webpage;
    EditText text_text, image_description, webpage_url, webpage_description;

    ImageView image_image;

    WebView add_feed_webview;
    CardView add_feed_webview_container, add_webview, add_text, add_image;
    TextView add_feed_webview_touchfield;

    String textdescription, webview_url = "https://docs.google.com/forms/d/e/1FAIpQLSeLQR8H89nmZF7k3a1XqE5m1Rgkn5I_X-haJLcSH-Yr6xbTIQ/viewform";
    int feed_index;
    ArrayList<String> Values;
    byte[] add_image_image;
    String[] User_Info;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    boolean loaded_index = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gallery);

        getWindow().setNavigationBarColor(Color.BLACK);

        User_Info = getIntent().getStringArrayExtra("UserInformation");

        add_image_button = findViewById(R.id.add_feed_image_button);
        add_text_button = findViewById(R.id.add_feed_text_button);
        add_webpage_button = findViewById(R.id.add_feed_webpage_button);
        add_text = findViewById(R.id.add_feed_text);
        add_webview = findViewById(R.id.add_feed_webpage);
        add_image = findViewById(R.id.add_feed_image);
        text_text = findViewById(R.id.add_feed_text_text);
        image_description = findViewById(R.id.add_feed_image_description);
        image_image = findViewById(R.id.add_feed_image_image);
        webpage_url = findViewById(R.id.add_feed_webpage_url);
        webpage_description = findViewById(R.id.add_feed_webpage_description);
        load_webpage = findViewById(R.id.add_feed_loadwebpage);

        add_image_image = new byte[1];

        add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textdescription = image_description.getText().toString();

                if (add_image_image.length == 1 || textdescription.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Required Information", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!loaded_index) {
                    Toast.makeText(getApplicationContext(), "Database Index Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                storageReference = FirebaseStorage.getInstance().getReference("FEED/" + feed_index);
                storageReference.putBytes(add_image_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Values = new ArrayList<>();
                        Values.add("1");
                        Values.add(User_Info[0] + " " + User_Info[1]);
                        Values.add(textdescription);
                        Values.add(" ");
                        Values.add("0");
                        Values.add(FirebaseAuth.getInstance().getUid());
                        databaseReference.child("" + feed_index).setValue(Values).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Unable To Post", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Unable To Post", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });

        add_webpage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                webview_url = webpage_url.getText().toString();
                textdescription = webpage_description.getText().toString();

                if (webview_url.length() == 0 || textdescription.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Required Information", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!loaded_index) {
                    Toast.makeText(getApplicationContext(), "Database Index Error", Toast.LENGTH_SHORT).show();
                    return;
                }


                Values = new ArrayList<>();
                Values.add("2");
                Values.add(User_Info[0] + " " + User_Info[1]);
                Values.add(textdescription);
                Values.add(webview_url);
                Values.add("0");
                Values.add(FirebaseAuth.getInstance().getUid());
                databaseReference.child("" + feed_index).setValue(Values).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Unable To Post", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


            }
        });

        add_text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textdescription = text_text.getText().toString();

                if (textdescription.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Required Information", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!loaded_index) {
                    Toast.makeText(getApplicationContext(), "Database Index Error", Toast.LENGTH_SHORT).show();
                    return;
                }


                Values = new ArrayList<>();
                Values.add("0");
                Values.add(User_Info[0] + " " + User_Info[1]);
                Values.add(textdescription);
                Values.add(" ");
                Values.add("0");
                Values.add(FirebaseAuth.getInstance().getUid());
                databaseReference.child("" + feed_index).setValue(Values).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Unable To Post", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


            }

        });


        databaseReference = FirebaseDatabase.getInstance().getReference("FEED");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                loaded_index = true;
                feed_index = (int) dataSnapshot.getChildrenCount();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Post_Types = findViewById(R.id.feed_types);
        Post_Types_Adapter = ArrayAdapter.createFromResource(this, R.array.post_types, R.layout.custom_spinner_gallery);
        Post_Types_Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_gallery);
        Post_Types.setAdapter(Post_Types_Adapter);

        Post_Types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                add_text.setVisibility(View.INVISIBLE);
                add_image.setVisibility(View.INVISIBLE);
                add_webview.setVisibility(View.INVISIBLE);
                switch (i) {
                    case 0:
                        add_text.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        add_image.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        add_webview.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add_feed_webview = findViewById(R.id.add_feed_webview);
        add_feed_webview.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSeLQR8H89nmZF7k3a1XqE5m1Rgkn5I_X-haJLcSH-Yr6xbTIQ/viewform");

        add_feed_webview_container = findViewById(R.id.add_feed_webview_container);
        add_feed_webview_touchfield = findViewById(R.id.add_feed_webview_touchfield);

        add_feed_webview_touchfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Form_Intent = new Intent(getApplicationContext(), form_answer.class);
                Form_Intent.putExtra("URL", webview_url);
                startActivity(Form_Intent);
            }
        });

        load_webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview_url = webpage_url.getText().toString();
                add_feed_webview.loadUrl(webview_url);
            }
        });

        image_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);

            }
        });

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

                    image_image.setImageBitmap(Poster_Bitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Poster_Bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    add_image_image = stream.toByteArray();

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Failed to get file", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

}
