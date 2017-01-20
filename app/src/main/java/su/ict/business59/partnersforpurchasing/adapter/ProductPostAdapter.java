package su.ict.business59.partnersforpurchasing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.models.Product;

/**
 * Created by kaowneaw on 1/20/2017.
 */

public class ProductPostAdapter extends RecyclerView.Adapter<ProductPostAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Product> productList;
    // Store the context for easy access
    private Context mContext;
    private final ProductPostAdapter.OnItemClickListener listener;

    public ProductPostAdapter(List<Product> productList, Context mContext, ProductPostAdapter.OnItemClickListener listener) {
        this.productList = productList;
        this.mContext = mContext;
        this.listener = listener;

    }

    public interface OnItemClickListener {
        void onItemClick(Product item);
    }


    // Easy access to the context object in the recyclerview
    protected Context getContext() {
        return mContext;
    }

    @Override
    public ProductPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_product_post, parent, false);

        // Return a new holder instance
        return new ProductPostAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ProductPostAdapter.ViewHolder holder, int position) {
        Product mProduct = productList.get(position);
        holder.title_text.setText(mProduct.getProductName());
        holder.category_text.setText(mProduct.getCatName());
        holder.promotion.setText(mProduct.getPromotion_name());
        holder.shop_text.setText(mProduct.getShopName() + '\n' + mProduct.getAddressShopString());
        if (mProduct.getImgList().size() > 0) {
            String host = mContext.getResources().getString(R.string.host);
            Picasso.with(mContext).load(host + mProduct.getImgList().get(0).getPimg_url()).fit().centerCrop().into(holder.img_product);
        } else {
            Picasso.with(mContext).load(R.mipmap.product_default).fit().centerInside().into(holder.img_product);
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Provide a direct reference to each of the views within a data item
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView title_text;
        private TextView category_text;
        private ImageView img_product;
        private TextView promotion;
        private TextView shop_text;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            title_text = (TextView) itemView.findViewById(R.id.title_text);
            category_text = (TextView) itemView.findViewById(R.id.category_text);
            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            promotion = (TextView) itemView.findViewById(R.id.promotion);
            shop_text = (TextView) itemView.findViewById(R.id.shop_text);
        }


        @Override
        public void onClick(View view) {
            listener.onItemClick(productList.get(getAdapterPosition()));
        }

    }
}