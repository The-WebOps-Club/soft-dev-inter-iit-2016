package example.com.alerto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegUsersActivity extends AppCompatActivity {
    private RecyclerView contactRV;
    private TextView noContactAlert;
    List<ContactItem> userslist;
    public static final String MyPREFERENCES = "UserPrefs" ;
    public static final String Name = "usersKey";
    SharedPreferences sharedpreferences;
    JSONArray contactsArray;
    ArrayList<ContactItem> list;
    MainActivity mAc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String usersarray = sharedpreferences.getString(Name, null);
        userslist = new ArrayList<ContactItem>();
        mAc = new MainActivity();

        try {
            if(usersarray!=null) {
                contactsArray = new JSONArray(usersarray);
                for(int i=0;i<contactsArray.length();i++)
                {
                    try {
                        JSONObject obj = contactsArray.getJSONObject(i);
                        ContactItem user = new ContactItem(obj.getString("username"), obj.getString("phoneNumber"));
                        userslist.add(user);

                    } catch (Exception E)
                    {

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Toast.makeText(getApplicationContext(), contactsArray.toString(), Toast.LENGTH_SHORT).show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        contactRV = (RecyclerView) findViewById(R.id.usersRecycler);
        noContactAlert = (TextView) findViewById(R.id.noContactAlert);
        noContactAlert.setVisibility(View.GONE);

        UsersRecyclerAdapter adapter = new UsersRecyclerAdapter(userslist, getApplicationContext(),mAc);

        contactRV.setAdapter(adapter);
        contactRV.setLayoutManager(new LinearLayoutManager(RegUsersActivity.this));

        userslist = new ArrayList<>();
        new AsyncGet(getApplicationContext(), "http://54.169.0.11:8000/users", new AsyncGet.AsyncResult() {
            @Override
            public void gotResult(String s) {
                try {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Name, s);
                    editor.commit();
                    JSONArray usersarray = new JSONArray(s);
                    for(int i=0;i<usersarray.length();i++)
                    {
                        try {
                            JSONObject obj = usersarray.getJSONObject(i);
                            ContactItem user = new ContactItem(obj.getString("username"), obj.getString("phoneNumber"));
                            userslist.add(user);

                        } catch (Exception E)
                        {

                        }
                    }

                    UsersRecyclerAdapter adapter = new UsersRecyclerAdapter(userslist, getApplicationContext(), mAc);
                    contactRV.setAdapter(adapter);
                    contactRV.setLayoutManager(new LinearLayoutManager(RegUsersActivity.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setRVContent(JSONArray contactsArray, ArrayList<ContactItem> list) {
        list = new ArrayList<>();
        for(int i=0; i<contactsArray.length(); i++)
        {
            try {
                JSONObject cono = contactsArray.getJSONObject(i);
                ContactItem item = new ContactItem(cono.getString("name"), cono.getString("phone"));
                list.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(list.size()>0)
        {
            noContactAlert.setVisibility(View.GONE);
        }
        UsersRecyclerAdapter adapter = new UsersRecyclerAdapter(list, getApplicationContext(), mAc);
        contactRV.setAdapter(adapter);
        contactRV.setLayoutManager(new LinearLayoutManager(RegUsersActivity.this));

    }

}
