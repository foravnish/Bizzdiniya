package bizzduniya.app.bizzdiniya.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class LatestNewsDetail extends Fragment {


    public LatestNewsDetail() {
        // Required empty public constructor
    }

    TextView name,offersText,tag,desc,comName,catName;
    NetworkImageView imgaeView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_latest_news_detail, container, false);

        HomeAct.title.setText(getArguments().getString("name"));


        name=view.findViewById(R.id.name);
        imgaeView=view.findViewById(R.id.imgaeView);
        offersText=view.findViewById(R.id.offersText);
        tag=view.findViewById(R.id.tag);
        desc=view.findViewById(R.id.desc);

        try {
            JSONObject jsonObject=new JSONObject(getArguments().getString("data"));

            name.setText(jsonObject.getString("topic"));
            offersText.setText(jsonObject.getString("posted_date"));
            tag.setText(jsonObject.getString("cat_name"));
            desc.setText(Html.fromHtml(jsonObject.getString("description")));

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imgaeView.setImageUrl(jsonObject.getString("photo").replace(" ","%20"),imageLoader);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Typeface tvFont = Typeface.createFromAsset(getActivity().getAssets(), "muli_bold.ttf");
        name.setTypeface(tvFont);
        tag.setTypeface(tvFont);
        offersText.setTypeface(tvFont);

        return  view;
    }

}
