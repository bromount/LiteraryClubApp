package com.example.VecLiteraryClubApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Feed_Data_Adapter extends RecyclerView.Adapter<Feed_Data_Adapter.Feed_Data_Template> {

    HashMap<String, Bitmap> AdminProfiles;
    ArrayList<Feed_Data_Structure> Data;
    int count;
    View.OnClickListener onClickWebview, onClickImageview, onClickStar;
    Feed_Data_Adapter ThisAdapter;

    Feed_Data_Adapter(ArrayList<Feed_Data_Structure> DataIn, View.OnClickListener WebViewClick, View.OnClickListener ImageViewClick, View.OnClickListener StarClick) {
        ThisAdapter = this;
        Data = DataIn;
        count = DataIn.size();
        onClickWebview = WebViewClick;
        onClickImageview = ImageViewClick;
        onClickStar = StarClick;
        AdminProfiles = new HashMap<>();
    }

    @NonNull
    @Override
    public Feed_Data_Template onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_text_layout, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_image_layout, parent, false);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_webview_layout, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_header_gallery, parent, false);
                break;
        }
        Feed_Data_Template template = new Feed_Data_Template(view, viewType);
        return template;
    }

    @Override
    public void onBindViewHolder(@NonNull Feed_Data_Template holder, int position) {


        if (position == count - 1) {
            holder.itemView.setVisibility(View.VISIBLE);
            Log.d("Trans", "" + holder.itemView.getHeight() + "  Bound   ");
            return;
        } else if (position == 0) {
            holder.itemView.setVisibility(View.INVISIBLE);
            Log.d("Trans", "" + holder.itemView.getHeight() + "  Bound   ");
            return;
        }

        String[] Values = Data.get(position).Values;

        if (AdminProfiles.containsKey(Data.get(position).Values[1])) {
            holder.Profile.setImageBitmap(AdminProfiles.get(Data.get(position).Values[1]));
        }

        holder.starcount.setText(Values[4]);
        holder.StarButton.setTag(position - 1);
        holder.StarButton.setOnClickListener(onClickStar);

        if (Values[5].compareTo("1") == 0) {
            holder.StarButton.setSelected(true);
            holder.StarButton.setImageResource(R.drawable.star_selected);
        } else {
            holder.StarButton.setSelected(false);
            holder.StarButton.setImageResource(R.drawable.star_unselected);
        }

        switch (Integer.parseInt(Values[0])) {
            case 0:
                holder.textdescription.setText(Values[2]);
                holder.UID.setText(Values[1]);
                break;
            case 1:
                holder.textdescription.setText(Values[2]);
                holder.UID.setText(Values[1]);
                holder.Image_Index_TextView.setText("" + position);


                if (Data.get(position).Image.length < 5) {
                    holder.Image.setImageResource(R.drawable.re_res_alpha_shadow_mini);
                } else {
                    //byte[] image = Data.get(position).Image ;
                    holder.Image_View_Card.setOnClickListener(onClickImageview);
                    Bitmap image_bitmap; //= BitmapFactory.decodeByteArray( image , 0 , image.length ) ;
                    byte[] byte_array = Data.get(position).Image;
                    //byte[] byte_array = Data.get(position).Image ;
                    image_bitmap = BitmapFactory.decodeByteArray(byte_array, 0, byte_array.length);
                    if (image_bitmap.getHeight() > 540) {
                        Bitmap image_bitmap_scaled = Bitmap.createScaledBitmap(image_bitmap, (540 * image_bitmap.getWidth()) / image_bitmap.getHeight(), 540, true);
                        holder.Image.setImageBitmap(image_bitmap_scaled);
                    } else {
                        holder.Image.setImageBitmap(image_bitmap);
                    }
                }
                break;
            case 2:
                holder.textdescription.setText(Values[2]);
                holder.UID.setText(Values[1]);
                holder.Webpage.setWebViewClient(new WebViewClientCustom());
                holder.Webpage.loadUrl(Values[3]);
                holder.click_this.setText(Values[3]);
                holder.click_this.setOnClickListener(onClickWebview);
                break;
        }
        Log.d("Trans", "" + holder.itemView.getHeight() + "  Bound   " + "   -  " + holder.textdescription.getText().toString());

    }

    @Override
    public int getItemCount() {
        return count;
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0 || position == count - 1) {
            return -1;
        }
        return Integer.parseInt(Data.get(position).Values[0]);

    }

    public static class Feed_Data_Template extends RecyclerView.ViewHolder {

        TextView textdescription, UID, starcount, click_this, Image_Index_TextView;
        ImageView Image, Profile;
        WebView Webpage;
        CardView Image_View_Card;
        ImageButton StarButton;

        Feed_Data_Template(View itemView, int viewType) {
            super(itemView);
            Profile = itemView.findViewById(R.id.feed_profile);
            switch (viewType) {
                case 0:
                    textdescription = itemView.findViewById(R.id.feed_textview);
                    UID = itemView.findViewById(R.id.feed_username);
                    starcount = itemView.findViewById(R.id.feed_star_count);
                    StarButton = itemView.findViewById(R.id.feed_star);
                    break;
                case 1:
                    textdescription = itemView.findViewById(R.id.feed_textview);
                    UID = itemView.findViewById(R.id.feed_username);
                    starcount = itemView.findViewById(R.id.feed_star_count);
                    Image = itemView.findViewById(R.id.feed_image);
                    Image_View_Card = itemView.findViewById(R.id.ImageView_Card);
                    Image_Index_TextView = itemView.findViewById(R.id.Image_Index_TextView);
                    StarButton = itemView.findViewById(R.id.feed_star);
                    break;
                case 2:
                    textdescription = itemView.findViewById(R.id.feed_textview);
                    UID = itemView.findViewById(R.id.feed_username);
                    starcount = itemView.findViewById(R.id.feed_star_count);
                    Webpage = itemView.findViewById(R.id.feed_webview);
                    click_this = itemView.findViewById(R.id.feed_click_this);
                    StarButton = itemView.findViewById(R.id.feed_star);
                    break;
            }


        }

    }


}
