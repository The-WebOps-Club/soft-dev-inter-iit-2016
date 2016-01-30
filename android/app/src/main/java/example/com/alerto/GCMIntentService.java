package example.com.alerto;

/**
 * Created by adarsh on 28/1/16.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
        super("559026050350");
//        GCMRegistrar.register(this, "559026050350");

    }

    private static final String TAG = "GCMIntentService";

    @Override
    protected void onRegistered(Context arg0, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        // write gcm key push code here
    }


    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.i(TAG, "unregistered = " + arg1);
    }


    private void notify( String title, String text, String id){
        Log.i("title", title);
        Log.i("text", text);
        Uri sound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //builder.setSound(alarmSound);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon( R.mipmap.ic_launcher )
                        .setContentTitle( title )
                        .setContentText( text )
                        .setSound( sound_uri )
                        .setAutoCancel(true);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, AlertActivity.class);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("text", text);
        resultIntent.putExtra("id", id);


// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        // stackBuilder.addParentStack(Home.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        //int mId = 1234;
        mNotificationManager.notify(1234, mBuilder.build());
    }

    @SuppressWarnings("deprecation")
    @ Override
    protected void onMessage(Context context, Intent intent) {
        Log.d(TAG, "onMessage - context: " + context);
        String msg = intent.getStringExtra("message");
        String name="",id="";
        String jst = intent.getStringExtra("fromUser");
        try {
            JSONObject obj = new JSONObject(jst);
            Toast.makeText(getApplicationContext(), obj.getString("username"), Toast.LENGTH_LONG).show();
            name = obj.getString("username");
            id = obj.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
           // JSONObject msgJSON = new JSONObject(msg);
            String text = msg;
            String title = "Help "+ name +"!!";
            notify(title, text, id);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        // make notifications here
        Log.i("gcm", "received");

    }

    @
            Override
    protected void onError(Context arg0, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @
            Override
    protected boolean onRecoverableError(Context context, String errorId) {
        return super.onRecoverableError(context, errorId);
    }
}