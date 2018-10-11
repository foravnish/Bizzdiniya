package bizzduniya.app.bizzdiniya.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import bizzduniya.app.bizzdiniya.Activites.HomeAct;
import bizzduniya.app.bizzdiniya.Activites.Login;
import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import bizzduniya.app.bizzdiniya.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeEnquiry extends Fragment {


    public ComposeEnquiry() {
        // Required empty public constructor
    }

    TextView enqText;
    Button sendEnquiry;
    EditText editText,email,mobile;
    Dialog dialog;
    LinearLayout linerVis;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_compose_enquiry, container, false);
        Log.d("dfsdfsdfsdfgs",getArguments().getString("id"));
        Log.d("dfsdfsdfsdfgdfds",getArguments().getString("comName"));

        HomeAct.title.setText(getArguments().getString("comName"));
        enqText=view.findViewById(R.id.enqText);
        sendEnquiry=view.findViewById(R.id.sendEnquiry);
        editText=view.findViewById(R.id.editText);
        linerVis=view.findViewById(R.id.linerVis);
        email=view.findViewById(R.id.email);
        mobile=view.findViewById(R.id.mobile);
        enqText.setText("Send Your Inquiry Directly To "+getArguments().getString("comName"));

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        if (MyPrefrences.getUserLogin(getActivity())==true){
            linerVis.setVisibility(View.GONE);
        }
        else{
            linerVis.setVisibility(View.VISIBLE);
        }

        sendEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){

                    if (MyPrefrences.getUserLogin(getActivity())==true){
                        SendEquiry("login");
                    }
                    else{
                        SendEquiry("not_login");
                    }


                }

            }
        });

        return view;

    }

    private void SendEquiry(final String isLogin) {

        Util.showPgDialog(dialog);
        // RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.submitEnquery,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("ResponseEnquiry", response);
                        Util.cancelPgDialog(dialog);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")){

                                Toast.makeText(getActivity(), "Enquiry Sent Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                        Util.cancelPgDialog(dialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                if (isLogin.equals("login")){
                    params.put("email", MyPrefrences.getEMAILID(getActivity()));
                    params.put("company_name", getArguments().getString("comName"));
                    params.put("company_id", MyPrefrences.getUserID(getActivity()));
                    params.put("fname", MyPrefrences.getUSENAME(getActivity()));
                    params.put("mobile", MyPrefrences.getMobile(getActivity()));
                    params.put("city", "");
                    params.put("state", "");
                    params.put("description", editText.getText().toString());
                    params.put("country_id", "");
                    params.put("frq", "");
                    params.put("sourceuri", "");
                    params.put("referal_url", "");
                    params.put("multiple_quote", "");
                }
                else if (isLogin.equals("not_login")){
                    params.put("email", email.getText().toString());
                    params.put("company_name", getArguments().getString("comName"));
                    params.put("company_id", "");
                    params.put("fname", "");
                    params.put("mobile", mobile.getText().toString());
                    params.put("city", "");
                    params.put("state", "");
                    params.put("description", editText.getText().toString());
                    params.put("country_id", "");
                    params.put("frq", "");
                    params.put("sourceuri", "");
                    params.put("referal_url", "");
                    params.put("multiple_quote", "");
                }


                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);

    }

    private boolean validate(){

        if (TextUtils.isEmpty(editText.getText().toString()))
        {
            editText.setError("Oops! field blank");
            editText.requestFocus();
            return false;
        }
        return true;
    }


}
