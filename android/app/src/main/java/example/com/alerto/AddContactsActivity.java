package example.com.alerto;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddContactsActivity extends ActionBarActivity {
    private static final int RESULT_PICK_CONTACT = 1;
    public static final String MyPREFERENCES = "USER";
    public static final String Name = "nameKey";
    SharedPreferences sharedpreferences;
    JSONArray contactsArray;
    RecyclerView contactRV;
    TextView noContactAlert;
    ArrayList<ContactItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        contactRV = (RecyclerView) findViewById(R.id.contactsRecycler);
        noContactAlert = (TextView) findViewById(R.id.noContactAlert);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String contactsjson = sharedpreferences.getString(Name, null);
//        Toast.makeText(getApplicationContext(), contactsjson, Toast.LENGTH_LONG).show();
        contactsArray = new JSONArray();
        try {
            if(contactsjson!=null) {
                contactsArray = new JSONArray(contactsjson);
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        list = new ArrayList<>();

        setRVContent(contactsArray, list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                pickContact();
            }
        });
    }

    private void setRVContent(JSONArray contactsArray, ArrayList<ContactItem> list) {
        list = new ArrayList<>();
        for(int i=0; i<contactsArray.length(); i++)
        {
            try {
                JSONObject cono = contactsArray.getJSONObject(i);
                ContactItem item = new ContactItem(cono.getString("name"), cono.getString("phone"), cono.getString("_id"));
                list.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(list.size()>0)
        {
            noContactAlert.setVisibility(View.GONE);
        }
        ContactRecyclerAdapter adapter = new ContactRecyclerAdapter(list, getApplicationContext());
        contactRV.setAdapter(adapter);
        contactRV.setLayoutManager(new LinearLayoutManager(this));

    }

    public void pickContact()
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
//            textView1.setText(name);
//            textView2.setText(phoneNo);
            JSONObject obj=new JSONObject();
            obj.put("name",name);
            obj.put("phone", phoneNo);
//            if(contactsArray == null) {
//                JSONArray arr = new JSONArray();
//                arr.put(obj);
//            }
//            else
//            {
//                contactsArray.put(obj);
//            }
            contactsArray.put(obj);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString(Name, contactsArray.toString());
            editor.commit();
            setRVContent(contactsArray, list);

            Snackbar.make(getCurrentFocus(), "Contact \"" + name + "\" added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
