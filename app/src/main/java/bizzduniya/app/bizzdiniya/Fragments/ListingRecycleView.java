package bizzduniya.app.bizzdiniya.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bizzduniya.app.bizzdiniya.Activites.HomeAct;
import bizzduniya.app.bizzdiniya.ProductsAdapter;
import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;
import bizzduniya.app.bizzdiniya.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListingRecycleView extends Fragment {


    public ListingRecycleView() {
        // Required empty public constructor
    }

    private RecyclerView products_rclv;
    private LinearLayoutManager linearLayoutManager;


    private ArrayList<HashMap<String, String>> products_arrayList;
    List<HashMap<String,String>> AllProductsLocation ;
    ListView lvExp;
    HashMap<String,String> map;
    Dialog dialog;
    JSONObject jsonObject1;
    FloatingActionButton fabButton;
    String value="";
    List<String> data=new ArrayList<>();
    Button bubmit;
    Dialog dialog1;
    Boolean flag=false;
    ImageView imageNoListing;
    ProductsAdapter productsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_listing_recycle_view, container, false);

        products_rclv = (RecyclerView) view.findViewById(R.id.rclv_products);
        products_arrayList = new ArrayList<>();
        AllProductsLocation = new ArrayList<>();
        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);

        HomeAct.title.setText(getArguments().getString("title").toString());

        productsAdapter = new ProductsAdapter(getActivity(), products_arrayList,products_rclv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        products_rclv.setLayoutManager(mLayoutManager);
        products_rclv.setItemAnimator(new DefaultItemAnimator());

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popLocation();
            }
        });

        listingDataOfCom(getArguments().getString("value"));


        products_rclv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Fragment fragment=new ListingTabDetails();
                        Bundle bundle=new Bundle();
                        bundle.putString("id",products_arrayList.get(position).get("id"));
                        bundle.putString("company_name",products_arrayList.get(position).get("company_name"));

                        FragmentManager manager=getActivity().getSupportFragmentManager();
                        FragmentTransaction ft=manager.beginTransaction();
                        fragment.setArguments(bundle);
                        ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();


                    }
                })
        );


        return view;
    }

    private void popLocation() {


        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // Util.showPgDialog(dialog);

        dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.alertdialogcustom3);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //TextView text = (TextView) dialog.findViewById(R.id.msg_txv);

        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);

        products_arrayList = new ArrayList<>();
        lvExp = (ListView) dialog1.findViewById(R.id.lvExp);
        bubmit = (Button) footerView.findViewById(R.id.bubmit);
        Button bubmit2 = (Button) dialog1.findViewById(R.id.bubmit2);
        TextView text1 = (TextView) dialog1.findViewById(R.id.text1);
        TextView text2 = (TextView) dialog1.findViewById(R.id.text2);
        final LinearLayout linearGPS = (LinearLayout) dialog1.findViewById(R.id.linearGPS);
        final RadioButton nearShow = (RadioButton) dialog1.findViewById(R.id.nearShow);
        final RadioButton areaShow = (RadioButton) dialog1.findViewById(R.id.areaShow);


        lvExp.addFooterView(footerView);


        nearShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp.setVisibility(View.INVISIBLE);
                bubmit.setVisibility(View.INVISIBLE);
                linearGPS.setVisibility(View.VISIBLE);
                nearShow.setChecked(true);
                areaShow.setChecked(false);

            }
        });

        areaShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp.setVisibility(View.VISIBLE);
                bubmit.setVisibility(View.VISIBLE);
                linearGPS.setVisibility(View.GONE);
                nearShow.setChecked(false);
                areaShow.setChecked(true);
            }
        });


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.catIdToLocation+"?cityId="+"1"+"&catId="+getArguments().getString("fragmentKey"), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());
                dialog1.show();
                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        JSONArray jsonArray=response.getJSONArray("message");
                        AllProductsLocation.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap();
                            map.put("id",jsonObject.optString("id"));
