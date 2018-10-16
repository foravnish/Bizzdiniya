package bizzduniya.app.bizzdiniya;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.HashMap;

import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> implements AdapterView.OnClickListener
{
    private ArrayList<HashMap<String,String>> products_arrayList;
    private LayoutInflater layoutInflater;
    Context context;
    RecyclerView recyclerView;

    public ProductsAdapter(Context context, ArrayList<HashMap<String,String>> products_arrayList,RecyclerView recyclerView)
    {
        /*
         * RecyclerViewAdapter Constructor to Initialize Data which we get from RecyclerViewFragment
         **/

        layoutInflater = LayoutInflater.from(context);
        this.products_arrayList = products_arrayList;
        this.context=context;
        this.recyclerView=recyclerView;
        recyclerView.setOnClickListener(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        /*
         * LayoutInflater is used to Inflate the view
         * from fragment_listview_adapter
         * for showing data in RecyclerView
         **/

        View view = layoutInflater.inflate(R.layout.list_lsiting, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.MyViewHolder holder, int position)
    {
        /*
         * onBindViewHolder is used to Set all the respective data
         * to Textview or Imagview form worldpopulation_pojoArrayList
         * ArrayList Object.
         **/

//            if (!TextUtils.isEmpty(products_arrayList.get(position).getTitle()))
//            {
//                String cropName = products_arrayList.get(position).getTitle().substring(0,1).toUpperCase() +  products_arrayList.get(position).getTitle().substring(1);
//
//                holder.textview_product.setText(cropName);
//            }

//        holder.name.setText(products_arrayList.get(position).get("product_name"));
//        holder.price.setText("₹ "+products_arrayList.get(position).get("mrp_price"));
//        holder.newprice.setText("₹ "+products_arrayList.get(position).get("sell_price"));
//        holder.off.setText(products_arrayList.get(position).get("discount"));
//        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//        holder.imageView.setImageUrl(products_arrayList.get(position).get("photo"),imageLoader);
//        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        Log.d("fgdgdfgdfgdfg", String.valueOf(products_arrayList.size()));


        holder.name.setText(products_arrayList.get(position).get("company_name"));
        holder.address.setText(products_arrayList.get(position).get("city")+ " | "+products_arrayList.get(position).get("country_code"));
        //viewholder.totlareview.setText(AllProducts.get(position).get("totlauser")+" Rating");
        holder.area.setText(products_arrayList.get(position).get("exporter"));
        holder.subcatListing.setText(products_arrayList.get(position).get("exporterText"));

        Log.d("dfsvgdvgdgdfgdf",products_arrayList.get(position).get("logo"));


        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

//        holder.imgaeView.setImageUrl(products_arrayList.get(position).get("logo"),imageLoader);
//        holder.flagIcon.setImageUrl(products_arrayList.get(position).get("country_flag"),imageLoader);

        if ((products_arrayList.get(position).get("logo").equalsIgnoreCase(Api.IMAGE_URL))){
            holder.imgaeView.setBackgroundResource(R.drawable.no_listing);
        }
        else{
            holder.imgaeView.setImageUrl(products_arrayList.get(position).get("logo"), imageLoader);
        }
        holder.flagIcon.setImageUrl(products_arrayList.get(position).get("country_flag"), imageLoader);


    }

    @Override
    public int getItemCount()
    {
        /*
         * getItemCount is used to get the size of respective worldpopulation_pojoArrayList ArrayList
         **/

        return products_arrayList.size();
    }

    @Override
    public void onClick(View view) {
        Log.d("fsdfsdfsdgfsd", "dfgdfg");
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgFav,stars;
        TextView address,name,totlareview,area,subcatListing,distance;
        ImageView callNow1;
        LinearLayout liner,linerLayoutOffer;
        NetworkImageView imgaeView,flagIcon;
        CardView cardView;
        ShimmerTextView offersText;
        Shimmer shimmer;
        ImageView img1,img2,img3,img4,img5;
        LinearLayout footer_layout;

        /**
         * MyViewHolder is used to Initializing the view.
         **/

        MyViewHolder(View itemView)
        {
            super(itemView);


            name=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address);
            imgFav=itemView.findViewById(R.id.imgFav);
            stars=itemView.findViewById(R.id.stars);
            liner=itemView.findViewById(R.id.liner);
            totlareview=itemView.findViewById(R.id.totlareview);
            // viewholder.callNow1=convertView.findViewById(R.id.callNow1);
//            viewholder.rating=convertView.findViewById(R.id.rating);
            area=itemView.findViewById(R.id.area);
            distance=itemView.findViewById(R.id.distance);
            imgaeView=itemView.findViewById(R.id.imgaeView);
            flagIcon=itemView.findViewById(R.id.flagIcon);
            linerLayoutOffer=itemView.findViewById(R.id.linerLayoutOffer);
            cardView=itemView.findViewById(R.id.cardView);
            offersText=itemView.findViewById(R.id.offersText);
            subcatListing=itemView.findViewById(R.id.subcatListing);

            itemView.setTag(itemView);


//            recyclerView.setOnTouchListener((View.OnTouchListener) this);

        }

//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            Log.d("fsdfsdfsdgfsd", "dfgdfg");
//
//            return false;
//        }

//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            Log.d("fsdfsdfsdgfsd", "dfgdfg");
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {

//        }

//        @Override
//        public void onClick(View view) {
//            Log.d("fsdfsdfsdgfsd", "dfgdfg");
//        }

//        @Override
//        public void onClick(View view) {
//           // int position = recyclerView.getChildAdapterPosition(view);
//          //  int position = recyclerView.getChildLayoutPosition(view);
//
//            Log.d("fsdfsdfsdgfsd", String.valueOf("vbgvg"));
//        }


//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            Log.d("fsdfsdfsdgfsd", String.valueOf("vbgvg"));
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//
//        }

//        @Override
//        public void onItemClick(View view, int i) {
//            Log.d("fsdfsdfsdgfsd", String.valueOf("vbgvg"));
//        }
    }
}

