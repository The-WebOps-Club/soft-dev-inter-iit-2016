package example.com.alerto;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder> {

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

    public ContactRecyclerAdapter(List<ContactItem> itemList, Context context) {
        this.itemList = itemList;
        this.c = context;

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder,final int i) {
        ContactItem ci = itemList.get(i);
        contactViewHolder.nameText.setText(ci.getItemName());
        contactViewHolder.phoneText.setText(ci.getPhoneNo());
        try {
            contactViewHolder.iconText.setText((ci.getItemName().substring(0, 1).toUpperCase()));
        }catch (Exception e) {
            contactViewHolder.iconText.setText("S");
        }
        contactViewHolder.iconBack.setColorFilter(Color.parseColor(colors[i % 16]));
        contactViewHolder.llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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