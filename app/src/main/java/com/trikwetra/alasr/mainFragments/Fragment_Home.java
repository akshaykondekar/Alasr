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

        markerList = new ArrayList<>();

        places = new ArrayList<>();
        places.add(new LatLng(16.6883133, 74.4612817));
        places.add(new LatLng(16.7076433, 74.4689206));
        places.add(new LatLng(16.707782, 74.4773161));
        places.add(new LatLng(16.7095022, 74.4402356));
        places.add(new LatLng(16.6891011, 74.4578651));
        places.add(new LatLng(16.749198, 74.4192259));
        places.add(new LatLng(16.7154968, 74.456129)); // shortest khanjire

        mMapView = rootView.findViewById(R.id.mv_home);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        selectedMosqueAdvBeanArrayList = new ArrayList<>();

        selectedMosqueAdvBeanArrayList.add(new SelectedMosqueAdvBean("Houghton Masjid", "1.5 KM",
                "15 Minutes", R.drawable.mosque));
        selectedMosqueAdvBeanArrayList.add(new SelectedMosqueAdvBean("", "", "",
                R.drawable.banner_one));
        selectedMosqueAdvBeanArrayList.add(new SelectedMosqueAdvBean("", "", "",
                R.drawable.banner_two));
        selectedMosqueAdvBeanArrayList.add(new SelectedMosqueAdvBean("", "", "",
                R.drawable.banner_three));
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

        tvPrayerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.mBottomNavView.setSelectedItemId(R.id.action_schedule);
                /*getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Fragment_Schedule())
                        .commit();*/
            }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);

                /*Collections.sort(places,new NearByPlaces(new LatLng(MainActivity.mLastKnownLocation.getLatitude(),
                        MainActivity.mLastKnownLocation.getLongitude())));*/

                if (mMap != null && MainActivity.mLastKnownLocation != null) {
                    myLocationIcon =
                            getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.ic_my_location));
                    destIcon =
                            getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.ic_near_mosque));
                    CameraPosition cameraPosition =
                            new CameraPosition.Builder().target(
                                    new LatLng(MainActivity.mLastKnownLocation.getLatitude(),
                                            MainActivity.mLastKnownLocation.getLongitude())
                            ).zoom(MAP_ZOOM).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    placeNearestMarkers();
                    drawRoute(places.get(0).latitude,places.get(0).longitude);
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (markerList.contains(marker)) {
                            Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                            //drawRoute(marker.getPosition().latitude,marker.getPosition().longitude);
                        }
                        return false;
                    }
                });

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if(!visibility){
                            visibility = true;

                            clNextPrayer.startAnimation(bottomUp);
                            clNextPrayer.setVisibility(View.GONE);

                            clViewpager.startAnimation(bottomDown);
                            clViewpager.setVisibility(View.GONE);
                        }
                        else {
                            visibility = false;
                            clNextPrayer.startAnimation(bottomDownToPosition);
                            clNextPrayer.setVisibility(View.VISIBLE);

                            clViewpager.startAnimation(bottomUpToPosition);
                            clViewpager.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void placeNearestMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position( new LatLng(MainActivity.mLastKnownLocation.getLatitude(),
                       MainActivity.mLastKnownLocation.getLongitude()))
                .icon(myLocationIcon));
        for (int i=0; i<=4; i++) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(places.get(i).latitude, places.get(i).longitude))
                    .icon(destIcon));
            markerList.add(i, marker);
        }
    }

    private void drawRoute(double latitude, double longitude) {

        String urlTopass = makeURL(MainActivity.mLastKnownLocation.getLatitude(),
                MainActivity.mLastKnownLocation.getLongitude(), latitude, longitude);

        new connectAsyncTask(urlTopass).execute();
    }

    public String makeURL(double sourcelat, double sourcelog, double destlat,
                          double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append((sourcelat));
        urlString.append(",");
        urlString.append((sourcelog));
        urlString.append("&destination=");// to
        urlString.append((destlat));
        urlString.append(",");
        urlString.append((destlog));
        urlString.append("&key=");// to
        urlString.append(getResources().getString(R.string.google_maps_key));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void drawPath(String result) {
        if (line != null) {
            mMap.clear();
        }

        placeNearestMarkers();
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            PolylineOptions options = new PolylineOptions().width(8).color(Color.DKGRAY).geodesic(true);
            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                options.add(point);
            }
            line = mMap.addPolyline(options);

            LatLngBounds.Builder bc = new LatLngBounds.Builder();

            for (LatLng item : list) {
                bc.include(item);
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 280));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser= new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            Log.d("URL :::: ", url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                Log.d("RESULT :: ", result);
                drawPath(result);
            }
        }
    }
}
