package bizzduniya.app.bizzdiniya.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import bizzduniya.app.bizzdiniya.Utils.Util;

public class SplashAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_act);



        Thread background = new Thread() {
            public void run() {
                try {

                    sleep(1*1000);

//                    Intent intent = new Intent(Splash.this, Login.class);
//                    startActivity(intent);
//                    finish();

                    if (MyPrefrences.getUserLogin(SplashAct.this)==true){
                        Intent intent=new Intent(SplashAct.this,HomeAct.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashAct.this, Login.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }



                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }
}
