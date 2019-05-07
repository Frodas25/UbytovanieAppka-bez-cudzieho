package com.example.ubytovanieappka;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class RoomList extends AppCompatActivity {

    GridView gridView;
    ArrayList<Room> list;
    RoomListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        gridView = findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new RoomListAdapter(this, R.layout.room_items, list);
        gridView.setAdapter(adapter);

        // získa všetky dáta z SQLLite
        Cursor cursor = RoomActivity.sqLiteHelper.getData("SELECT * FROM ROOM");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String size = cursor.getString(2);
            String area = cursor.getString(3);
            String equip = cursor.getString(4);
            String price = cursor.getString(5);
            byte[] image = cursor.getBlob(6);

            list.add(new Room(id, name, size, area, equip, price, image));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Upraviť", "Zmazať"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(RoomList.this);

                dialog.setTitle("Vyber si čo urobiť");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = RoomActivity.sqLiteHelper.getData("SELECT id FROM ROOM");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate(RoomList.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = RoomActivity.sqLiteHelper.getData("SELECT id FROM ROOM");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    ImageView imageRoomView;

    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_room_activity);
        dialog.setTitle("Upraviť");

        imageRoomView = dialog.findViewById(R.id.imageRoomView);
        final EditText edtRoomName =  dialog.findViewById(R.id.edtRoomName);
        final EditText edtSize =  dialog.findViewById(R.id.edtSize);
        final EditText edtArea =  dialog.findViewById(R.id.edtArea);
        final EditText edtEquip =  dialog.findViewById(R.id.edtEquip);
        final EditText edtPrice =  dialog.findViewById(R.id.edtPrice);
        Button btnUpdateRoom =  dialog.findViewById(R.id.btnUpdateRoom);

        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM ROOM WHERE Id = " + position);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String size = cursor.getString(2);
            String area = cursor.getString(3);
            String equip = cursor.getString(4);
            String price = cursor.getString(5);
            byte[] image = cursor.getBlob(6);

            edtRoomName.setText(name);
            edtSize.setText(size);
            edtArea.setText(area);
            edtEquip.setText(equip);
            edtPrice.setText(price);
        }

        // šírka pre dialóg
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // výška pre dialóg
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageRoomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        RoomList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RoomActivity.sqLiteHelper.updateRoomData(
                            edtRoomName.getText().toString().trim(),
                            edtSize.getText().toString().trim(),
                            edtArea.getText().toString().trim(),
                            edtEquip.getText().toString().trim(),
                            edtPrice.getText().toString().trim(),
                            RoomActivity.imageViewToByte(imageRoomView),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Úspešne upravené!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Chyba pri upravovaní", error.getMessage());
                }
                updateRoomList();
            }
        });
    }

    private void showDialogDelete(final int idRoom){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(RoomList.this);

        dialogDelete.setTitle("Upozornenie!!");
        dialogDelete.setMessage("Si si istý, že to chceš odstrániť?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    RoomActivity.sqLiteHelper.deleteRoomData(idRoom);
                    Toast.makeText(getApplicationContext(), "Zmazanie úspešné!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("Chyba", e.getMessage());
                }
                updateRoomList();
            }
        });

        dialogDelete.setNegativeButton("Zrušiť", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateRoomList(){
        // získa všetky dáta z db
        Cursor cursor = RoomActivity.sqLiteHelper.getData("SELECT * FROM ROOM");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String size = cursor.getString(2);
            String area = cursor.getString(3);
            String equip = cursor.getString(4);
            String price = cursor.getString(5);
            byte[] image = cursor.getBlob(6);

            list.add(new Room(id, name, size, area , equip, price, image));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "Nemáš povolenie na prístup k vnútornej pamäti!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageRoomView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
