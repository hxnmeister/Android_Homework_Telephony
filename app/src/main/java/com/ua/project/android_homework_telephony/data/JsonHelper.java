package com.ua.project.android_homework_telephony.data;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ua.project.android_homework_telephony.models.Contact;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class JsonHelper {
    private final Context context;
    private final Gson gson;

    @Getter
    @Setter
    private String fileName = "contacts.json";

    public JsonHelper(Context context) {
        this.context = context;
        gson = new Gson();
    }

    public void saveContacts(List<Contact> contactList) {
        String json = gson.toJson(contactList);

        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        }
        catch (IOException e) {
            Log.e("TAG", "saveContacts: ", e);
        }
    }

    public List<Contact> loadContacts() {
        try (FileInputStream fis = context.openFileInput(fileName);
             InputStreamReader isr = new InputStreamReader(fis)) {
            Contact[] contacts = gson.fromJson(isr, Contact[].class);

            return contacts == null ? new ArrayList<>() : Arrays.asList(contacts);
        }
        catch (IOException e) {
            Log.e("TAG", "loadContacts: ", e);

            return new ArrayList<>();
        }
    }
}
