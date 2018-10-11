package bizzduniya.app.bizzdiniya.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import bizzduniya.app.bizzdiniya.Activites.HomeAct;
import bizzduniya.app.bizzdiniya.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProduct extends Fragment {


    public SearchProduct() {
        // Required empty public constructor
    }


    SearchView simpleSearchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_product, container, false);
        simpleSearchView=(SearchView) view.findViewById(R.id.simpleSearchView);
        int id = simpleSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) simpleSearchView.findViewById(id);
        textView.setTextColor(getResources().getColor(R.color.grayText));
        textView.setHintTextColor(getResources().getColor(R.color.grayText));

        HomeAct.title.setText("Search Product");

        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

//                Fragment fragment=new SearchProductListing();
//                FragmentManager manager=getActivity().getSupportFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//                Bundle bundle=new Bundle();
//                bundle.putString("query",s);
//                fragment.setArguments(bundle);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        return view;
    }
}
