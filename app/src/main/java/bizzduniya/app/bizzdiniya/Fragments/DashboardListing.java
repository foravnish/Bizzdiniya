package bizzduniya.app.bizzdiniya.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bizzduniya.app.bizzdiniya.Activites.HomeAct;
import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import bizzduniya.app.bizzdiniya.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardListing extends Fragment {


    public DashboardListing() {
        // Required empty public constructor
    }
    Dialog dialog;
    List<HashMap<String,String>> AllProducts ;
    GridView expListView;
    JSONObject jsonObject1;
    ImageView imageNoListing;
    JSONArray jsonArray;

    LinearLayout approvedLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dashboard_listing, container, false);

        expListView = (GridView) view.findViewById(R.id.lvExp);
        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
        approvedLayout = (LinearLayout) view.findViewById(R.id.approvedLayout);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);
        AllProducts=new ArrayList<>();

        HomeAct.title.setText("Dashboard Listing");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.myPostDashboard+"?companyId="+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());
                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        expListView.setVisibility(View.VISIBLE);
                        imageNoListing.setVisibility(View.GONE);

                        jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("image", jsonObject.optString("image"));
                            map.put("title", jsonObject.optString("title"));
                            map.put("created_date", jsonObject.optString("created_date"));
                            map.put("description", jsonObject.optString("description"));
                            map.put("country_flag", jsonObject.optString("country_flag"));
                            map.put("followers", jsonObject.optString("followers"));
                            map.put("following", jsonObject.optString("following"));
                            map.put("companyName", jsonObject.optString("companyName"));
                            map.put("city", jsonObject.optString("city"));
                            map.put("country_code", jsonObject.optString("country_code"));
                            map.put("logo", jsonObject.optString("logo"));


                            Adapter adapter = new Adapter();
                            expListView.setAdapter(adapter);
                            AllProducts.add(map);

                        }
                        //  AllEvents.add(hashMap);
                    }

                    else if (response.getString("status").equalsIgnoreCase("failure")){
                        expListView.setVisibility(View.GONE);
                        imageNoListing.setVisibility(View.VISIBLE);

                        //Toast.makeText(getActivity(), "Offer list not available", Toast.LENGTH_SHORT).show();
                        // errorDialog("Offer list not available");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        return view;
    }


    class Adapter extends BaseAdapter {

        LayoutInflater inflater;
        TextView name,offersText,tag,desc,address,follow,follwing;

        NetworkImageView imgaeView,flag,comLogo;
        LinearLayout linerLayout;

        Adapter() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return AllProducts.size();
        }

        @Override
        public Object getItem(int position) {
            return AllProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView=inflater.inflate(R.layout.list_my_dash_listing,parent,false);

            name=convertView.findViewById(R.id.name);
            imgaeView=convertView.findViewById(R.id.imgaeView);
            address=convertView.findViewById(R.id.address);
            desc=convertView.findViewById(R.id.desc);
            flag=convertView.findViewById(R.id.flag);
            follow=convertView.findViewById(R.id.follow);
            follwing=convertView.findViewById(R.id.follwing);
            comLogo=convertView.findViewById(R.id.comLogo);

            name.setText(Html.fromHtml(AllProducts.get(position).get("companyName")));
            address.setText(AllProducts.get(position).get("city")+" | "+AllProducts.get(position).get("country_code"));
            desc.setText(AllProducts.get(position).get("description"));
            follow.setText(AllProducts.get(position).get("followers"));
            follwing.setText(AllProducts.get(position).get("following"));


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imgaeView.setImageUrl(AllProducts.get(position).get("image").replace(" ","%20"),imageLoader);
            flag.setImageUrl(AllProducts.get(position).get("country_flag").replace(" ","%20"),imageLoader);
            comLogo.setImageUrl(AllProducts.get(position).get("logo").replace(" ","%20"),imageLoader);

            final Typeface tvFont = Typeface.createFromAsset(getActivity().getAssets(), "muli_bold.ttf");
            name.setTypeface(tvFont);
            desc.setTypeface(tvFont);


            return convertView;
        }
    }



}
