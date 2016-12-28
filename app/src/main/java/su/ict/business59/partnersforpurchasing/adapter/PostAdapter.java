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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.models.Post;

/**
 * Created by kaowneaw on 12/25/2016.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    // Store a member variable for the contacts
    private List<Post> postList;
    // Store the context for easy access
    private Context mContext;
    private final PostAdapter.OnItemClickListener listener;
    private final String host;
    String myFormat = "yyyy-MM-dd hh:mm"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public PostAdapter(List<Post> postList, Context mContext, PostAdapter.OnItemClickListener listener) {
        this.postList = postList;
        this.mContext = mContext;
        this.listener = listener;
        this.host = mContext.getResources().getString(R.string.host);
    }

    public interface OnItemClickListener {
        void onItemClick(Post item);

        void onJoinButtonClick(int index);
    }

    // Easy access to the context object in the recyclerview
    protected Context getContext() {
        return mContext;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.post_item, parent, false);
        // Return a new holder instance
        return new PostAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        Post mPost = postList.get(position);
        holder.title_text.setText(mPost.getPostName());
        holder.shop_text.setText("Shop : " + mPost.getShopName());
        try {
            holder.date_text.setText("Due Date : " + sdf.format(sdf.parse(mPost.getPostEnd())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.category_text.setText("Category : " + mPost.getCatName());
        Picasso.with(mContext).load(host + mPost.getPostImg()).fit().centerCrop().into(holder.img_product);
        Picasso.with(mContext).load(host + mPost.getImage_url()).fit().centerCrop().into(holder.user_img);
        holder.user_text.setText(mPost.getUsername());
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    // Provide a direct reference to each of the views within a data item
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView title_text;
        private TextView category_text;
        private TextView shop_text;
        private TextView date_text;
        private ImageView img_product;
        private ImageView user_img;
        private TextView user_text;
        private Button join_btn;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            title_text = (TextView) itemView.findViewById(R.id.title_text);
            category_text = (TextView) itemView.findViewById(R.id.category_text);
            shop_text = (TextView) itemView.findViewById(R.id.shop_text);
            date_text = (TextView) itemView.findViewById(R.id.date_text);
            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            user_img = (ImageView) itemView.findViewById(R.id.user_img);
            user_text = (TextView) itemView.findViewById(R.id.user_text);
            join_btn = (Button) itemView.findViewById(R.id.join_btn);
            join_btn.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view == join_btn) {
                listener.onJoinButtonClick(getAdapterPosition());
            } else {
                listener.onItemClick(postList.get(getAdapterPosition()));
            }

        }
    }
}
