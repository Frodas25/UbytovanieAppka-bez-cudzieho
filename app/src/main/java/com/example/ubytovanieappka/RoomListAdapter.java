package com.example.ubytovanieappka;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RoomListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Room> roomsList;

    public RoomListAdapter(Context context, int layout, ArrayList<Room> roomsList) {
        this.context = context;
        this.layout = layout;
        this.roomsList = roomsList;
    }

    @Override
    public int getCount() {
        return roomsList.size();
    }

    @Override
    public Object getItem(int position) {
        return roomsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtSize, txtEquip, txtPrice;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        RoomListAdapter.ViewHolder holder = new RoomListAdapter.ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = row.findViewById(R.id.txtName);
            holder.txtSize = row.findViewById(R.id.txtSize);
            holder.txtEquip = row.findViewById(R.id.txtEquip);
            holder.txtPrice = row.findViewById(R.id.txtPrice);
            holder.imageView = row.findViewById(R.id.imgRoom);
            row.setTag(holder);
        }
        else {
            holder = (RoomListAdapter.ViewHolder) row.getTag();
        }

        Room room = roomsList.get(position);

        holder.txtName.setText(room.getName());
        holder.txtSize.setText("Kapacita: " + room.getSize() + "   Veľkosť: " + room.getArea());
        holder.txtEquip.setText(room.getEquip());
        holder.txtPrice.setText(room.getPrice());

        byte[] roomImage = room.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(roomImage, 0, roomImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
