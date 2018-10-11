package bizzduniya.app.bizzdiniya.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Network;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import bizzduniya.app.bizzdiniya.Activites.HomeAct;
import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.AppController;

/**
 * A simple {@link Fragment} subclass.
 */
public class TradeShowDetail extends Fragment {


    public TradeShowDetail() {
        // Required empty public constructor
    }


    NetworkImageView imgaeView;
    TextView name,date,address,desc,desc2,desc3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_trade_show_detail, container, false);
        HomeAct.title.setText(getArguments().getString("name"));

        name=view.findViewById(R.id.name);
        date=view.findViewById(R.id.date);
        desc=view.findViewById(R.id.desc);
        desc2=view.findViewById(R.id.desc2);
        desc3=view.findViewById(R.id.desc3);
        imgaeView=view.findViewById(R.id.imgaeView);
        address=view.findViewById(R.id.address);

        Log.d("fdsdfsdfsdfsdf",getArguments().getString("data"));

        try {
            JSONObject jsonObject=new JSONObject(getArguments().getString("data"));


            name.setText(jsonObject.getString("orgniser_name"));
            date.setText(jsonObject.getString("start_date")+" - "+jsonObject.getString("end_date"));
            address.setText(jsonObject.getString("venue"));
            desc.setText(Html.fromHtml(jsonObject.getString("trade_description")));
            desc2.setText(Html.fromHtml(jsonObject.getString("product_profile")));
            desc3.setText(Html.fromHtml(jsonObject.getString("visitor_profile")));

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imgaeView.setImageUrl(jsonObject.getString("strip_photo").replace(" ","%20"),imageLoader);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;


    }

}
