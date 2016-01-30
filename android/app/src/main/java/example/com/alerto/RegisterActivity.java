package example.com.alerto;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


    @Override
    protected void onDestroy(){
        super.onDestroy();
        GCMRegistrar.onDestroy(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("username",nametextbox.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("phoneNumber",phonetextbox.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("gcmId", GCMRegistrar.getRegistrationId(getApplicationContext())));
                String url = "http://54.169.0.11:8000/users/create";
                new HTTPPost(url, nameValuePairs, RegisterActivity.this){
                    @Override
                    public void gotResult(String s){
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });

        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        Toast.makeText(getApplicationContext(), mPhoneNumber, Toast.LENGTH_LONG).show();

        phonetextbox.setText(mPhoneNumber);
    }

}
