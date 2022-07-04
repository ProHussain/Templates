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
import java.util.HashMap;
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
             //   postDataVolley();
                postDataRetro();
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

    private void postDataRetro() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" https://service.iiilab.com/video/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Long timestamp = System.currentTimeMillis();
        String sign = MD5(link + timestamp + clientSecretKey);
        // passing data from our text fields to our modal class.
        DataModel modal = new DataModel(link,String.valueOf(timestamp),sign,client);

        // calling a method to create a post and passing our modal class.
        Call<ReturnModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<ReturnModel>() {
            @Override
            public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                // this method is called when we get response from our api.
                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                ReturnModel responseFromAPI = response.body();
                Log.e("Res",responseFromAPI.data[0].getCover());

            }

            @Override
            public void onFailure(Call<ReturnModel> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.e("Errpr",t.getMessage());
            }
        });
    }

    public interface RetrofitAPI {

        @POST("download")
        Call<ReturnModel> createPost(@Body DataModel dataModal);
    }

    public class DataModel {
        String link;
        String timestamp;
        String sign;
        String client;

        public DataModel(String link, String timestamp, String sign, String client) {
            this.link = link;
            this.timestamp = timestamp;
            this.sign = sign;
            this.client = client;
        }
    }

    public class ReturnModel {
        String retCode;
        String retDesc;
        Data[] data;
        boolean succ;

        public ReturnModel(String retCode, String retDesc, Data[] data, boolean succ) {
            this.retCode = retCode;
            this.retDesc = retDesc;
            this.data = data;
            this.succ = succ;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetDesc() {
            return retDesc;
        }

        public void setRetDesc(String retDesc) {
            this.retDesc = retDesc;
        }

        public Data[] getData() {
            return data;
        }

        public void setData(Data[] data) {
            this.data = data;
        }

        public boolean isSucc() {
            return succ;
        }

        public void setSucc(boolean succ) {
            this.succ = succ;
        }
    }

    public class Data {
        String cover;
        String text;
        String video;

        public Data(String cover, String text, String video) {
            this.cover = cover;
            this.text = text;
            this.video = video;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }

}