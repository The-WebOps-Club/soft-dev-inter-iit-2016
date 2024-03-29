package example.com.alerto;

import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import example.com.alerto.contactloader.Contact;

public class FavRecyclerAdapter extends RecyclerView.Adapter<FavRecyclerAdapter.ContactViewHolder> {

    private List<ContactItem> itemList;
    MainActivity c;
    ContactItem ci;
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

    public FavRecyclerAdapter(List<ContactItem> itemList, MainActivity mActivity) {
        this.itemList = itemList;
        this.c = mActivity;

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder,final int i) {
        ci = itemList.get(i);
        contactViewHolder.nameText.setText(ci.getItemName());
        contactViewHolder.phoneText.setText(ci.getPhoneNo());
        contactViewHolder.iconText.setText((ci.getItemName().substring(0, 1).toUpperCase()));
        contactViewHolder.iconBack.setColorFilter(Color.parseColor(colors[i % 16]));
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