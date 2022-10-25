package com.khamvongsa.victor.go4lunch.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.manager.UserManager;
import com.khamvongsa.victor.go4lunch.model.User;
import com.khamvongsa.victor.go4lunch.ui.helper.LocaleHelper;
import com.khamvongsa.victor.go4lunch.ui.helper.NavigationHelper;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    //BottomNavigationView
    BottomNavigationView mBottomNavigationView;
    AppBarConfiguration mAppBarConfiguration;
    NavController mNavController;

    //NavigationDrawer
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;
    ActionBarDrawerToggle mToggle;

    private UserManager mUserManager = UserManager.getInstance();
    private String mUserPictureUrl;

    ImageView mUserPicture;
    TextView mUserName;
    TextView mUserEmail;

    //UpdateView
    private Boolean localeChanged = false;
    private String initialLocale;
    //Data User
    private UserViewModel mUserViewModel;
    private String mUserChosenRestaurant;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (initialLocale != null && !initialLocale.equals(LocaleHelper.getPersistedLocale(this))) {
            recreate();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialLocale = LocaleHelper.getPersistedLocale(this);
        Log.e(TAG, initialLocale);
        setContentView(R.layout.activity_main);

        mUserViewModel = new ViewModelProvider(this, FactoryViewModel.getInstance()).get(UserViewModel.class);

        //Initialize NavigationDrawer.
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_drawer_view);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        try {
            Field mCollapseIcon = mToolbar.getClass().getDeclaredField("mCollapseIcon");
            mCollapseIcon.setAccessible(true);
            Drawable drw = (Drawable) mCollapseIcon.get(mToolbar);
            drw.setTint(getResources().getColor(R.color.white));
        }
        catch (Exception e) {
        }
        // NavigationDrawer menu.
        mNavigationView.bringToFront();
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        View headerView = mNavigationView.getHeaderView(0);
        mUserName = headerView.findViewById(R.id.user_name_header_navigation_drawer);
        mUserEmail = headerView.findViewById(R.id.user_email_header_navigation_drawer);
        mUserPicture = headerView.findViewById(R.id.user_picture_header_navigation_drawer);
        setUserDataInNavigationDrawer();

        mUserViewModel.getChosenRestaurantId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String chosenRestaurantId) {
                mUserChosenRestaurant = chosenRestaurantId;
                Log.d(TAG, "Current CHOSEN_RESTAURANT_FIELD : " + mUserChosenRestaurant);
            }
        });
        mUserViewModel.getUserChosenRestaurant();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_drawer_lunch:
                        //Do some thing here
                        NavigationHelper.launchRestaurantActivity(MainActivity.this, RestaurantActivity.class, mUserChosenRestaurant);
                        // add navigation drawer item onclick method here
                        break;
                    case R.id.navigation_drawer_settings:
                        //Do some thing here
                        NavigationHelper.launchNewActivity(MainActivity.this, SettingsActivity.class);
                        // add navigation drawer item onclick method here
                        break;
                    case R.id.navigation_drawer_logout:
                        AuthUI.getInstance()
                                .signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        NavigationHelper.launchNewActivity(MainActivity.this, SignInActivity.class);
                                        finish();
                                    }
                                });
                        // add navigation drawer item onclick method here
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //Initialize Bottom Navigation View.
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);

        //Pass the ID's of Different destinations
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map_view, R.id.navigation_list_view, R.id.navigation_workmates)
                .build();

        //Initialize NavController.
        mNavController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mBottomNavigationView, mNavController);
    }

    public void setUserDataInNavigationDrawer(){
        Task<User> userData = mUserManager.getUserData();
        // If the user already exist in Firestore, we get his data (isMentor)
        userData.addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getUrlPicture() != null){
                mUserPictureUrl = documentSnapshot.getUrlPicture();
                Glide.with(mUserPicture)
                        .load(mUserPictureUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mUserPicture);
            }
            if (documentSnapshot.getUsername() != null){
                mUserName.setText(documentSnapshot.getUsername());
            }
            if (documentSnapshot.getMail() != null){
                mUserEmail.setText(documentSnapshot.getMail());
            }
        });
    }

    // TOGGLE MENU
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.string.settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {super.onBackPressed(); }
    }
}