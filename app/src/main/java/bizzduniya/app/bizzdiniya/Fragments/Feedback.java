package bizzduniya.app.bizzdiniya.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import bizzduniya.app.bizzdiniya.Activites.HomeAct;
import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import bizzduniya.app.bizzdiniya.Utils.Util;

import static android.text.Html.fromHtml;

/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment {


    public Feedback() {
        // Required empty public constructor
    }

    Spinner spinerCat,state,discountSpinner;
    Dialog dialog;
    List<String> data=new ArrayList<>();
    List<String> stateDataList=new ArrayList<>();
    List<HashMap<String,String>> All;
    LinearLayout linearDiscount,linearPrice;
    TextView dateFrom,dateTill,imageBtn;
    DatePickerDialog datePickerDialog;
    Button bubmit;
    EditText name,email,mobile,address,city,message;
    ImageView image1;
    String filepath1,fileName1;
    int check = 0;
    ProgressDialog progress;
    String offerval,disVal;
    String offerId="";
    String firstData="",stateData="";
    ArrayAdapter  aa;
    List<HashMap<String,String>> DataLoc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feedback2, container, false);

        spinerCat= (Spinner) view.findViewById(R.id.spinerCat);
//        state= (Spinner) view.findViewById(R.id.state);
        name=(EditText)view.findViewById(R.id.name);
        email=(EditText)view.findViewById(R.id.email);
        mobile=(EditText)view.findViewById(R.id.mobile);
        address=(EditText)view.findViewById(R.id.address);
        city=(EditText)view.findViewById(R.id.city);
        message=(EditText)view.findViewById(R.id.message);
        bubmit=(Button) view.findViewById(R.id.bubmit);

        DataLoc=new ArrayList<>();
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        data.clear();
        data.add("Select Topic");
        data.add("Queries/Complaints");
        data.add("Subscription Enquiry");
        data.add("Press Contact");
        data.add("ContactAdvertising/Events");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCat.setAdapter(dataAdapter);

        HomeAct.title.setText("Feedback");



        spinerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                firstData=spinerCat.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    if (!firstData.equals("Select Topic")) {
                        SendDataFeedback();
                    }
                    else{
                        Toast.makeText(getActivity(), "Select Your Topic", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return view;
    }

    private void SendDataFeedback() {

//        Log.d("dfssdfgsdgsdfgsdfg",stateData);
//        Log.d("dfssdfgsdgsdfgsdfg",firstData);
        Util.showPgDialog(dialog);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.feedbackSubmit,
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

                                
                                    //Toast.makeText(getActivity(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                                    errorDialog(jsonObject.getString("message"));

                                

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
                        Log.d("dfsdfsdfsdgs",error.toString());
                        Util.cancelPgDialog(dialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {


                Map<String, String>  params = new HashMap<String, String>();
                params.put("fullName", name.getText().toString());
                params.put("em", email.getText().toString());
                params.put("mob", mobile.getText().toString());
                params.put("address", address.getText().toString());
                params.put("city",city.getText().toString() );
                params.put("messageType", firstData.toString());
                params.put("message", message.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);

    }

    private void errorDialog(String res) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText(fromHtml(res));
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                Fragment fragment = new Home();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).commit();


            }
        });
        dialog.show();

    }

    private boolean validate(){

        if (TextUtils.isEmpty(name.getText().toString()))
        {
            name.setError("Oops! Name blank");
            name.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(email.getText().toString()))
        {
            email.setError("Oops! Email blank");
            email.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(mobile.getText().toString()))
        {
            mobile.setError("Oops! Mobile blank");
            mobile.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(address.getText().toString()))
        {
            address.setError("Oops! Address blank");
            address.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(city.getText().toString()))
        {
            city.setError("Oops! City blank");
            city.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(message.getText().toString()))
        {
            message.setError("Oops! Message blank");
            message.requestFocus();
            return false;
        }

        return true;

    }


}
