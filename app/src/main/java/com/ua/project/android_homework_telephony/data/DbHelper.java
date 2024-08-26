package com.ua.project.android_homework_telephony.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ua.project.android_homework_telephony.models.ContactModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    @Getter
    @Setter
    private static String dbName = "contacts.db";
    @Getter
    @Setter
    private static String tableName = "contacts";

    public DbHelper(@Nullable Context context) {
        super(context, dbName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable() {
        String query = """
            CREATE TABLE IF NOT EXISTS %s(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                last_name TEXT,
                first_name TEXT,
                phone_number TEXT NOT NULL
            )
        """;

        getWritableDatabase().execSQL(String.format(query, tableName));
    }

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS " + tableName;

        getWritableDatabase().execSQL(query);
    }

    public void insert(@NonNull ContactModel contact) {
        ContentValues values = new ContentValues();

        values.put("last_name", contact.getLastName());
        values.put("first_name", contact.getFirstName());
        values.put("phone_number", contact.getPhoneNumber());

        getWritableDatabase().insert(tableName, null, values);
    }

    public void insertMany(@NonNull List<ContactModel> contactsList) {
        for (ContactModel contact : contactsList) {
            insert(contact);
        }
    }

    public void deleteById(int id) {
        String whereClause = "id = ?";

        getWritableDatabase().delete(tableName, whereClause, new String[]{ String.valueOf(id) });
    }

    @SuppressLint("Range")
    public List<ContactModel> findAll() {
        String query = "SELECT * FROM " + tableName;
        List<ContactModel> contactsList = new ArrayList<>();

        try (Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{})) {
            while (cursor.moveToNext()) {
                contactsList.add(ContactModel.builder()
                                .id(cursor.getInt(cursor.getColumnIndex("id")))
                                .lastName(cursor.getString(cursor.getColumnIndex("last_name")))
                                .firstName(cursor.getString(cursor.getColumnIndex("first_name")))
                                .phoneNumber(cursor.getString(cursor.getColumnIndex("phone_number")))
                                .build());
            }
        }
        catch (Exception e) {
            Log.e("TAG", "db.findAll: ", e);
        }

        return contactsList;
    }

    public boolean doesTableExist() {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";

        try(Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{tableName})) {
            return cursor.getCount() > 0;
        }
        catch (Exception e) {
            Log.e("TAG", "doesTableExist: ", e);

            return false;
        }
    }
}
