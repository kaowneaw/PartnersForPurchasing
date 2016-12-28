package su.ict.business59.partnersforpurchasing.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaowneaw on 12/25/2016.
 */

public class Post extends Shop implements Parcelable {

    @SerializedName("post_id")
    @Expose
    int productId;
    @SerializedName("post_name")
    @Expose
    String postName;
    @SerializedName("post_desc")
    @Expose
    String postDesc;
    @SerializedName("post_start")
    @Expose
    String postStart;
    @SerializedName("post_end")
    @Expose
    String postEnd;
    @SerializedName("post_img")
    @Expose
    String postImg;
    @SerializedName("category_id")
    @Expose
    String catId;
    @SerializedName("category_name")
    @Expose
    String catName;
    @SerializedName("max_joined")
    @Expose
    String maxJoin;

    public int getProductId() {
        return productId;
    }

    public String getPostName() {
        return postName;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public String getPostEnd() {
        return postEnd;
    }

    public String getPostImg() {
        return postImg;
    }

    public String getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostStart() {
        return postStart;
    }

    public void setPostStart(String postStart) {
        this.postStart = postStart;
    }

    public void setPostEnd(String postEnd) {
        this.postEnd = postEnd;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getMaxJoin() {
        return maxJoin;
    }

    public void setMaxJoin(String maxJoin) {
        this.maxJoin = maxJoin;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeString(this.postName);
        dest.writeString(this.postDesc);
        dest.writeString(this.postStart);
        dest.writeString(this.postEnd);
        dest.writeString(this.postImg);
        dest.writeString(this.catId);
        dest.writeString(this.catName);
        dest.writeString(this.maxJoin);
        dest.writeString(shopName);
    }

    public Post() {
    }

    protected Post(Parcel in) {
        this.productId = in.readInt();
        this.postName = in.readString();
        this.postDesc = in.readString();
        this.postStart = in.readString();
        this.postEnd = in.readString();
        this.postImg = in.readString();
        this.catId = in.readString();
        this.catName = in.readString();
        this.maxJoin = in.readString();
        shopName = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
