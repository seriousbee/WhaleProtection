package com.ulluna.whaleprotection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    RVadapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    EditText messageBox;
    ArrayList<Message> messages;
    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewChat);
        messageBox = (EditText) findViewById(R.id.editTextMessage);
        messages = new ArrayList<>();
        messages.add(new Message("Hi, how can I help you?", Message.CONSULTANT_ID));

        adapter = new RVadapter(messages);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarChat);
        toolbar.setTitle("Alex");
        toolbar.setSubtitle("She'll help you in hard times");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.subtitleColor));

        setSupportActionBar(toolbar);

    }

    public void sendMessage(View view) {
        String message = messageBox.getText().toString();
        messageBox.setText("");
        messages.add(new Message(message, Message.USER_ID));
        adapter.updateData(messages);
        getLocation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        messageBox.clearFocus();
    }

    public void getLocation(){
        gps = new GPSTracker(ChatActivity.this);

        // Check if GPS enabled
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_chat:
                Toast.makeText(this, "Calling for help...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
