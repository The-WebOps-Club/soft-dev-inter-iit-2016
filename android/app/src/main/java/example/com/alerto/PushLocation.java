package example.com.alerto;

import android.app.Service;
import android.content.Intent;
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


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public int onStartCommand (Intent intent, int flags, int startId) {
        try {
            id = (String) intent.getExtras().get("userid");
        }catch (Exception e ){

        }
        Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT).show();
        locationUpdate = new LocationUpdate(PushLocation.this){
            @Override
            public void onLocationChanged(Location location) {
                super.onLocationChanged(location);
                Toast.makeText(PushLocation.this, "hhhhhhhh",Toast.LENGTH_SHORT).show();
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
        //locationUpdate.togglePeriodicLocationUpdates();
        return 0;
    }
}
