package com.example.kontakty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
    }

    private void getPermission(){
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, code);

        }else{
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == code){
            // czy dostałem zgodę?
            boolean granted = false;
            for(int i=0; i<permissions.length; i++){
                if(permissions[i].equals(Manifest.permission.READ_CONTACTS)){
                    granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    break;
                }
            }
            if(granted){
                readContacts();
            }else{
                Toast.makeText(getBaseContext(), "Brak uprawnień", Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void readContacts(){
        Uri uriContacts = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor;
        String[] fields = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        String sort = ContactsContract.Contacts.DISPLAY_NAME + " desc";

        CursorLoader cursorLoader = new CursorLoader(this, uriContacts, fields, null, null, sort);
        cursor = cursorLoader.loadInBackground();

        ArrayList<String> contacts = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                contacts.add(String.format("%s. %s", id, name));
            }while (cursor.moveToNext());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}