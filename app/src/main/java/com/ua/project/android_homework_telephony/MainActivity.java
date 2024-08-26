package com.ua.project.android_homework_telephony;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ua.project.android_homework_telephony.adapters.ContactAdapter;
import com.ua.project.android_homework_telephony.data.DbHelper;
import com.ua.project.android_homework_telephony.databinding.ActivityMainBinding;
import com.ua.project.android_homework_telephony.interfaces.CallHandler;
import com.ua.project.android_homework_telephony.interfaces.SmsHandler;
import com.ua.project.android_homework_telephony.models.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CallHandler, SmsHandler {
    private ActivityMainBinding binding;
    private ContactAdapter contactAdapter;
    private DbHelper dbHelper;

    private final ActivityResultLauncher<String> callPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isPermitted -> {
                if(!isPermitted) {
                    Toast.makeText(this, "Для совершения звонка необходимо разрешение. Пожалуйста, настройте его в настройках.", Toast.LENGTH_LONG).show();
                }
            }
    );

    private final ActivityResultLauncher<String> smsPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isPermitted -> {
                if(!isPermitted) {
                    Toast.makeText(this, "Для отправки СМС необходимо разрешение. Пожалуйста, настройте его в настройках.", Toast.LENGTH_LONG).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<ContactModel> contactsList;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        dbHelper = new DbHelper(MainActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        fillDb(dbHelper);
        contactsList = dbHelper.findAll();
        contactAdapter = new ContactAdapter(MainActivity.this, this, this, contactsList);

        binding.contactsRecyclerView.setAdapter(contactAdapter);
        binding.contactsRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
    }

    private void fillDb(DbHelper db) {
        if(!db.doesTableExist()) {
            final List<ContactModel> CONTACTS_LIST = new ArrayList<>(List.of(
                    ContactModel.builder().id(1).firstName("FirstName1").phoneNumber("0998877666").build(),
                    ContactModel.builder().id(2).phoneNumber("0998827116").build(),
                    ContactModel.builder().id(3).lastName("LastName3").phoneNumber("0668881656").build(),
                    ContactModel.builder().id(4).phoneNumber("0931113322").build(),
                    ContactModel.builder().id(5).firstName("FirstName5").phoneNumber("0667788999").build(),
                    ContactModel.builder().id(6).firstName("FirstName6").lastName("LastName6").phoneNumber("0223344555").build()
            ));

            db.createTable();
            db.insertMany(CONTACTS_LIST);
        }
    }

    @Override
    public void makeCall(String phoneNumber) {
        callPermissionLauncher.launch(Manifest.permission.CALL_PHONE);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(String.format("tel:%s", phoneNumber))));
        }
    }

    @Override
    public void sendSms(String phoneNumber, String text) {
        smsPermissionLauncher.launch(Manifest.permission.SEND_SMS);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(String.format("smsto:%s", phoneNumber)));

            intent.putExtra("sms_body", text);
            startActivity(intent);
        }
    }
}