package example.com.alerto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import example.com.alerto.contactloader.Contact;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ContactViewHolder> {

    private JSONArray usersjson = new JSONArray();
    private List<ContactItem> itemList;
    Context c;
    private String[] colors = new String[]{
            "#F44336",
            "#E91E63",
            "#9C27B0",
            "#673AB7",
            "#3F51B5",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#BF360C",
            "#3E2723"

    };
    MainActivity mainActivity;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "FavPrefs" ;
    public static final String Name = "favKey";

    public UsersRecyclerAdapter(List<ContactItem> itemList, Context context, MainActivity mainActivity) {
        this.itemList = itemList;
        this.c = context;
        this.mainActivity = mainActivity;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String usersarray = sharedpreferences.getString(Name, null);
        try {
            if(usersarray!=null) {
                usersjson = new JSONArray(usersarray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder,final int i) {
        final ContactItem ci = itemList.get(i);
        contactViewHolder.nameText.setText(ci.getItemName());
        contactViewHolder.phoneText.setText(ci.getPhoneNo());
        if(ci.getItemName().length()>1) {
            contactViewHolder.iconText.setText((ci.getItemName().substring(0, 1).toUpperCase()));
        }
        contactViewHolder.iconBack.setColorFilter(Color.parseColor(colors[i % 16]));
        contactViewHolder.llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFavs(ci);
                Intent intent = new Intent(c.getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            }
        });
    }

    public void updateFavs(ContactItem ci)
    {
        JSONObject obj=new JSONObject();
        try {
            obj.put("username",ci.getItemName());
            obj.put("phoneNumber", ci.getPhoneNo());
            obj.put("_id", ci.getId());
            usersjson.put(obj);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Name, usersjson.toString());
            editor.commit();
            Toast.makeText(c.getApplicationContext(), usersjson.toString(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.users_card, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView nameText;
        protected TextView phoneText;
        protected TextView iconText;
        protected ImageView iconBack;
        protected LinearLayout llUser;

        public ContactViewHolder(View v) {
            super(v);
            nameText =  (TextView) v.findViewById(R.id.nameText);
            phoneText = (TextView) v.findViewById(R.id.phoneText);
            iconText = (TextView) v.findViewById(R.id.icontext);
            iconBack = (ImageView) v.findViewById(R.id.iconBack);
            llUser = (LinearLayout) v.findViewById(R.id.lluser);
        }
    }
}