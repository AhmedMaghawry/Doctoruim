package com.ezzat.doctoruim.View.Patient;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import com.bumptech.glide.util.Util;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.ListDoctorAdapter;
import com.ezzat.doctoruim.Control.RecycleView_Products.DoctorsAdapter;
import com.ezzat.doctoruim.Control.RecycleView_Products.VerticalSpacingDecoration;
import com.ezzat.doctoruim.Control.Utils.Constants;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Doctor;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.Model.UserType;
import com.ezzat.doctoruim.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class HomePatientActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DoctorsAdapter.DoctorAdapterListener {

    private SearchView searchView;
    private DoctorsAdapter adapterProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseController.getElement(USER_TABLE, Utils.getPhone(), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(HomePatientActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Utils.hideDialog();
                User u = (User) object;
                TextView name = drawer.findViewById(R.id.name);
                TextView phone = drawer.findViewById(R.id.phone);
                CircleImageView image = drawer.findViewById(R.id.profile);
                name.setText(u.getName());
                phone.setText(u.getPhone());
                Utils.setPhoto(HomePatientActivity.this, u.getImage(), image);
            }
        });

        setupMainPages();
    }

    private void setupMainPages() {
        final ViewPager viewPager = findViewById(R.id.vp_horizontal_ntb);
        final PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view;
                view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.view_doctor, null, false);
                setupDoctors(view);
                container.addView(view);
                return view;
            }
        };
        viewPager.setAdapter(adapter);
    }

    @SuppressLint("WrongConstant")
    private void setupDoctors(final View view) {
        DatabaseController.getAllUsersType(USER_TABLE, UserType.Doctor, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(HomePatientActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                final ArrayList<User> users = (ArrayList<User>) object;
                Utils.hideDialog();
                RecyclerView recyclerView = view.findViewById(R.id.rec_view);
                LinearLayoutManager llm = new LinearLayoutManager(HomePatientActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                adapterProducts = new DoctorsAdapter(HomePatientActivity.this, users, HomePatientActivity.this);
                recyclerView.addItemDecoration(new VerticalSpacingDecoration(5));
                recyclerView.setAdapter(adapterProducts);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if (query != null)
                    adapterProducts.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (query != null)
                    adapterProducts.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
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
        } else if (id == R.id.nav_reservation) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDoctorSelected(User user) {
        Toast.makeText(this, "Doctor " + user.getName() + " is Selected", Toast.LENGTH_SHORT).show();
    }
}
