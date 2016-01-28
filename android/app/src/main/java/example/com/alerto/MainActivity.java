package example.com.alerto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    //LocationUpdate locationUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("USER",0);
        if(sharedPreferences.contains("userid")){
            startActivity(new Intent(this, RegUsersActivity.class));
        }
        else
        startActivity(new Intent(this, RegisterActivity.class));
        /*locationUpdate = new LocationUpdate(this);
        locationUpdate.googleConnect();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean updateStart = locationUpdate.togglePeriodicLocationUpdates();
                if (updateStart)
                    Snackbar.make(view, "Location Update started", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else
                    Snackbar.make(view, "Location Update stopped", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_contacts) {
            startActivity(new Intent(MainActivity.this, AddContactsActivity.class));
            return true;
        }

        if (id == R.id.action_gcmtest) {
            startActivity(new Intent(MainActivity.this, GCMTestActivity.class));
            return true;
        }

        if (id == R.id.action_register) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            return true;
        }

        if (id == R.id.action_regusers) {
            startActivity(new Intent(MainActivity.this, RegUsersActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //locationUpdate.googleResume();

    }

    protected void onStart() {
        //locationUpdate.googleStart();
        super.onStart();
    }

    protected void onStop() {
        //locationUpdate.googleStop();
        super.onStop();
    }

}
