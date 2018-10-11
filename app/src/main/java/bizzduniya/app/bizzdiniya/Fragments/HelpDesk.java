package bizzduniya.app.bizzdiniya.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.JSONParser;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import bizzduniya.app.bizzdiniya.Utils.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpDesk extends Fragment {


    public HelpDesk() {
        // Required empty public constructor
    }
    Dialog  dialog;
    Spinner spiner;
    String[] list = { "Select","Complaint", "Praise", "Question","Suggestion"};
    EditText messageT;
    Button submit,raiseTicket;
    String sub;
    GridView gridview;
    Adapter adapter;
    List<HashMap<String,String>> DataList;
    JSONArray jsonArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_help_desk2, container, false);
        getActivity().setTitle("Help Desk");

        raiseTicket=(Button) view.findViewById(R.id.raiseTicket);
        gridview=(GridView) view.findViewById(R.id.gridview);
        DataList =new ArrayList<>();
        new TicketListApi(getActivity()).execute();

        raiseTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertdialogcustm_ticket);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView text = (TextView) dialog.findViewById(R.id.msg_txv);


                spiner=(Spinner)dialog.findViewById(R.id.spiner);
                messageT=(EditText)dialog.findViewById(R.id.message);
                submit=(Button) dialog.findViewById(R.id.submit);

                ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,list);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spiner.setAdapter(aa);


                spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        sub=spiner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chaeckValidation()){
//                    Toast.makeText(getActivity(), ""+sub, Toast.LENGTH_SHORT).show();
                            new HelpDeskApi(getActivity(),sub.toString(),messageT.getText().toString(),dialog).execute();

                            //  Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                dialog.show();

            }
        });

        return view;
    }

    private boolean chaeckValidation() {

        if (TextUtils.isEmpty(messageT.getText().toString())){
            messageT.setError("oops! Message is blank");
            messageT.requestFocus();
            return false;
        }
        if (spiner.getSelectedItem().equals("Select")){
            Toast.makeText(getActivity(), "Please select type.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private class HelpDeskApi extends AsyncTask<String, Void, String> {
        Context context;
        String subject,message;
        Dialog dialog1;
        public HelpDeskApi(Context context, String subject, String message,Dialog dialog1) {
            this.context = context;
            this.subject=subject;
            this.message=message;
            this.dialog1=dialog1;
            dialog =new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Util.showPgDialog(dialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();

            map.put("function","insertHelpDesk");
            map.put("subject", subject.toString());
            map.put("message", message.toString());
            map.put("memberId", MyPrefrences.getUserID(getActivity()));

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.Login,"GET",map);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", ": " + s);
            Util.cancelPgDialog(dialog);
            try {
                final JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                        Util.errorDialog(getActivity(),"Submit successfully...\nTicket No is "+jsonObject.optString("message"));
                        messageT.setText("");
                        dialog1.dismiss();
//                        new TicketListApi(getActivity()).execute();
                        Fragment fragment= new HelpDesk();
                        FragmentManager manager=getActivity().getSupportFragmentManager();
                        FragmentTransaction ft=manager.beginTransaction().addToBackStack(null);
                        ft.replace(R.id.container,fragment).commit();

                    }
                    else {
                        Util.errorDialog(context,jsonObject.optString("message"));
                    }
                }
            } catch (JSONException e) {
                Util.errorDialog(context,"Some Error! Please try again...");
                e.printStackTrace();
            }
        }

    }


    private class TicketListApi extends AsyncTask<String, Void, String> {
        Context context;

        public TicketListApi(Context context) {
            this.context = context;
            dialog=new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Util.showPgDialog(dialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();

            map.put("function","getMyTicket");
            map.put("memberId",MyPrefrences.getUserID(getActivity()));

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.Login,"GET",map);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", ": " + s);
            Util.cancelPgDialog(dialog);
            try {
                final JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {

                        jsonArray=jsonObject.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();
                            map.put("id",jsonObject1.optString("id").toString());
                            map.put("ticketNo",jsonObject1.optString("ticketNo").toString());
                            map.put("memberId",jsonObject1.optString("memberId").toString());
                            map.put("subject",jsonObject1.optString("subject").toString());
                            map.put("message",jsonObject1.optString("message").toString());
                            map.put("date_created",jsonObject1.optString("date_created").toString());
                            map.put("status",jsonObject1.optString("status").toString());
                            map.put("currentAction",jsonObject1.optString("currentAction").toString());
                            map.put("comment_by_admin",jsonObject1.optString("comment_by_admin").toString());
                            map.put("ticketClosedDate",jsonObject1.optString("ticketClosedDate").toString());

                            DataList.add(map);

                            adapter=new Adapter();
                            gridview.setAdapter(adapter);
                        }
//
                    }
                    else if (jsonObject.optString("status").equalsIgnoreCase("failure")){

                    }
                    else {
                        Util.errorDialog(context,jsonObject.getJSONObject("message").toString());
                    }
                }
            } catch (JSONException e) {
                Util.errorDialog(context,"Some Error! Please try again...");
                e.printStackTrace();
            }
        }

    }
    class Adapter extends BaseAdapter {
        TextView ticketNo,message,typeTicket,status,comment,date,adminT;
        LayoutInflater inflater;
        RelativeLayout discountTag;
        Adapter(){
            inflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return DataList.size();
        }

        @Override
        public Object getItem(int position) {
            return DataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView=inflater.inflate(R.layout.custonlistview_ticket,parent,false);

            ticketNo=(TextView)convertView.findViewById(R.id.ticketNo);
            message=(TextView)convertView.findViewById(R.id.message);
            typeTicket=(TextView)convertView.findViewById(R.id.typeTicket);
            status=(TextView)convertView.findViewById(R.id.status);
            comment=(TextView) convertView.findViewById(R.id.comment);
            date=(TextView) convertView.findViewById(R.id.date);
            adminT=(TextView) convertView.findViewById(R.id.adminT);

            ticketNo.setText("# "+DataList.get(position).get("ticketNo"));
            message.setText(DataList.get(position).get("message"));
            typeTicket.setText("Type: "+DataList.get(position).get("subject"));
            status.setText(DataList.get(position).get("currentAction"));
            comment.setText(DataList.get(position).get("comment_by_admin"));
            date.setText(DataList.get(position).get("date_created"));


            if (DataList.get(position).get("currentAction").equalsIgnoreCase("Open")){
                status.setTextColor(Color.BLUE);
            }
            else if (DataList.get(position).get("currentAction").equalsIgnoreCase("closed")){
                status.setTextColor(Color.parseColor("#35a76a"));
            }
            if (DataList.get(position).get("comment_by_admin").equalsIgnoreCase("")){
                comment.setVisibility(View.GONE);
                adminT.setVisibility(View.GONE);
            }
            return convertView;
        }
    }


}
