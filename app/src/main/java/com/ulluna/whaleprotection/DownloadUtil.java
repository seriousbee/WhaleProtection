package com.ulluna.whaleprotection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class DownloadUtil extends AsyncTask<String, String, Void> {

    private final String filesource = "http://159.203.75.187:5000/getmsgs";
    Context context;
    DownloadFileCallback callback;
    JSONArray array;

    public DownloadUtil(Context context, DownloadFileCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... strings) {
        downloadFile(filesource);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callback.confirmFinish(array);
    }

    private void downloadFile(String urlString) {
        URL url;
        URLConnection connection;
        BufferedReader bufferedReader = null;
        StringBuilder content = new StringBuilder();
        try {
            url = new URL(urlString);
            connection = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                content.append(line).append("\n");
            bufferedReader.close();

            array = new JSONArray(content.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("com.ulluna.demergency", "Problem with download");
            Toast.makeText(context, "Download failed!", Toast.LENGTH_LONG).show();

        }
    }
}

