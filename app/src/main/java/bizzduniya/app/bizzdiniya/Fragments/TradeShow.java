package bizzduniya.app.bizzdiniya.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
import bizzduniya.app.bizzdiniya.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class TradeShow extends Fragment {


    public TradeShow() {
        // Required empty public constructor
    }

    Dialog dialog;
    List<HashMap<String,String>> AllProducts ;
    GridView expListView;
    JSONObject jsonObject1;
    ImageView imageNoListing;
    JSONArray jsonArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_trade_show, container, false);


        AllProducts = new ArrayList<>();
        expListView = (GridView) view.findViewById(R.id.lvExp);
        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        HomeAct.title.setText("Trade Show");

//        AdView adView = (AdView)view. findViewById(R.id.search_ad_view);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.tradeShow, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposetradeShow", response.toString());
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
                            map.put("trade_name", jsonObject.optString("trade_name"));
                            map.put("start_date", jsonObject.optString("start_date"));
                            map.put("end_date", jsonObject.optString("end_date"));
                            map.put("venue", jsonObject.optString("venue"));
                            map.put("trade_description", jsonObject.optString("trade_description"));
                            map.put("strip_photo", jsonObject.optString("strip_photo"));
//                            map.put("discount", jsonObject.optString("discount"));
//                            map.put("actual_price", jsonObject.optString("actual_price"));
//                            map.put("offer_price", jsonObject.optString("offer_price"));
//                            map.put("coupon_code", jsonObject.optString("coupon_code"));
//                            map.put("offer_from", jsonObject.optString("offer_from"));
//                            map.put("offer_to", jsonObject.optString("offer_to"));
//                            map.put("image", jsonObject.optString("image"));
//                            map.put("cat_name", jsonObject.optString("cat_name"));
//                            map.put("posted_date", jsonObject.optString("posted_date"));
//                            map.put("company_name", jsonObject2.optString("company_name"));
//                            map.put("address", jsonObject2.optString("address"));
//                            map.put("c1_mobile1", jsonObject2.optString("c1_mobile1"));
//                            map.put("new_keywords", jsonObject2.optString("new_keywords"));
//                            map.put("c1_fname", jsonObject2.optString("c1_fname")+" "+jsonObject2.optString("c1_mname")+" "+jsonObject2.optString("c1_lname"));


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
                        "Error! Please Connect to the internet.", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);


        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = new TradeShowDetail();
                Bundle bundle=new Bundle();
                try {
                    bundle.putString("data", String.valueOf(jsonArray.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putString("name", AllProducts.get(i).get("trade_name"));
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

            }
        });


        return view;
    }


    class Adapter extends BaseAdapter {

        LayoutInflater inflater;
        TextView name,date,address,desc,catName;

        NetworkImageView imgaeView;
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


            convertView=inflater.inflate(R.layout.list_trade_show,parent,false);

            name=convertView.findViewById(R.id.name);
            imgaeView=convertView.findViewById(R.id.imgaeView);
            date=convertView.findViewById(R.id.date);
            address=convertView.findViewById(R.id.address);
            desc=convertView.findViewById(R.id.desc);

            name.setText(Html.fromHtml(AllProducts.get(position).get("trade_name")));
            date.setText(AllProducts.get(position).get("start_date")+" - "+AllProducts.get(position).get("end_date"));
            address.setText(AllProducts.get(position).get("venue"));
            desc.setText(AllProducts.get(position).get("trade_description"));

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imgaeView.setImageUrl(AllProducts.get(position).get("strip_photo").replace(" ","%20"),imageLoader);

            final Typeface tvFont = Typeface.createFromAsset(getActivity().getAssets(), "muli_bold.ttf");
            name.setTypeface(tvFont);
            address.setTypeface(tvFont);
            desc.setTypeface(tvFont);
            date.setTypeface(tvFont);

            return convertView;
        }
    }



}
