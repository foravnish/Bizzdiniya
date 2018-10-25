package bizzduniya.app.bizzdiniya.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import bizzduniya.app.bizzdiniya.Utils.Util;



public class Login extends AppCompatActivity {

    EditText mobile,password;
    Button btnLogin ;
    TextView btnReg,skipNow,forgt;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);

        btnLogin=findViewById(R.id.btnLogin);
        btnReg=findViewById(R.id.btnReg);
        skipNow=findViewById(R.id.skipNow);
        forgt=findViewById(R.id.forgt);

        dialog=new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        skipNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    Toast.makeText(getApplicationContext(), "Please Connect to the internet...", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(Login.this,HomeAct.class);
                    intent.putExtra("type","0");
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }

            }
        });
//
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){

                    Util.showPgDialog(dialog);
                   // RequestQueue queue = Volley.newRequestQueue(Login.this);
                    StringRequest postRequest = new StringRequest(Request.Method.POST, Api.companyLogin,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);
                                    Util.cancelPgDialog(dialog);
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        if (jsonObject.getString("status").equalsIgnoreCase("success")){

                                            JSONArray jsonArray=jsonObject.getJSONArray("message");
                                            for (int i=0;i<jsonArray.length();i++) {
                                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);


                                       //         Toast.makeText(getApplicationContext(), "Login Successfully...", Toast.LENGTH_SHORT).show();

                                                MyPrefrences.setUserLogin(getApplicationContext(), true);
                                                MyPrefrences.setUserID(getApplicationContext(), jsonObject1.optString("id").toString());
//                                            MyPrefrences.setCatID(getApplicationContext(),jsonObject1.optString("cat_id").toString());
//                                            MyPrefrences.setSCatID(getApplicationContext(),jsonObject1.optString("subcat").toString());
                                                MyPrefrences.setUSENAME(getApplicationContext(), jsonObject1.optString("company_name").toString());
                                            MyPrefrences.setEMAILID(getApplicationContext(),jsonObject1.optString("email").toString());
                                            MyPrefrences.setMobile(getApplicationContext(),jsonObject1.optString("mobile").toString());
                                           // MyPrefrences.setImage(getApplicationContext(),jsonObject1.optString("image").toString());

                                            }
                                            Intent intent=new Intent(Login.this,HomeAct.class);
                                            intent.putExtra("type","0");
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Toast.makeText(Login.this, "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                                    Util.cancelPgDialog(dialog);
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("email", mobile.getText().toString());
                            params.put("password", password.getText().toString());

                            return params;
                        }
                    };

                  //  queue.add(postRequest);

                    postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    postRequest.setShouldCache(false);

                    AppController.getInstance().addToRequestQueue(postRequest);

                }

            }
        });

    }
    private boolean validate(){

        if (TextUtils.isEmpty(mobile.getText().toString()))
        {
            mobile.setError("Oops! Mobile field blank");
            mobile.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(password.getText().toString()))
        {
            password.setError("Oops! Password field blank");
            password.requestFocus();
            return false;
        }

        return true;

    }
}
