package bizzduniya.app.bizzdiniya.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import bizzduniya.app.bizzdiniya.Fragments.Aboutus;
import bizzduniya.app.bizzdiniya.Fragments.AllEvents;
import bizzduniya.app.bizzdiniya.Fragments.ContactUs;
import bizzduniya.app.bizzdiniya.Fragments.Dashboard;
import bizzduniya.app.bizzdiniya.Fragments.Feedback;
import bizzduniya.app.bizzdiniya.Fragments.HelpDesk;
import bizzduniya.app.bizzdiniya.Fragments.Home;
import bizzduniya.app.bizzdiniya.Fragments.Inquiry;
import bizzduniya.app.bizzdiniya.Fragments.LatestNews;
import bizzduniya.app.bizzdiniya.Fragments.MangeBusiness;
import bizzduniya.app.bizzdiniya.Fragments.ProductListing;
import bizzduniya.app.bizzdiniya.Fragments.Profile;
import bizzduniya.app.bizzdiniya.Fragments.SearchProduct;
import bizzduniya.app.bizzdiniya.Fragments.TermAndCondition;
import bizzduniya.app.bizzdiniya.Fragments.TradeShow;
import bizzduniya.app.bizzdiniya.R;
import bizzduniya.app.bizzdiniya.Utils.MyPrefrences;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomeAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


//        getSupportActionBar().setTitle("My title 123456789101112134578");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView nameUser=(TextView)header.findViewById(R.id.nameUser);
        TextView mobileNo=(TextView)header.findViewById(R.id.mobileNo);
        title=findViewById(R.id.title);


        nameUser.setText(MyPrefrences.getUSENAME(getApplicationContext()));
        mobileNo.setText(MyPrefrences.getMobile(getApplicationContext()));

        Fragment fragment = new Home();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).commit();
        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


        if (MyPrefrences.getUserLogin(getApplicationContext())==true){
            nameUser.setText(MyPrefrences.getUSENAME(getApplicationContext()).substring(0,1).toUpperCase()+MyPrefrences.getUSENAME(getApplicationContext()).substring(1).toLowerCase());
            mobileNo.setText("+91 "+MyPrefrences.getMobile(getApplicationContext()));

            Menu menu = navigationView.getMenu();
            MenuItem nav_login = menu.findItem(R.id.nav_logout);
            nav_login.setTitle("Logout");
        }

        else if (MyPrefrences.getUserLogin(getApplicationContext())==false){
            nameUser.setText("Guest");
            mobileNo.setText("+91 "+"XXXXXXXXXX");

            Menu menu = navigationView.getMenu();
            MenuItem nav_login = menu.findItem(R.id.nav_logout);
            nav_login.setTitle("Login");
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_profile) {
            // Handle the camera action
            Fragment fragment = new Profile();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_home) {
            Fragment fragment = new Home();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


        } else if (id == R.id.nav_dashboard) {
            Fragment fragment = new Dashboard();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_wishlist) {

        } else if (id == R.id.nav_event) {
            Fragment fragment = new AllEvents();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
        } else if (id == R.id.nav_news) {
            Fragment fragment = new LatestNews();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_tradefare) {
            Fragment fragment = new TradeShow();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


        } else if (id == R.id.nav_refer) {

        } else if (id == R.id.nav_refer) {

        } else if (id == R.id.nav_maange) {
            Fragment fragment = new MangeBusiness();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


        } else if (id == R.id.nav_product) {

            Fragment fragment = new ProductListing();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

//           Intent intent=new Intent(HomeAct.this,AddProduct.class);
//           startActivity(intent);

        } else if (id == R.id.nav_enquiry) {

            Fragment fragment = new Inquiry();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
        } else if (id == R.id.nav_logs) {

        } else if (id == R.id.nav_promote) {

        } else if (id == R.id.nav_post) {

        } else if (id == R.id.nav_search) {
            Fragment fragment = new SearchProduct();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_about) {
            Fragment fragment = new Aboutus();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
        } else if (id == R.id.nav_fedback) {
            Fragment fragment = new Feedback();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
        } else if (id == R.id.nav_help) {
            Fragment fragment = new HelpDesk();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
        } else if (id == R.id.nav_tnc) {
            Fragment fragment = new TermAndCondition();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_contact) {
            Fragment fragment = new ContactUs();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_logout) {

            MyPrefrences.resetPrefrences(HomeAct.this);
            Intent intent = new Intent(HomeAct.this, Login.class);
            startActivity(intent);
            finishAffinity();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
