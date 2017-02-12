package su.ict.business59.partnersforpurchasing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.models.Message;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

/**
 * Created by kaowneaw on 1/28/2017.
 */

public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ViewHolder> {

    private List<Message> msgList;
    private Context mContext;
    private final String host;
    private String myFormat = "yyyy-MM-dd hh:mm"; //In which you need put here
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private UserPreference pref;
    private String CurrentUserId;

    public ChatMsgAdapter(List<Message> msgList, Context mContext) {
        this.msgList = msgList;
        this.mContext = mContext;
        this.host = mContext.getResources().getString(R.string.host);
        this.pref = new UserPreference(this.mContext);
        this.CurrentUserId = this.pref.getUserObject().getUser_id();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_msg, parent, false);
        // Return a new holder instance
        return new ChatMsgAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message msg = msgList.get(position);
        if (msg.getUserId().equals(this.CurrentUserId)) {
            holder.warp_self.setVisibility(View.VISIBLE);
            holder.warp_other.setVisibility(View.GONE);
            holder.tv_msg_self.setText(msg.getText());
            holder.date_self.setText(msg.getDate());
        } else {
            holder.warp_self.setVisibility(View.GONE);
            holder.warp_other.setVisibility(View.VISIBLE);
            holder.tv_msg_other.setText(msg.getText());
            Picasso.with(mContext).load(host + msg.getImgProfile()).fit().centerCrop().into(holder.img_profile_other);
            holder.date_other.setText(msg.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_msg_self;
        private TextView tv_msg_other;
        private TextView date_other;
        private TextView date_self;
        private RelativeLayout warp_self;
        private RelativeLayout warp_other;
        private ImageView img_profile_other;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_msg_self = (TextView) itemView.findViewById(R.id.tv_msg_self);
            tv_msg_other = (TextView) itemView.findViewById(R.id.tv_msg_other);
            date_other = (TextView) itemView.findViewById(R.id.date_other);
            date_self = (TextView) itemView.findViewById(R.id.date_self);
            warp_self = (RelativeLayout) itemView.findViewById(R.id.warp_self);
            warp_other = (RelativeLayout) itemView.findViewById(R.id.warp_other);
            img_profile_other = (ImageView) itemView.findViewById(R.id.img_profile_other);
        }
    }
}
