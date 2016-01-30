package example.com.alerto;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PushLocation extends Service {
    LocationUpdate locationUpdate;
    String id;
    public PushLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER",0);
        id = sharedPreferences.getString("userid","");

        locationUpdate = new LocationUpdate(this){
            @Override
            public void onLocationChanged(Location location) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("location","{'lat':" + location.getLatitude() + ",'lng':" + location.getLongitude() + "}"));
                String url = "http://54.169.0.11:8000/users/" + id + "/location";
                new HTTPPost(url, nameValuePairs, PushLocation.this){
                    @Override
                    public void gotResult(String s){
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }
                };
            }
        };
        locationUpdate.googleConnect();
        locationUpdate.togglePeriodicLocationUpdates();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
