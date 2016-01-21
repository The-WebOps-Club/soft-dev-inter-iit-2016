package example.com.alerto;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int lat = 21,lon = 80, i = 0;
    Marker mapMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

    public void callAsynchronousTask(final GoogleMap googleMap) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            String url = "http://www.google.com";
                            new AsyncGet(getApplicationContext(), url, new AsyncGet.AsyncResult() {
                                @Override
                                public void gotResult(String s) {
                                    // Add a marker in Sydney and move the camera
                                    MarkerOptions options = new MarkerOptions();
                                    if(mapMarker != null){
                                        mapMarker.remove();
                                    }
                                    // following four lines requires 'Google Maps Android API Utility Library'
                                    // https://developers.google.com/maps/documentation/android/utility/
                                    // I have used this to display the time as title for location markers
                                    // you can safely comment the following four lines but for this info
                                    /*IconGenerator iconFactory = new IconGenerator(Maps.this);
                                    iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
                                    options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()));
                                    options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());*/

                                    LatLng currentLatLng = new LatLng(lat+0.001*i, lon+0.001*i);
                                    options.position(currentLatLng);
                                    mapMarker = googleMap.addMarker(options);
                                    //long atTime = mCurrentLocation.getTime();
                                    //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
                                    mapMarker.setTitle("Here");
                                    Log.d("hhhhhhhhhhh", "Marker added.............................");
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,19));
                                    Log.d("hhhhhhhhhhh", "Zoom done.............................");
                                }
                            });
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
                i++;
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 50000 ms
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        callAsynchronousTask(googleMap);
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
