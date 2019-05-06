package com.example.ubytovanieappka;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String name, String city, String street, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO HOTEL VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, city);
        statement.bindString(3, street);
        statement.bindBlob(4, image);

        statement.executeInsert();
    }

    public void insertRoomData(String name, String size, String area, String equip, String price, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ROOM VALUES (NULL, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, size);
        statement.bindString(3, area);
        statement.bindString(4, equip);
        statement.bindString(5, price);
        statement.bindBlob(6, image);

        statement.executeInsert();
    }

    public void updateData(String name, String city, String street, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE HOTEL SET name = ?, city = ?, street = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, city);
        statement.bindString(3, street);
        statement.bindBlob(4, image);
        statement.bindDouble(5, (double)id);

        statement.execute();
        database.close();
    }

    public void updateRoomData(String name, String size, String area, String equip, String price, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE ROOM SET name = ?, size = ?, area = ?, equip = ?, price = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, size);
        statement.bindString(3, area);
        statement.bindString(4, equip);
        statement.bindString(5, price);
        statement.bindBlob(6, image);
        statement.bindDouble(7, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM HOTEL WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteRoomData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM ROOM WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
