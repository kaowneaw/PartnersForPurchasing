package su.ict.business59.partnersforpurchasing.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Product extends BaseResponse implements Parcelable {

    @SerializedName("product_id")
    @Expose
    int productId;
    @SerializedName("product_name")
    @Expose
    String productName;
    @SerializedName("product_desc")
    @Expose
    String productDesc;
    @SerializedName("product_price")
    @Expose
    double productPrice;
    @SerializedName("created")
    @Expose
    String created;
    @SerializedName("category_id")
    @Expose
    String categoryId;
    @SerializedName("shop_id")
    @Expose
    String shopId;
    @SerializedName("img_list")
    @Expose
    List<ProductImg> imgList;

    public Product(int productId, String productName, String productDesc, double productPrice, String created, String categoryId, String shopId, List<ProductImg> imgList) {
        this.productId = productId;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.created = created;
        this.categoryId = categoryId;
        this.shopId = shopId;
        this.imgList = imgList;
    }

    public List<ProductImg> getImgList() {
        return imgList;
    }

    public void setImgList(List<ProductImg> imgList) {
        this.imgList = imgList;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productDesc);
        dest.writeDouble(this.productPrice);
        dest.writeString(this.created);
        dest.writeString(this.categoryId);
        dest.writeString(this.shopId);
        dest.writeList(this.imgList);
    }

    protected Product(Parcel in) {
        this.productId = in.readInt();
        this.productName = in.readString();
        this.productDesc = in.readString();
        this.productPrice = in.readDouble();
        this.created = in.readString();
        this.categoryId = in.readString();
        this.shopId = in.readString();
        this.imgList = new ArrayList<ProductImg>();
        in.readList(this.imgList, ProductImg.class.getClassLoader());
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
