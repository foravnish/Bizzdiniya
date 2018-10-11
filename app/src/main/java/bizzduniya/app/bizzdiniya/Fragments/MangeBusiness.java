package bizzduniya.app.bizzdiniya.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import bizzduniya.app.bizzdiniya.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MangeBusiness extends Fragment {


    public MangeBusiness() {
        // Required empty public constructor
    }


    Spinner yearSpin,daySpin,contry,stateSpin,citySpin;
    ArrayList<String> years = new ArrayList<String>();
    ArrayList<String> country = new ArrayList<String>();
    ArrayList<String> state = new ArrayList<String>();
    ArrayList<String> city = new ArrayList<String>();
    String[] days = {"Select Working Days","1 Day","2 Day","3 Day","4 Day","5 Day","6 Day","7 Day"};


    List<HashMap<String,String>> AllProducts;
    List<HashMap<String,String>> StateData;
    List<HashMap<String,String>> CityData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mange_business, container, false);

        yearSpin=view.findViewById(R.id.yearSpin);
        daySpin=view.findViewById(R.id.daySpin);
        contry=view.findViewById(R.id.contry);
        stateSpin=view.findViewById(R.id.stateSpin);
        citySpin=view.findViewById(R.id.citySpin);

        AllProducts=new ArrayList<>();
        StateData=new ArrayList<>();
        CityData=new ArrayList<>();

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1950; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(adapter);


        ArrayAdapter<String> daySpinAdp = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, days);
        daySpinAdp.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        daySpin.setAdapter(daySpinAdp);

        getCountryList();

        contry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id=AllProducts.get(i).get("id");
                stateByCountryId_get(id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        stateSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id=StateData.get(i).get("id");
                cityBySteteId_get(id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    private void getCountryList() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.countryList, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeContry", response.toString());
                //Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        country.clear();
                        AllProducts.clear();

                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("country", jsonObject.optString("country"));

                            country.add(jsonObject.optString("country"));
                            AllProducts.add(map);

                            ArrayAdapter<String> contryAdap= new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, country);
                            contryAdap.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            contry.setAdapter(contryAdap);


                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                   // Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
             //   Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void stateByCountryId_get(String id) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.stateByCountryId+"?countryId="+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeContry", response.toString());
                //Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        state.clear();
                        StateData.clear();
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("name", jsonObject.optString("name"));

                            state.add(jsonObject.optString("name"));
                            StateData.add(map);

                            ArrayAdapter<String> stateAdap= new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, state);
                            stateAdap.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            stateSpin.setAdapter(stateAdap);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    // Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                //   Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void cityBySteteId_get(String id) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.cityBySteteId+"?stateId="+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeContry", response.toString());
                //Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        city.clear();
                        CityData.clear();
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("name", jsonObject.optString("name"));

                            city.add(jsonObject.optString("name"));
                            CityData.add(map);

                            ArrayAdapter<String> stateAdap= new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, city);
                            stateAdap.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            citySpin.setAdapter(stateAdap);


                        }
                        //  AllEvents.add(hashMap);
                    }

//
//
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    // Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                //   Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


}
