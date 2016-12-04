package su.ict.business59.partnersforpurchasing.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kaowneaw on 12/3/2016.
 */

public class ProductImg implements Parcelable {
    private String pimg_id;
    private String pimg_url;

    public String getPimg_id() {
        return pimg_id;
    }

    public void setPimg_id(String pimg_id) {
        this.pimg_id = pimg_id;
    }

    public String getPimg_url() {
        return pimg_url;
    }

    public void setPimg_url(String pimg_url) {
        this.pimg_url = pimg_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pimg_id);
        dest.writeString(this.pimg_url);
    }

    public ProductImg() {
    }

    protected ProductImg(Parcel in) {
        this.pimg_id = in.readString();
        this.pimg_url = in.readString();
    }

    public static final Parcelable.Creator<ProductImg> CREATOR = new Parcelable.Creator<ProductImg>() {
        @Override
        public ProductImg createFromParcel(Parcel source) {
            return new ProductImg(source);
        }

        @Override
        public ProductImg[] newArray(int size) {
            return new ProductImg[size];
        }
    };
}
