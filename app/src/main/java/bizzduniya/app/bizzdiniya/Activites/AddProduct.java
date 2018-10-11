package bizzduniya.app.bizzdiniya.Activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bizzduniya.app.bizzdiniya.Fragments.Home;
import bizzduniya.app.bizzdiniya.Fragments.SubCategory;
import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.Api;
import bizzduniya.app.bizzdiniya.Utils.AppController;
import bizzduniya.app.bizzdiniya.Utils.JSONParser;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import bizzduniya.app.bizzdiniya.Utils.Util;

public class AddProduct extends AppCompatActivity {

    Spinner subCatSpin,catSpin,subCatSpin2;
    Dialog dialog, dialog4;
    List<HashMap<String,String>> AllProducts ;
    List<HashMap<String,String>> AllProducts2 ;
    List<HashMap<String,String>> AllProducts3 ;
    ArrayAdapter aa;
    ArrayAdapter aa2;
    ArrayAdapter aa3;

    List<String> CatList = new ArrayList<String>();
    List<String> CatList2 = new ArrayList<String>();
    List<String> CatList3 = new ArrayList<String>();
    LinearLayout linearTihird;

    EditText name,code,desc,feature,spicification,otherDetail,keyword;
    Button bubmitData;
    RadioGroup radioGroup;
    String id="",id2="",id3="";
    TextView uploadImage;
    CheckBox checkTNC;


    private static final int REQUEST_PICK_IMAGE = 1002;
    Bitmap imageBitmap;
    ImageView imageView2;
    File f=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setTitle("Add New Products");

        catSpin=findViewById(R.id.catSpin);
        subCatSpin=findViewById(R.id.subCatSpin);
        subCatSpin2=findViewById(R.id.subCatSpin2);
        linearTihird=findViewById(R.id.linearTihird);
        bubmitData=findViewById(R.id.bubmitData);
        name=findViewById(R.id.name);
        code=findViewById(R.id.code);
        desc=findViewById(R.id.desc);
        feature=findViewById(R.id.feature);
        spicification=findViewById(R.id.spicification);
        otherDetail=findViewById(R.id.otherDetail);
        keyword=findViewById(R.id.keyword);
        uploadImage=findViewById(R.id.uploadImage);
        checkTNC=findViewById(R.id.checkTNC);
        imageView2=findViewById(R.id.imageView2);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);



        dialog=new Dialog(AddProduct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        AllProducts = new ArrayList<>();
        AllProducts2 = new ArrayList<>();
        AllProducts3 = new ArrayList<>();

        bubmitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectedId);

//                Toast.makeText(AddProduct.this,radioButton.getText(), Toast.LENGTH_SHORT).show();


                //Log.d("sdfsdfsdgsdgsd",f.toString());
                String path= null;
                String filename= null;
                try {
                    path = f.toString();
                    filename = path.substring(path.lastIndexOf("/")+1);
                    Log.d("dsfdfsdfsfs",filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (checkTNC.isChecked()) {
                    PostData(radioButton.getText().toString(),path,filename);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please click on T&C.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        catSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id=AllProducts.get(i).get("id");
                subCatData(id);
                Log.d("sdfsdfsdfsd",id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subCatSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id2=AllProducts2.get(i).get("id");
                String plus=AllProducts2.get(i).get("plus");


                if (plus.equalsIgnoreCase("yes")){
                    linearTihird.setVisibility(View.VISIBLE);
                    subCatData2(id2);

                }
                else if (plus.equalsIgnoreCase("no")){
                    linearTihird.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        subCatSpin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id3=AllProducts3.get(i).get("id");


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPermissionGranted()){
                    pickImage();
                }else{
                    ActivityCompat.requestPermissions(AddProduct.this, new String[]{Manifest.permission.CAMERA}, 1);
                }



            }
        });

        JsonObjectRequest jsonObjReqOffers = new JsonObjectRequest(Request.Method.GET,
                Api.categoryList, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());
                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");
                    HashMap<String,String> hashMap = null;
                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllProducts.clear();
                        CatList.clear();
                        JSONArray jsonArray=response.getJSONArray("message");

                        for (int i=0;i<jsonArray.length();i++) {
                           JSONObject jsonObject = jsonArray.getJSONObject(i);


                            HashMap<String,String> map=new HashMap<>();
                            map.put("id", jsonObject.optString("id"));
                            map.put("category", jsonObject.optString("category"));
                            // map.put("display_name", jsonObject.optString("display_name"));
                            map.put("icon", jsonObject.optString("icon"));

                            CatList.add(jsonObject.optString("category"));

                            AllProducts.add(map);

                            aa = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,CatList);
                            aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            catSpin.setAdapter(aa);

                        }
                    }
