package team.project.tripmanager;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class WebService {
    private String aUrl;
    private JSONObject jsonObject;

    WebService(String aUrl, JSONObject jsonObject){
        this.aUrl = aUrl;
        this.jsonObject = jsonObject;
    }
    public String PostJSon() throws  IOException {
        URL url = new URL(aUrl);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        urlConnection.setDoOutput(true);

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));

        bufferedWriter.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        bufferedWriter.flush();
        bufferedWriter.close();

        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.d(result.toString(),"abc");
        inputStream.close();
        reader.close();
        urlConnection.connect();

        return result.toString();
    }
}
