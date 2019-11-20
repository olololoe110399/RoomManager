package com.example.qunlphngtr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import com.example.qunlphngtr.Activities.RoomActivity;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;

import java.util.List;


public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.ViewHolder> {
    List<Room> roomList;
    Context context;
    public AdapterRoom(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    public AdapterRoom() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_room, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(roomList.get(position).getRoomName());
        holder.acreage.setText(roomList.get(position).getAcreage()+" m\u00B2");
        holder.price.setText(roomList.get(position).getRoomPrice()/1000000+" tr");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, RoomActivity.class);
                i.putExtra("RoomName",roomList.get(position).getRoomName());
                RoomActivity.room=roomList.get(position);
                context.startActivity(i);
                Animatoo.animateSlideLeft(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,acreage,price,tvpeople,tvvehical;
        private CardView cardView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView=itemView.findViewById(R.id.cardView);
        name=itemView.findViewById(R.id.tvroom);
        acreage=itemView.findViewById(R.id.tvacreage);
        price=itemView.findViewById(R.id.tvpriceroom);
        tvpeople=itemView.findViewById(R.id.tvpeolpe);
        tvvehical=itemView.findViewById(R.id.tvvehical);
    }
}
}