//                            map.put("state_id",jsonObject.optString("state_id"));
//                            map.put("city_id",jsonObject.optString("city_id"));
                            map.put("location",jsonObject.optString("location"));


                            AdapterLocation  adapter=new AdapterLocation ();
                            lvExp.setAdapter(adapter);
                            AllProductsLocation.add(map);

                        }

                    }
                    else {
                        Toast.makeText(getActivity(), "No Area Here...", Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
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

    }

    public static Fragment NewInstance(String typeforListing, String title, String searchSt, String  value) {
        Bundle args = new Bundle();
        args.putString("fragmentKey", typeforListing);
        args.putString("title", title);
        args.putString("searc",searchSt);
        args.putString("value",value);

        ListingRecycleView fragment = new ListingRecycleView();
        fragment.setArguments(args);

        return fragment;
    }
    private void listingDataOfCom(String val) {

//        String lat,longi;

//        Log.d("fsdgfsdgfsdgfsdgds",MyPrefrences.getUserID(getActivity()));
//        Log.d("fsdgfsdgfsdgfsdgds123",val);
//        Log.d("dfdsfsdgfsfdgdf",getArguments().getString("value").toString());
//        Log.d("dfsdfsdgfsdgdfgdf", String.valueOf(HomeAct.latitude));
//        Log.d("dfsdfsdgfsdgdfgdf2", String.valueOf(HomeAct.longitude));
//
//        if (String.valueOf(HomeAct.latitude).equals("null")){
//            Log.d("dfgdfgdfgdfghd","sdfdsgd");
//            lat="";
//            longi="";
//        }
//        else{
//            lat=String.valueOf(HomeAct.latitude);
//            longi=String.valueOf(HomeAct.longitude);
//        }





//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                Api.serviceBySubcategory+"?serviceId="+getArguments().getString("fragmentKey"), null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("ResposeListing", response.toString());
//
//                Util.cancelPgDialog(dialog);
//                try {
//
//                    if (response.getString("status").equalsIgnoreCase("success")){
//
//                        expListView.setVisibility(View.VISIBLE);
//                        imageNoListing.setVisibility(View.GONE);
//                        JSONArray jsonArray=response.getJSONArray("message");
//                        for (int i=0;i<jsonArray.length();i++){
//                            JSONObject jsonObject=jsonArray.getJSONObject(i);
//
//
//                            map=new HashMap();
//                            map.put("id",jsonObject.optString("id"));
//                            map.put("company_name",jsonObject.optString("company_name"));
//                            map.put("city",jsonObject.optString("city"));
//                            map.put("country_code",jsonObject.optString("country_code"));
//                            map.put("user_type",jsonObject.optString("user_type"));
//                            map.put("company_type_word",jsonObject.optString("company_type_word"));
//                            map.put("country_flag",jsonObject.optString("country_flag"));
//                            map.put("exporter",jsonObject.optString("exporter"));
//                            map.put("exporterText",jsonObject.optString("exporterText"));
//                            map.put("premium",jsonObject.optString("premium"));
//                            map.put("logo",jsonObject.optString("logo"));
//
//                            Adapter adapter=new Adapter();
//                            expListView.setAdapter(adapter);
//                            AllProducts.add(map);
//                        }
//                    }
//                    else{
//                        expListView.setVisibility(View.GONE);
//                        imageNoListing.setVisibility(View.VISIBLE);
//                      //  Toast.makeText(getActivity(), "No Record Found...", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                    Util.cancelPgDialog(dialog);
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("ResposeError", "Error: " + error.getMessage());
//
//                Toast.makeText(getActivity(),
//                        "Error! Please Connect to the internet...", Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                Util.cancelPgDialog(dialog);
//
//            }
//        });

        // Adding request to request queue
//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq);



        Log.d("fsdgfsdgfsdgfsdgds",getArguments().getString("fragmentKey"));
        products_arrayList.clear();

        StringRequest postRequest = new StringRequest(Request.Method.GET, Api.serviceBySubcategory+"?serviceId="+getArguments().getString("fragmentKey"),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("ResposeListingRec", response);
                        Util.cancelPgDialog(dialog);

                        try {

                            JSONObject response1=new JSONObject(response);
                            if (response1.getString("status").equalsIgnoreCase("success")){

                                products_rclv.setVisibility(View.VISIBLE);
                                imageNoListing.setVisibility(View.GONE);
                                JSONArray jsonArray=response1.getJSONArray("message");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);


                                    map=new HashMap();
                                    map.put("id",jsonObject.optString("id"));
                                    map.put("company_name",jsonObject.optString("company_name"));
                                    map.put("city",jsonObject.optString("city"));
                                    map.put("country_code",jsonObject.optString("country_code"));
                                    map.put("user_type",jsonObject.optString("user_type"));
                                    map.put("company_type_word",jsonObject.optString("company_type_word"));
                                    map.put("country_flag",jsonObject.optString("country_flag"));
                                    map.put("exporter",jsonObject.optString("exporter"));
                                    map.put("exporterText",jsonObject.optString("exporterText"));
                                    map.put("premium",jsonObject.optString("premium"));
                                    map.put("logo",jsonObject.optString("logo"));

                                    products_arrayList.add(map);

                                }


                                products_rclv.setAdapter(productsAdapter);

//                                linearLayoutManager = new LinearLayoutManager(getActivity());
//                                products_rclv.setLayoutManager(linearLayoutManager);
                                productsAdapter.notifyDataSetChanged();

                            }
                            else{
                                products_rclv.setVisibility(View.GONE);
                                imageNoListing.setVisibility(View.VISIBLE);
                                //  Toast.makeText(getActivity(), "No Record Found...", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Util.cancelPgDialog(dialog);
                        }




                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("ResposeError", "Error: " + error.getMessage());
                        Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                        Util.cancelPgDialog(dialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("serviceId", "5041");
                //   params.put("password", password.getText().toString());

                return params;
            }
        };
       // postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);

    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            }
            return false;
        }

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
//            View childView = view.findChildViewUnder(e.getX(), e.getY());
//            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
//                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
//            }
//            return false;
//        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    class AdapterLocation extends BaseAdapter {

        LayoutInflater inflater;
        CheckBox textviwe;

        Boolean flag=false;
        AdapterLocation() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            if (inflater == null) {
//                throw new AssertionError("LayoutInflater not found.");
//            }
        }

        @Override
        public int getCount() {
            return AllProductsLocation.size();
        }

        @Override
        public Object getItem(int position) {
            return AllProductsLocation.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            convertView=inflater.inflate(R.layout.list_location,parent,false);

            textviwe=convertView.findViewById(R.id.textviwe);
            textviwe.setText(AllProductsLocation.get(position).get("location"));

            data.clear();
            textviwe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("sdfsdfsdfgsgs",AllProductsLocation.get(position).get("id"));
                    data.add(AllProductsLocation.get(position).get("id"));
                }
            });

            bubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    value=data.toString().replace("[","").replace("]","").replace(" ","");
                    Log.d("fgdgdfgdfgdfgdfg",value.toString());
                    Log.d("fgdgdfdfdfgdfgdfgdfg",getArguments().getString("fragmentKey"));
                    // listingDataOfCom(getArguments().getString("value"));
                    dialog1.dismiss();

                    Fragment fragment=new ListedPage();
                    FragmentManager manager=getActivity().getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",getArguments().getString("fragmentKey"));
                    bundle.putString("title",getArguments().getString("title"));
                    bundle.putString("search","no");
                    bundle.putString("keyowd","");
                    bundle.putString("value",value.toString());
                    FragmentTransaction ft=manager.beginTransaction();
                    fragment.setArguments(bundle);
                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

                    //  data.clear();


                }
            });

            return convertView;
        }
    }


}