//                    else if (response.getString("status").equalsIgnoreCase("failure")){
//                        relativeLayout1.setVisibility(View.GONE);
//                        cardView1.setVisibility(View.GONE);
//
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("dfsdfsdfsdfgsd",e.getMessage());
                    Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReqOffers.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReqOffers);



    }

    private void PostData(final String produ,String filePath,String fileName) {

        try {
            Log.d("sdfsdfasdfsdfsdf1",filePath);
            Log.d("sdfsdfasdfsdfsdf2",fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }


        new AddProductData(produ,filePath,fileName).execute();


//        Util.showPgDialog(dialog);
//        Log.d("dfdgdgdfgd","true");
//        // RequestQueue queue = Volley.newRequestQueue(Login.this);
//        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.addServices,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("Responseadd", response);
//                        Util.cancelPgDialog(dialog);
//                        try {
//                            JSONObject jsonObject=new JSONObject(response);
//                            if (jsonObject.getString("status").equalsIgnoreCase("success")){
//
//
//                            }
//                            else{
//                                Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("dfsfsdfsdfsdf",error.toString());
//                        Toast.makeText(AddProduct.this, "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
//                        Util.cancelPgDialog(dialog);
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//
//
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("service_type", produ.toString());
//                params.put("cat_id", id.toString());
//                params.put("service_id", id2.toString());
//                params.put("sub_service_id", id3.toString());
//                params.put("adnify_uid", MyPrefrences.getUserID(getApplicationContext()));
//                params.put("service_name", name.getText().toString());
//                params.put("service_code", code.getText().toString());
//                params.put("service_desc", desc.getText().toString());
//                params.put("service_features", feature.getText().toString());
//                params.put("service_specifications", spicification.getText().toString());
//                params.put("service_other_details", otherDetail.getText().toString());
//                params.put("packing_type", "");
//                params.put("service_keyword", keyword.getText().toString());
//
//                return params;
//            }
//        };
//
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        postRequest.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(postRequest);
//

    }



    private class AddProductData extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        String val, path, fName, min, kmsDone, mobile, emailID, brand;
        HashMap<String, String> params = new HashMap<>();

        //EditText descreption,ageOfProd,headline,min,kmsDone,mobile,emailID;
        AddProductData(String val,String path,String fName) {
            this.val = val;
            this.path = path;
            this.fName = fName;

        }

        @Override
        protected void onPreExecute() {
          Util.showPgDialog(dialog);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonObject = null;
            try {

                jsonObject = uploadImageFile(AddProduct.this, val,path, fName);


                if (jsonObject != null) {

                    return jsonObject;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            String message = "";
            String data = "";

//            if (progress.isShowing())
//                progress.dismiss();

            Util.cancelPgDialog(dialog);
            if (json != null) {


                if (json.optString("status").equalsIgnoreCase("success")) {


                    Toast.makeText(getApplicationContext(), "Product Add Successfully..", Toast.LENGTH_LONG).show();

//                    startActivity(new Intent(EditPhotos.this,   EditPhotos.class));
//                    finish();


                } else {
//                    Toast.makeText(PostAdd.this, "Error " + json, Toast.LENGTH_LONG).show();
                    //Util.errorDialog(EditPhotos.this, json.optString("message"));
                }
            }
        }

    }

    private JSONObject uploadImageFile(Context context,String value, String filepath1, String fileName1) {

        // sourceFile2= new File("");

        File sourceFile1 = new File(filepath1);

        String result = null;
        Log.e("FindPlayerPageAsync", "File...::::" + sourceFile1 + " : " + sourceFile1.exists());
        Log.e("file name", ": " + fileName1);
        JSONObject jsonObject = null;

        try {

            ////for image
            final MediaType MEDIA_TYPE_PNG = filepath1.endsWith("png") ? MediaType.parse("image/png") : MediaType.parse("image");

            Log.e("file name", ": " + fileName1);

            //   Log.d("fgdgdfgdfgdf1",getIntent().getStringExtra("areatypenum"));

            Log.d("dfsdfsdgfsdgd",id.toString());
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)

                    .addFormDataPart("service_type", value)
                    .addFormDataPart("cat_id", id.toString())
                    .addFormDataPart("service_id",id2.toString())
                    .addFormDataPart("sub_service_id", id3.toString())
                    .addFormDataPart("adnify_uid", MyPrefrences.getUserID(getApplicationContext()))
                    .addFormDataPart("service_name",  name.getText().toString())
                    .addFormDataPart("service_code", code.getText().toString())
                    .addFormDataPart("service_desc", desc.getText().toString())
                    .addFormDataPart("service_features", feature.getText().toString())
                    .addFormDataPart("service_specifications", spicification.getText().toString())
                    .addFormDataPart("service_other_details", otherDetail.getText().toString())
                    .addFormDataPart("packing_type", "")
                    .addFormDataPart("service_keyword", keyword.getText().toString())

                    .addFormDataPart("image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                    .build();

           // Log.d("dfdsgsdgdfgdfh",id);

//            Log.d("fvfgdgdfhgghfhgdfh", amounts.getText().toString().replace("₹ ", ""));
//            Log.d("fvfgdgdfhgdfhqwdfs",amounts.getText().toString().replace("₹ ", ""));

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.addServices)
//                    .url("http://templatestheme.com/demo/tradeone/ws/post_offer.php")
                    // .addHeader("enctype", "multipart/form-data")
                    .post(requestBody)
                    .build();


            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            client.setWriteTimeout(15, TimeUnit.SECONDS);
            client.setReadTimeout(15, TimeUnit.SECONDS);


            Log.e("request1", ":url:  " + request.urlString() + ", header: " + request.headers() + ", body " + request.body());
            com.squareup.okhttp.Response response = client.newCall(request).execute();
            result = response.body().string();
            Log.e("responseMultipart", ": " + result);
            jsonObject = new JSONObject(result);
            Log.e("result", ": " + result);
            return jsonObject;
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("FindPlayerPageAsync", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("FindPlayerPageAsync", "Other Error: " + e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();
        }
        return jsonObject;
    }



    private void subCatData(String id) {

        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.subcategoryByCategoryId+"?categoryId="+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());
                Util.cancelPgDialog(dialog);
                try {


                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllProducts2.clear();
                        CatList2.clear();

                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("cat_id", jsonObject.optString("cat_id"));
                            map.put("service_name", jsonObject.optString("service_name"));
                            map.put("plus", jsonObject.optString("plus"));

                            CatList2.add(jsonObject.optString("service_name"));


                            aa2 = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,CatList2);
                            aa2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            subCatSpin.setAdapter(aa2);

                            AllProducts2.add(map);



                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);



    }


    private void subCatData2(String id) {

        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.plusSubcategoryByCategoryId+"?categoryId="+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());
                Util.cancelPgDialog(dialog);
                try {


                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllProducts3.clear();
                        CatList3.clear();

                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("cat_id", jsonObject.optString("cat_id"));
                            map.put("sub_service_name", jsonObject.optString("sub_service_name"));

                            CatList3.add(jsonObject.optString("sub_service_name"));


                            aa3 = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,CatList3);
                            aa3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            subCatSpin2.setAdapter(aa3);

                            AllProducts3.add(map);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);



    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public void pickImage() {
        startActivityForResult(new Intent(this, ImagePickerActivity.class), REQUEST_PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    String imagePath = data.getStringExtra("image_path");

                    setImage(imagePath);
                    break;
            }
        } else {
            System.out.println("Failed to load image");
        }
    }

    private void setImage(String imagePath) {
        imageView2.setImageBitmap(getImageFromStorage(imagePath));
    }



    private Bitmap getImageFromStorage(String path) {
        try {
             f = new File(path);
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 512, 512);

            Log.d("sdfasafsdfsdfsdfsdf",f.toString());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
