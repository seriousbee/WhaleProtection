package com.ulluna.whaleprotection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class ChatActivity extends AppCompatActivity implements DownloadFileCallback {


    public static final String TAG = "VolleyPatterns";

    private RequestQueue mRequestQueue;


    RVadapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    EditText messageBox;
    ArrayList<Message> messages;
    GPSTracker gps;
    private Context context;
    private DownloadFileCallback callback;

    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }


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


        final Handler h = new Handler();
        final int delay = 10000; //milliseconds
        context = this;
        callback=this;
        h.postDelayed(new Runnable(){
            public void run(){
                DownloadUtil util = new DownloadUtil(context, callback);
                util.execute();
                h.postDelayed(this, delay);
            }
        }, delay);

    }

    public void sendMessage(View view) {
        String message = messageBox.getText().toString();
        messageBox.setText("");
        messages.add(new Message(message, Message.USER_ID));
        adapter.updateData(messages);
        getLocation();
        sendMessage(message);
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

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

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


    @Override
    public void confirmFinish(JSONArray array) {
        int length = array.length();
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                messages.add(new Message((JSONArray) array.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.updateData(messages);
    }

    public void sendMessage(String s){
        MyRequest request = new MyRequest(s, String.valueOf(latitude), String.valueOf(longitude));
        try{
            JSONObject object = new JSONObject(request.toString());
            String url = "http://159.203.75.187:5000//postmsg";
            JsonObjectRequest req = new JsonObjectRequest(url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });
            mRequestQueue.add(req);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    class MyRequest{
        String message;
        String username;
        String longitute;
        String latitude;

        public MyRequest(String message, String longitute, String latitude){
            this.message = message;
            this.longitute = longitute;
            this.latitude = latitude;
            this.username = "user";
        }

        public String toString(){
            String s = "{";
            s+="\"messange\":\"" + message + "\",";
            s+="\"location\": [" +longitute + ", " + latitude + "]" ;
            s+="}";
            return s;
        }
    }

}
