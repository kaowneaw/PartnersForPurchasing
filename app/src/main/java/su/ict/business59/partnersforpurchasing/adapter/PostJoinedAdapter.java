package su.ict.business59.partnersforpurchasing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.SHOPSHARE;
import su.ict.business59.partnersforpurchasing.models.MemberJoin;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

/**
 * Created by kaowneaw on 3/11/2017.
 */

public class PostJoinedAdapter extends RecyclerView.Adapter<PostJoinedAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Post> postList;
    // Store the context for easy access
    private Context mContext;
    private final PostJoinedAdapter.OnItemClickListener listener;
    private final String host;
    private String myFormat = "yyyy-MM-dd hh:mm"; //In which you need put here
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private UserPreference pref;
    private String CurrentUserId;
    private Shop currentUser;


    public PostJoinedAdapter(List<Post> postList, Context mContext, PostJoinedAdapter.OnItemClickListener listener) {
        this.postList = postList;
        this.mContext = mContext;
        this.listener = listener;
        this.host = mContext.getResources().getString(R.string.host);
        this.pref = new UserPreference(this.mContext);
        this.CurrentUserId = this.pref.getUserObject().getUser_id();
        UserPreference uref = new UserPreference(this.mContext);
        this.currentUser = uref.getUserObject();
    }

    public interface OnItemClickListener {
        void onItemClick(Post item);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public void updateData(List<Post> list) {
        this.postList = list;
    }

    // Easy access to the context object in the recyclerview
    protected Context getContext() {
        return mContext;
    }

    @Override
    public PostJoinedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.post_item_joined, parent, false);
        // Return a new holder instance
        return new PostJoinedAdapter.ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(PostJoinedAdapter.ViewHolder holder, int position) {
        Post mPost = postList.get(position);
        holder.title_text.setText(mPost.getPostName());
        holder.category_text.setText(mPost.getCatName());
        Picasso.with(mContext).load(host + mPost.getPostImg()).fit().centerCrop().into(holder.img_product);
        Picasso.with(mContext).load(SHOPSHARE.getPathImg(mPost.getImage_url())).fit().centerCrop().into(holder.user_img);
        holder.user_text.setText(mPost.getUsername());
        holder.date.setText("โพสเมื่อ " + mPost.getPostTime());
        int amount = calAmountRequire(mPost);
        if (amount <= 0) {
            holder.amountRequire.setText("ครบจำนวนแล้ว");
        } else {
            holder.amountRequire.setText(amount + " " + mPost.getUnitRequire());
        }

        if (mPost.getPromotionId() == null || mPost.getPromotionId().equals("")) {
            holder.promotion.setVisibility(View.GONE);
        } else {
            holder.promotion.setVisibility(View.VISIBLE);
            holder.promotion.setText(mPost.getPromotionName());
        }
    }

    private boolean checkMemberJoined(List<MemberJoin> listJoin) {
        for (MemberJoin member : listJoin) {
            if (member.getUser_id().equals(currentUser.getUser_id())) {
                return true;
            }
        }

        return false;
    }

    private int calAmountRequire(Post post) {

        int totalAmount = post.getAmountRequire();
        int userJoinAmount = 0;
        for (MemberJoin join : post.getMemberJoin()) {
            userJoinAmount += join.getAmount();
        }

        return totalAmount - userJoinAmount;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView title_text;
        private TextView category_text;
        private TextView date;
        private ImageView img_product;
        private ImageView user_img;
        private TextView user_text;
        private TextView promotion;
        private TextView showJoined;
        private TextView amountRequire;
        private LinearLayout postItem;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            postItem = (LinearLayout) itemView.findViewById(R.id.postItem);
            title_text = (TextView) itemView.findViewById(R.id.title_text);
            category_text = (TextView) itemView.findViewById(R.id.category_text);
            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            user_img = (ImageView) itemView.findViewById(R.id.user_img);
            user_text = (TextView) itemView.findViewById(R.id.user_text);
            date = (TextView) itemView.findViewById(R.id.date);
            promotion = (TextView) itemView.findViewById(R.id.promotion);
            showJoined = (TextView) itemView.findViewById(R.id.showJoined);
            amountRequire = (TextView) itemView.findViewById(R.id.amountRequire);
        }


        @Override
        public void onClick(View view) {
            listener.onItemClick(postList.get(getAdapterPosition()));
        }
    }
}
