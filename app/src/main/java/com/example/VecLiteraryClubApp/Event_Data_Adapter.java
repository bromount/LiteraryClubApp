package com.example.VecLiteraryClubApp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Event_Data_Adapter extends RecyclerView.Adapter<Event_Data_Adapter.Event_Data_Viewholder> {

    ArrayList<Event_Data_Structure> Data;
    int count;
    View.OnClickListener OnClickList;

    Event_Data_Adapter(ArrayList<Event_Data_Structure> data, View.OnClickListener OnClickEventRecycler) {
        OnClickList = OnClickEventRecycler;
        Data = data;
        count = data.size();
    }

    @NonNull
    @Override
    public Event_Data_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_viewholder, parent, false);
        Event_Data_Viewholder template = new Event_Data_Viewholder(view);

        return template;

    }

    @Override
    public void onBindViewHolder(@NonNull Event_Data_Viewholder holder, int position) {

        holder.name_view.setText(Data.get(position).Values[0]);
        holder.type_view.setText(Data.get(position).Values[21]);
        holder.state_view.setText(Data.get(position).Values[5]);
        holder.name_view.setSelected(true);
        holder.index_view.setText("" + Data.get(position).Values[22]);
        holder.card.setOnClickListener(OnClickList);

    }

    @Override
    public int getItemCount() {
        return count;
    }


    public static class Event_Data_Viewholder extends RecyclerView.ViewHolder {

        TextView name_view, type_view, state_view, index_view;
        CardView card;

        Event_Data_Viewholder(View itemView) {
            super(itemView);
            this.name_view = itemView.findViewById(R.id.event_recycler_name);
            this.type_view = itemView.findViewById(R.id.event_recycler_type);
            this.state_view = itemView.findViewById(R.id.event_recycler_state);
            this.index_view = itemView.findViewById(R.id.event_recycler_index);
            this.card = itemView.findViewById(R.id.event_recycler_card);
        }

    }

}


