package su.ict.business59.partnersforpurchasing.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;

/**
 * Created by kaowneaw on 12/3/2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Product> productList;
    // Store the context for easy access
    private Context mContext;
    private final OnItemClickListener listener;

    public ProductAdapter(List<Product> productList, Context mContext, OnItemClickListener listener) {
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
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.product_item, parent, false);

        // Return a new holder instance
        return new ProductAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product mProduct = productList.get(position);
        holder.product_name.setText(mProduct.getProductName());
        holder.product_desc.setText(mProduct.getProductDesc());
        holder.product_price.setText(String.valueOf(mProduct.getProductPrice()) + " บาท");
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
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView product_name;
        private TextView product_desc;
        private TextView product_price;
        private ImageView img_product;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_desc = (TextView) itemView.findViewById(R.id.product_desc);
            product_price = (TextView) itemView.findViewById(R.id.product_price);
            img_product = (ImageView) itemView.findViewById(R.id.img_product);
        }


        @Override
        public void onClick(View view) {
            listener.onItemClick(productList.get(getAdapterPosition()));

        }

        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(getContext(), "" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            ProductService service = ServiceGenerator.createService(ProductService.class);
                            Call<BaseResponse> call = service.removeProduct(productList.get(getAdapterPosition()).getProductId() + "");
                            call.enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().isStatus()) {
                                            productList.remove(getAdapterPosition());
                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        try {
                                            Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {

                                }
                            });

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Do you want to Remove ?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            return true;
        }
    }
}
