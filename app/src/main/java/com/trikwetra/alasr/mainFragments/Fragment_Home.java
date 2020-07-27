package com.trikwetra.alasr.mainFragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trikwetra.alasr.MainActivity;
import com.trikwetra.alasr.R;
import com.trikwetra.alasr.adapter.HomeViewPagerAdapter;
import com.trikwetra.alasr.bean.NearByPlaces;
import com.trikwetra.alasr.bean.SelectedMosqueAdvBean;
import com.trikwetra.alasr.helper.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_Home extends Fragment {
    private MapView mMapView;
    private GoogleMap mMap;
    ArrayList<LatLng> places;
    ArrayList<SelectedMosqueAdvBean> selectedMosqueAdvBeanArrayList;
    private BitmapDescriptor myLocationIcon, destIcon;
    private Polyline line;
    List<Marker> markerList;
    private float MAP_ZOOM = 6.5f;
    ViewPager2 vpSelMosAdv;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    private TextView tvPrayerList;
    private ConstraintLayout clViewpager, clNextPrayer;
    Animation bottomUp, bottomDown, bottomDownToPosition, bottomUpToPosition;
    Boolean visibility = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        vpSelMosAdv = rootView.findViewById(R.id.vp_sel_mos_adv);
        tvPrayerList = rootView.findViewById(R.id.tv_prayer_list);
        clViewpager = rootView.findViewById(R.id.cl_viewpager);
        clNextPrayer = rootView.findViewById(R.id.cl_next_prayer);


        vpSelMosAdv.setAdapter(new HomeViewPagerAdapter(getContext(),
                selectedMosqueAdvBeanArrayList, vpSelMosAdv));

        vpSelMosAdv.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                currentPage = vpSelMosAdv.getCurrentItem();

            }
        });

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4) {
                    currentPage = 0;
                }
                vpSelMosAdv.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        vpSelMosAdv.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View view, float position) {
                final float MIN_SCALE = 0.85f;
                final float MIN_ALPHA = 0.5f;
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0f);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        view.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0f);
                }
            }
        });

        bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
        bottomDownToPosition = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down_to_postition);
        bottomUpToPosition = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up_to_position);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
