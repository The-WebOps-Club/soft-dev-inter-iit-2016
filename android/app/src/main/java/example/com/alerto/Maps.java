package example.com.alerto;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int lat = 13,lon = 80;
    Marker mapMarker;
    MarkerOptions options;
    String trackUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        trackUserId = getIntent().getStringExtra("id");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void animateMarker(final GoogleMap mGoogleMapObject, final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMapObject.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
                mGoogleMapObject.moveCamera(CameraUpdateFactory.newLatLng(toPosition));
            }
        });
    }

    public void callAsynchronousTask(final GoogleMap googleMap) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            String url = "http://54.169.0.11:8000/users/" + trackUserId + "/location";
                            new AsyncGet(getApplicationContext(), url, new AsyncGet.AsyncResult() {
                                @Override
                                public void gotResult(String s) {
                                    if(s.length()<=1)
                                        return;
                                    // Add a marker in Sydney and move the camera
                                    /*if(mapMarker != null){
                                        mapMarker.remove();
                                    }*/
                                    // following four lines requires 'Google Maps Android API Utility Library'
                                    // https://developers.google.com/maps/documentation/android/utility/
                                    // I have used this to display the time as title for location markers
                                    // you can safely comment the following four lines but for this info
                                    /*IconGenerator iconFactory = new IconGenerator(Maps.this);
                                    iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
                                    options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()));
                                    options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());*/
                                    try {
//                                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                        JSONObject pos = new JSONObject(s.substring(1,s.length()-1));
                                        LatLng currentLatLng = new LatLng(pos.getLong("lat"), pos.getLong("lng"));
                                    /*options.position(currentLatLng);
                                    mapMarker = googleMap.addMarker(options);
                                    //long atTime = mCurrentLocation.getTime();
                                    //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
                                    mapMarker.setTitle("Here");
                                    Log.d("hhhhhhhhhhh", "Marker added.............................");
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,19));*/
                                        animateMarker(googleMap, mapMarker, currentLatLng, false);
                                        Log.d("hhhhhhhhhhh", "Zoom done.............................");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 50000 ms
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        options = new MarkerOptions();
        String url = "http://54.169.0.11:8000/users/" + trackUserId + "/location";
        new AsyncGet(getApplicationContext(), url, new AsyncGet.AsyncResult() {
            @Override
            public void gotResult(String s) {
                if(s.length()<=1)
                    return;
                // Add a marker in Sydney and move the camera
                                    /*if(mapMarker != null){
                                        mapMarker.remove();
                                    }*/
                // following four lines requires 'Google Maps Android API Utility Library'
                // https://developers.google.com/maps/documentation/android/utility/
                // I have used this to display the time as title for location markers
                // you can safely comment the following four lines but for this info
                                    /*IconGenerator iconFactory = new IconGenerator(Maps.this);
                                    iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
                                    options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()));
                                    options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());*/
                try {
                    // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    JSONObject pos = new JSONObject(s.substring(1,s.length()-1));
                    LatLng currentLatLng = new LatLng(pos.getLong("lat"), pos.getLong("lng"));
                                    /*options.position(currentLatLng);
                                    mapMarker = googleMap.addMarker(options);
                                    //long atTime = mCurrentLocation.getTime();
                                    //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
                                    mapMarker.setTitle("Here");
                                    Log.d("hhhhhhhhhhh", "Marker added.............................");
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,19));*/
                    options.position(currentLatLng);
                    mapMarker = googleMap.addMarker(options);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                    callAsynchronousTask(googleMap);
                    Log.d("hhhhhhhhhhh", "Zoom done.............................");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
