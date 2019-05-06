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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class HotelList extends AppCompatActivity {

    GridView gridView;
    ArrayList<Hotel> list;
    HotelListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        gridView = findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new HotelListAdapter(this, R.layout.hotel_items, list);
        gridView.setAdapter(adapter);

        // získa všetky dáta z SQLLite
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM HOTEL");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String city = cursor.getString(2);
            String street = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new Hotel(id, name, city, street,image));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Upraviť", "Zmazať"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(HotelList.this);

                dialog.setTitle("Vyber si čo urobiť");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM HOTEL");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate(HotelList.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM HOTEL");
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

    ImageView imageView;

    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_hotel_activity);
        dialog.setTitle("Upraviť");

        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM HOTEL WHERE Id = " + position);

        imageView = dialog.findViewById(R.id.imageView);
        final EditText edtName =  dialog.findViewById(R.id.edtName);
        final EditText edtCity =  dialog.findViewById(R.id.edtCity);
        final EditText edtStreet =  dialog.findViewById(R.id.edtStreet);
        Button btnUpdate =  dialog.findViewById(R.id.btnUpdate);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String city = cursor.getString(2);
            String street = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            edtName.setText(name);
            edtCity.setText(city);
            edtStreet.setText(street);
        }

        // šírka pre dialóg
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // výška pre dialóg
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        HotelList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.sqLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            edtCity.getText().toString().trim(),
                            edtStreet.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageView),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Úspešne upravené!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Chyba pri upravovaní", error.getMessage());
                }
                updateHotelList();
            }
        });
    }

    private void showDialogDelete(final int idHotel){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(HotelList.this);

        dialogDelete.setTitle("Upozornenie!!");
        dialogDelete.setMessage("Si si istý, že to chceš odstrániť?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    MainActivity.sqLiteHelper.deleteData(idHotel);
                    Toast.makeText(getApplicationContext(), "Zmazanie úspešné!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("Chyba", e.getMessage());
                }
                updateHotelList();
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

    private void updateHotelList(){
        // // získa všetky dáta z db
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM HOTEL");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String city = cursor.getString(2);
            String street = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new Hotel(id, name, city, street ,image));
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
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
