package su.ict.business59.partnersforpurchasing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.models.MemberJoin;


public class MemberJoinAdapter extends RecyclerView.Adapter<MemberJoinAdapter.ViewHolder> {
    // Store a member variable for the contacts
    private List<MemberJoin> joinedList;
    // Store the context for easy access
    private Context mContext;
    private final String host;
    private String unit;

    public MemberJoinAdapter(Context mContext, List<MemberJoin> joinedList, String unit) {
        this.mContext = mContext;
        this.joinedList = joinedList;
        this.host = mContext.getResources().getString(R.string.host);
        this.unit = unit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_dialog_member_join, parent, false);
        // Return a new holder instance
        return new MemberJoinAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemberJoin mMemberJoin = joinedList.get(position);
        holder.txt_username.setText(String.valueOf(mMemberJoin.getUsername()));
        holder.amount.setText(String.valueOf(mMemberJoin.getAmount()) + " " + this.unit);
        Picasso.with(mContext).load(host + mMemberJoin.getImage_url()).fit().centerCrop().into(holder.user_img);
    }

    @Override
    public int getItemCount() {
        return joinedList.size();
    }

    // Provide a direct reference to each of the views within a data item
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private ImageView user_img;
        private TextView txt_username;
        private TextView amount;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            txt_username = (TextView) itemView.findViewById(R.id.txt_username);
            user_img = (ImageView) itemView.findViewById(R.id.user_img);
            amount = (TextView) itemView.findViewById(R.id.amount);
        }

        @Override
        public void onClick(View view) {


        }
    }
}
