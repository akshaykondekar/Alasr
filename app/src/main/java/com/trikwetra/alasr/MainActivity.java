package com.trikwetra.alasr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trikwetra.alasr.helper.CurvedBottomNavigationView;
import com.trikwetra.alasr.mainFragments.Fragment_Home;
import com.trikwetra.alasr.mainFragments.Fragment_Mosque;
import com.trikwetra.alasr.mainFragments.Fragment_Schedule;
import com.trikwetra.alasr.mainFragments.Fragment_Setting;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    ImageButton ibtnSearch;
    Fragment selectedFragment = null;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;
    private String TAG = "MAP :: ";
    public static boolean mLocationPermissionGranted = false;
    public static Location mLastKnownLocation;
    public static FusedLocationProviderClient mFusedLocationProviderClient;
    public static CurvedBottomNavigationView mBottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("10/12/2019");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBottomNavView = findViewById(R.id.customBottomBar);
        mBottomNavView.setSelectedItemId(R.id.action_home);
        mBottomNavView.setOnNavigationItemSelectedListener(MainActivity.this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();

        ibtnSearch = findViewById(R.id.ibtn_search);
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                }
                else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            loadFragment(new Fragment_Home(), "10/12/2019");
                        }
                        else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            View decorView = getWindow().getDecorView();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else{
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                selectedFragment = new Fragment_Home();
                loadFragment(selectedFragment,"10/12/2019");
                break;
            case R.id.action_mosque:
                selectedFragment = new Fragment_Mosque();
                loadFragment(selectedFragment,"MOSQUE");
                break;
            case R.id.action_schedule:
                selectedFragment = new Fragment_Schedule();
                loadFragment(selectedFragment,"PRAYER SCHEDULE");
                break;
            case R.id.action_setting:
                selectedFragment = new Fragment_Setting();
                loadFragment(selectedFragment,"SETTINGS");
                break;
        }
        return true;
    }

    private void loadFragment(Fragment fragment, String title) {

        getSupportActionBar().setTitle(title);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_qibla) {
            Toast.makeText(MainActivity.this,"Qibla not found",Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.action_notification) {
            Toast.makeText(MainActivity.this,"Notifications not found",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mBottomNavView.getSelectedItemId() != R.id.action_home){
            mBottomNavView.setSelectedItemId(R.id.action_home);
        }
        else if(mBottomNavView.getSelectedItemId() == R.id.action_home){
            finishAffinity();
        }
        else{
            super.onBackPressed();
        }
    }
}
