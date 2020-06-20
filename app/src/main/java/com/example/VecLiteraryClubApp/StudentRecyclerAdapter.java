package com.example.VecLiteraryClubApp;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.StudentRecyclerTemplate> {

    int count = 0;

    public ArrayList<StudentRecyclerDataStructure> Data;
    HashMap<Integer, StudentRecyclerTemplate> TemplateInstances = new HashMap();
    View.OnClickListener StudentRecyclerClick;

    StudentRecyclerAdapter(ArrayList<StudentRecyclerDataStructure> data, View.OnClickListener click) {

        Data = data;
        count = data.size();
        StudentRecyclerClick = click;
    }

    @NonNull
    @Override
    public StudentRecyclerAdapter.StudentRecyclerTemplate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_recycler, parent, false);
        StudentRecyclerTemplate template = new StudentRecyclerTemplate(view);

        return template;

    }

    public void onBindViewHolder(@NonNull StudentRecyclerTemplate holder, int position) {

        holder.Name.setText(Data.get(position).Name);
        holder.LCA.setText(Data.get(position).LCA);
        holder.YCS.setText(Data.get(position).YCS);

        //   if( Data.get(position).Name.length() > 14 ){
        holder.Name.setSelected(true);
        // }

        if (Data.get(position).ProfileArray.length > 1) {

            holder.Profile.setImageBitmap(BitmapFactory.decodeByteArray(Data.get(position).ProfileArray, 0, Data.get(position).ProfileArray.length));

        } else {
            holder.Profile.setImageResource(R.drawable.student_icon);
        }

        if (Data.get(position).LCA.compareTo(" - ") != 0) {
            holder.clickCard.setOnClickListener(StudentRecyclerClick);
        }

    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class StudentRecyclerTemplate extends RecyclerView.ViewHolder {

        TextView Name, LCA, YCS;
        ImageView Profile;
        CardView clickCard;

        public StudentRecyclerTemplate(View itemView) {
            super(itemView);
            this.Name = itemView.findViewById(R.id.recycler_name);
            this.LCA = itemView.findViewById(R.id.recycler_LCA);
            this.YCS = itemView.findViewById(R.id.recycler_YCS);
            this.Profile = itemView.findViewById(R.id.recycler_profile);
            this.clickCard = itemView.findViewById(R.id.Student_recycler_card);
        }

    }

}
