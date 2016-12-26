package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaowneaw on 12/25/2016.
 */

public class Post extends Shop {

    @SerializedName("post_id")
    @Expose
    int productId;
    @SerializedName("post_name")
    @Expose
    String postName;
    @SerializedName("post_desc")
    @Expose
    String postDesc;
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
}
