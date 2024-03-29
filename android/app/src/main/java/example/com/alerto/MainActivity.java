package example.com.alerto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView contactRV;
    private TextView noContactAlert;
    List<ContactItem> userslist;
    public static final String MyPREFERENCES = "FavPrefs" ;
    public static final String Name = "favKey";
    SharedPreferences sharedpreferences;
    JSONArray usersjson= new JSONArray();
    ArrayList<ContactItem> favlist;
    String PROJECT_NUMBER="559026050350";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String usersarray = sharedpreferences.getString(Name, null);

        contactRV = (RecyclerView) findViewById(R.id.favRecycler);
        noContactAlert = (TextView) findViewById(R.id.noContactAlert);
        noContactAlert.setVisibility(View.GONE);

//        if(usersarray.length()==0)
//        {
//            noContactAlert.setVisibility(View.GONE);
//        }

        try {
            if(usersarray!=null) {
                usersjson = new JSONArray(usersarray);
                setRVContent(usersjson, favlist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean updateStart = locationUpdate.togglePeriodicLocationUpdates();
//                if (updateStart)
//                    Snackbar.make(view, "Location Update started", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                else
//                    Snackbar.make(view, "Location Update stopped", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                popup_request();
            }
        });
        Intent intent = getIntent();
        if(intent.hasExtra("widget")){
            popup_request();
        }
//        GCMClientManager pushClientManager = new GCMClientManager(MainActivity.this, PROJECT_NUMBER);
//        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
//            @Override
//            public void onSuccess(String registrationId, boolean isNewRegistration) {
//                SharedPreferences sharedPreferences = getSharedPreferences("USER", 0);
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("gcmId", registrationId));
//                String url = "http://54.169.0.11:8000/users/"+sharedPreferences.getString("userid", "")+"/gcmId";
//
//                new HTTPPost(url, nameValuePairs, MainActivity.this){
//                    @Override
//                    public void gotResult(String s){
//
//                    }
//                };
//            }
//
//            @Override
//            public void onFailure(String ex) {
//                super.onFailure(ex);
//                Toast.makeText(getApplicationContext(), "registration failed.", Toast.LENGTH_SHORT).show();
//            }
//        });

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
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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

    private void popup_request() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

        View promptView = layoutInflater.inflate(R.layout.popup_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText input = (EditText) promptView.findViewById(R.id.userInput);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
//                        editTextMainScreen.setText(input.getText());
                        for (int i = 0; i < usersjson.length(); i++) {
                            try {
                                JSONObject obb = usersjson.getJSONObject(i);
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(obb.getString("phoneNumber"), null, input.getText().toString(), null, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        SharedPreferences sharedPreferences = getSharedPreferences("USER", 0);

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        List<NameValuePair> extraValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("userId", sharedPreferences.getString("userid", "")));
                        String usersstr = "";
                        for (int i = 0; i < usersjson.length(); i++) {
                            if (i == 0) {
                                try {
                                    JSONObject obb = usersjson.getJSONObject(i);
                                    usersstr += obb.getString("_id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    JSONObject obb = usersjson.getJSONObject(i);
                                    usersstr += "," + obb.getString("_id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        // Toast.makeText(getApplicationContext(), usersstr, Toast.LENGTH_LONG).show();
                        nameValuePairs.add(new BasicNameValuePair("users", usersstr));
                        nameValuePairs.add(new BasicNameValuePair("message", input.getText().toString()));
                        nameValuePairs.add(new BasicNameValuePair("lat", "31.77"));
                        nameValuePairs.add(new BasicNameValuePair("lng", "76.99"));
                        String url = "http://54.169.0.11:8000/users/alert/request";
                        new HTTPPost(url, nameValuePairs, MainActivity.this) {
                            @Override
                            public void gotResult(String s) {
                                // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "Alert sent!", Toast.LENGTH_LONG).show();
//                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                                if (s.equals("")) {
//                                    Toast.makeText(getApplicationContext(), "Username or Mobile Number already exists", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(s);
//                                        SharedPreferences sharedPreferences = getSharedPreferences("USER", 0);
//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        editor.putString("userid", jsonObject.getString("_id"));
//                                        editor.commit();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                                SharedPreferences sharedPreferences = getSharedPreferences("USER", 0);
                                Intent intent = new Intent(MainActivity.this, PushLocation.class);
                                intent.putExtra("userid", sharedPreferences.getString("userid", ""));
                                startService(intent);
                            }
                        };
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
    }

    public void setRVContent(JSONArray contactsArray, ArrayList<ContactItem> list) {
        list = new ArrayList<>();
        for(int i=0; i<contactsArray.length(); i++)
        {
            try {
                JSONObject cono = contactsArray.getJSONObject(i);
                ContactItem item = new ContactItem(cono.getString("username"), cono.getString("phoneNumber"), cono.getString("_id"));
                list.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(list.size()>0)
        {
            noContactAlert.setVisibility(View.GONE);
        }
        FavRecyclerAdapter adapter = new FavRecyclerAdapter(list, this);
        contactRV.setAdapter(adapter);
        contactRV.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

}
