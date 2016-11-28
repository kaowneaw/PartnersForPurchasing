package su.ict.business59.partnersforpurchasing.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import su.ict.business59.partnersforpurchasing.R;


public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Uri> imgList;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public SelectImageAdapter(Context context, List<Uri> imgList) {
        this.imgList = imgList;
        this.mContext = context;
    }

    // Easy access to the context object in the recyclerview
    protected Context getContext() {
        return mContext;
    }


    @Override
    public SelectImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.img_row, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(SelectImageAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Uri imgUri = imgList.get(position);
        holder.setItem(position + "");
        // Set item views based on your views and data model
        ImageView imgHolder = holder.imgView;
        Picasso.with(mContext).load(imgUri).fit().centerCrop().into(imgHolder);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    // Provide a direct reference to each of the views within a data item
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private ImageView imgView;
        private String mItem;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            imgView = (ImageView) itemView.findViewById(R.id.imgItem);

        }

        public void setItem(String item) {
            mItem = item;
        }

        @Override
        public void onClick(View view) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            imgList.remove(getPosition());
                            notifyDataSetChanged();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Remove ?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }
}
