package com.project.androidlab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.project.androidlab.db.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    CheckBox showContactsSelectionByVariantCheckBox;
    ListView contactsListView;

    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        showContactsSelectionByVariantCheckBox = findViewById(R.id.show_contacts_selection_by_variant_check_box);
        contactsListView = findViewById(R.id.contacts_list_view);

        dbHelper = new DbHelper(this);

        showContactsSelectionByVariantCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getContactsSelectionByVariantCondition();
                } else {
                    getAllContacts();
                }
            }
        });

        getAllContacts();
    }

    private void getAllContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Contact> contacts = dbHelper.getAllContacts();
                runOnUiThread(() -> {
                    setListViewAdapter(contacts);
                });
            }
        }).start();
    }

    private void getContactsSelectionByVariantCondition() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Contact> contacts = dbHelper.getContactsSelectionByVariantCondition();
                runOnUiThread(() -> {
                    setListViewAdapter(contacts);
                });
            }
        }).start();
    }

    private void setListViewAdapter(List<Contact> contacts) {
        String contactNameKey = "contactNameKey";
        String contactNumberKey = "contactNumberKey";

        ArrayList<HashMap<String, String>> contactsInfo = new ArrayList<>();
        for (Contact contact : contacts) {
            contactsInfo.add(new HashMap<String, String>() {{
                put(contactNameKey, contact.getFirstName() + " " + contact.getLastName());
                put(contactNumberKey, contact.getPhoneNumber());
            }});
        }

        SimpleAdapter contactsAdapter =
                new SimpleAdapter(this, contactsInfo,
                        android.R.layout.simple_list_item_2,
                        new String[]{contactNameKey, contactNumberKey},
                        new int[]{android.R.id.text1, android.R.id.text2});
        contactsListView.setAdapter(contactsAdapter);
    }

    public void goBack(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}