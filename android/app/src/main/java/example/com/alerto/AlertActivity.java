package example.com.alerto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlertActivity extends AppCompatActivity {
    TextView titletext, texttext;
    Button mapbutton, acceptbutton;
    String PROJECT_NUMBER="559026050350";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titletext = (TextView) findViewById(R.id.titleText);
        texttext = (TextView) findViewById(R.id.textText);

        mapbutton = (Button) findViewById(R.id.mapbutton);


        final Bundle bundle = getIntent().getExtras();

        if(bundle.getString("title")!= null)
        {
            titletext.setText(bundle.getString("title"));
        }

        if(bundle.getString("text")!= null)
        {
            texttext.setText(bundle.getString("text"));
        }
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertActivity.this, Maps.class);
                intent.putExtra("id",bundle.getString("id"));
                startActivity(intent);
            }
        });

        acceptbutton = (Button) findViewById(R.id.acceptbutton);
        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GCMClientManager pushClientManager = new GCMClientManager(AlertActivity.this, PROJECT_NUMBER);
                pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                    @Override
                    public void onSuccess(String registrationId, boolean isNewRegistration) {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        SharedPreferences sharedPreferences = getSharedPreferences("USER", 0);
                        nameValuePairs.add(new BasicNameValuePair("fromUserId", sharedPreferences.getString("userid", "")));
                        nameValuePairs.add(new BasicNameValuePair("userId", bundle.getString("id")));
                        nameValuePairs.add(new BasicNameValuePair("gcmId", registrationId));
                        String url = "http://54.169.0.11:8000/users/alert/accept";

                        new HTTPPost(url, nameValuePairs, AlertActivity.this) {
                            @Override
                            public void gotResult(String s) {
                                if (s.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Username or Mobile Number already exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        SharedPreferences sharedPreferences = getSharedPreferences("USER", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("userid", jsonObject.getString("_id"));
                                        editor.commit();
                                        startActivity(new Intent(AlertActivity.this, RegUsersActivity.class));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                    }

                    @Override
                    public void onFailure(String ex) {
                        super.onFailure(ex);
                        Toast.makeText(getApplicationContext(), "registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
