package example.com.alerto;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import example.com.alerto.contactloader.Contact;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder> {

    private List<ContactItem> itemList;
    Context c;

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
    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.discover_card, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView nameText;
        protected TextView phoneText;

        public ContactViewHolder(View v) {
            super(v);
            nameText =  (TextView) v.findViewById(R.id.nameText);
            phoneText = (TextView) v.findViewById(R.id.phoneText);
        }
    }
}