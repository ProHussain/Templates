package com.hashmac.templates;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {
    private static final String iiiLabVideoDownloadURL = "http://service.iiilab.com/video/download";
    private static String client = "0c6675f705c7fefg";
    private static String clientSecretKey = "c0e5a53128ef5157725378b859d65712";
    String link = "https://www.youtube.com/watch?v=UBd5GCZOOps&t=5258s";

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataVolley();
             //   postDataRetro();
            }
        });
    }



    private void postDataVolley() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://service.iiilab.com/video/download", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);
                    Log.e("Respo",response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Long timestamp = System.currentTimeMillis();
                String sign = MD5(link + timestamp + clientSecretKey);
                Map<String, String> params = new HashMap<String, String>();
                params.put("link", link);
                params.put("timestamp", String.valueOf(timestamp));
                params.put("sign", sign);
                params.put("client", client);
                return params;
            }
        };
        queue.add(request);
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch(UnsupportedEncodingException ex){
        }
        return null;
    }


}