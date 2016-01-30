package example.com.alerto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText nametextbox, phonetextbox;
    Button submit;
    String PROJECT_NUMBER="559026050350";


    @Override
    protected void onDestroy(){
        super.onDestroy();
        GCMRegistrar.onDestroy(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // TODO: Move the following code snippet to a place such that it is invoked each time app is opened.

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, "559026050350");
        } else {
            Log.v("Reg id", regId);

        }

        //////////////////////////////////////////////////

        nametextbox = (EditText) findViewById(R.id.nameTextBox);
        phonetextbox = (EditText) findViewById(R.id.phoneTextBox);
        submit = (Button) findViewById(R.id.buttonreg);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nametextbox.getText().toString();
                final String phone = phonetextbox.getText().toString();
                if(name.equals("") || phone.equals("")){
                    Toast.makeText(getApplicationContext(), "Fill your details", Toast.LENGTH_SHORT).show();
                    return;
                }
                GCMClientManager pushClientManager = new GCMClientManager(RegisterActivity.this, PROJECT_NUMBER);
                pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                    @Override
                    public void onSuccess(String registrationId, boolean isNewRegistration) {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("username", name));
                        nameValuePairs.add(new BasicNameValuePair("phoneNumber", phone));
                        nameValuePairs.add(new BasicNameValuePair("gcmId", registrationId));
                        String url = "http://54.169.0.11:8000/users/create";

                        new HTTPPost(url, nameValuePairs, RegisterActivity.this){
                            @Override
                            public void gotResult(String s){
                                if(s.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Username or Mobile Number already exists", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        SharedPreferences sharedPreferences = getSharedPreferences("USER", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("userid", jsonObject.getString("_id"));
                                        editor.commit();
                                        startActivity(new Intent(RegisterActivity.this, RegUsersActivity.class));
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

        try {
            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
//            Toast.makeText(getApplicationContext(), mPhoneNumber, Toast.LENGTH_LONG).show();
            phonetextbox.setText(mPhoneNumber);
        } catch (Exception E){
        SharedPreferences sharedPreferences = getSharedPreferences("USER",0);
        if(sharedPreferences.contains("userid")){
            startActivity(new Intent(this, RegUsersActivity.class));
        }

        }
    }

}
